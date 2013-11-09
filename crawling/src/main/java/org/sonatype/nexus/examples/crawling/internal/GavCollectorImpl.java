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

package org.sonatype.nexus.examples.crawling.internal;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.examples.crawling.ArtifactDiscoveryListener;
import org.sonatype.nexus.examples.crawling.GavCollector;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.walker.DefaultWalkerContext;
import org.sonatype.nexus.proxy.walker.Walker;
import org.sonatype.nexus.proxy.walker.WalkerContext;
import org.sonatype.nexus.proxy.walker.WalkerException;
import org.sonatype.sisu.goodies.common.ComponentSupport;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

/**
 * ???
 *
 * @since 1.0
 */
@Named
@Singleton
public class GavCollectorImpl
    extends ComponentSupport
    implements GavCollector
{
  private final Walker walker;

  @Inject
  public GavCollectorImpl(final Walker walker) {
    this.walker = Preconditions.checkNotNull(walker);
  }

  @Override
  public void collectGAVs(final ResourceStoreRequest request, final MavenRepository mavenRepository,
                          final ArtifactDiscoveryListener listener)
      throws IOException
  {
    if (StringUtils.isEmpty(request.getRequestPath())) {
      request.setRequestPath(RepositoryItemUid.PATH_ROOT);
    }
    // make sure we crawl local content (caches) only
    request.setRequestLocalOnly(true);
    final WalkerContext walkerContext = new DefaultWalkerContext(mavenRepository, request);
    final CollectGavsWalkerProcessor collectGavsWalkerProcessor =
        new CollectGavsWalkerProcessor(mavenRepository, listener);
    walkerContext.getProcessors().add(collectGavsWalkerProcessor);

    try {
      walker.walk(walkerContext);
    }
    catch (WalkerException e) {
      if (!(e.getWalkerContext().getStopCause() instanceof ItemNotFoundException)) {
        // everything that is not ItemNotFound should be reported,
        // otherwise just neglect it
        throw e;
      }
    }
  }
}
