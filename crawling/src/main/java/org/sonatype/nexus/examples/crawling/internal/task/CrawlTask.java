/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package org.sonatype.nexus.examples.crawling.internal.task;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.nexus.examples.crawling.ArtifactDiscoveryListener;
import org.sonatype.nexus.examples.crawling.GavCollector;
import org.sonatype.nexus.examples.crawling.internal.FileArtifactDiscoveryListener;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.scheduling.AbstractNexusRepositoriesPathAwareTask;

import com.google.inject.Inject;


/**
 * ???
 *
 * @since 1.0
 */
@Named(CrawlTaskDescriptor.ID)
public class CrawlTask
    extends AbstractNexusRepositoriesPathAwareTask<Object>
{
  private static final String ACTION = "NEXUS5030";

  private final ApplicationConfiguration applicationConfiguration;

  private final GavCollector gavCollector;

  @Inject
  public CrawlTask(final ApplicationConfiguration applicationConfiguration, GavCollector gavCollector) {
      this.applicationConfiguration = applicationConfiguration;
      this.gavCollector = gavCollector;
  }

  @Override
  protected String getRepositoryFieldId() {
    return CrawlTaskDescriptor.REPOSITORY_FIELD_ID;
  }

  @Override
  protected String getRepositoryPathFieldId() {
    return CrawlTaskDescriptor.REPOSITORY_PATH_FIELD_ID;
  }

  @Override
  protected Object doRun()
      throws IOException
  {
    if (getRepositoryId() != null) {
      try {
        final MavenRepository mavenRepository =
            getRepositoryRegistry().getRepositoryWithFacet(getRepositoryId(), MavenRepository.class);
        if (mavenRepository != null) {
          gavCollector.collectGAVs(createRequest(), mavenRepository, createListener(mavenRepository));
        }
      }
      catch (NoSuchRepositoryException e) {
        getLogger().warn("No MavenRepository with ID={} exists!", getRepositoryId());
      }
    }
    else {
      for (MavenRepository mavenRepository : getRepositoryRegistry().getRepositoriesWithFacet(
          MavenRepository.class)) {
        gavCollector.collectGAVs(createRequest(), mavenRepository, createListener(mavenRepository));
      }
    }

    return null;
  }

  @Override
  protected String getAction() {
    return ACTION;
  }

  @Override
  protected String getMessage() {
    return "Harvesting all the artifacts in Maven repository.";
  }

  // ==

  protected ResourceStoreRequest createRequest() {
    return new ResourceStoreRequest(getResourceStorePath(), true);
  }

  protected ArtifactDiscoveryListener createListener(final MavenRepository mavenRepository)
      throws IOException
  {
    return new FileArtifactDiscoveryListener(new File(
        applicationConfiguration.getWorkingDirectory("crawling"), mavenRepository.getId() + ".txt"));
  }
}
