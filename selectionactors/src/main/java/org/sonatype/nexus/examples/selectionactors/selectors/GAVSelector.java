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

package org.sonatype.nexus.examples.selectionactors.selectors;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.examples.selectionactors.Selection;
import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.examples.selectionactors.SelectionFactory;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.utils.RepositoryStringUtils;
import org.sonatype.nexus.proxy.walker.Walker;

import com.google.common.base.Preconditions;

/**
 * ???
 *
 * @since 1.0
 */
@Named("gav")
@Singleton
public class GAVSelector
    extends AbstractWalkingSelector
{
  @Inject
  public GAVSelector(final SelectionFactory selectionFactory, final Walker walker) {
        super(Preconditions.checkNotNull(selectionFactory), Preconditions.checkNotNull(walker));
  }

  @Override
  public Selection select(final Repository repository, final Map<String, String> terms) {
    final MavenRepository mavenRepository = repository.adaptToFacet(MavenRepository.class);
    if (mavenRepository == null) {
      throw new IllegalArgumentException(RepositoryStringUtils.getFormattedMessage(
          "%s is not a maven repository!", repository));
    }
    else {
      final SelectionCollector collector = getSelectionFactory().getCollector();
      final GAVWalkerProcessor gwp = new GAVWalkerProcessor(mavenRepository, new GAVPC(terms), collector);
      walk(mavenRepository, new ResourceStoreRequest("/"), gwp);
      return collector.done();
    }
  }
}
