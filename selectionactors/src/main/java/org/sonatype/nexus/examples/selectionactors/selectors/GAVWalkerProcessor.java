/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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

import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

/**
 * ???
 *
 * @since 1.0
 */
public class GAVWalkerProcessor
    extends AbstractWalkerProcessor
{
  private final MavenRepository mavenRepository;

  private final GAVPC gavpc;

  private final SelectionCollector selectionCollector;

  public GAVWalkerProcessor(final MavenRepository mavenRepository, final GAVPC gavpc,
                            final SelectionCollector selectionCollector)
  {
    this.mavenRepository = mavenRepository;
    this.gavpc = gavpc;
    this.selectionCollector = selectionCollector;
  }

  @Override
  public void processItem(final WalkerContext context, final StorageItem item)
      throws Exception
  {
    final Gav gav = mavenRepository.getGavCalculator().pathToGav(item.getRepositoryItemUid().getPath());
    if (gav != null && gavpc.matches(gav)) {
      selectionCollector.add(item);
    }
  }

  public SelectionCollector getSelectionCollector() {
    return selectionCollector;
  }
}
