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

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.examples.selectionactors.Selection;
import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.examples.selectionactors.SelectionFactory;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.targets.Target;
import org.sonatype.nexus.proxy.targets.TargetRegistry;
import org.sonatype.nexus.proxy.walker.Walker;

/**
 * ???
 *
 * @since 1.0
 */
@Named("target")
@Singleton
public class TargetSelector
    extends AbstractWalkingSelector
{
/**
   * The key for target ID term.
   */
  public static final String TERM_TARGET_ID = "targetId";

  private final TargetRegistry targetRegistry;

  public TargetSelector(final SelectionFactory selectionFactory, final Walker walker,
          final TargetRegistry targetRegistry) {
      super(selectionFactory, walker);
      this.targetRegistry = targetRegistry;
  }

  @Override
  public Selection select(final Repository repository, final Map<String, String> terms) {
    final String targetId = terms.get(TERM_TARGET_ID);
    if (targetId == null) {
      throw new IllegalArgumentException("Term " + TERM_TARGET_ID + " not found or is empty!");
    }

    final Target target = targetRegistry.getRepositoryTarget(targetId);
    if (target == null) {
      throw new IllegalArgumentException("Target with ID=\"" + targetId + "\" not found!");
    }

    final SelectionCollector collector = getSelectionFactory().getCollector();
    final TargetWalkerProcessor twp = new TargetWalkerProcessor(collector, target);
    walk(repository, new ResourceStoreRequest("/"), twp);
    return collector.done();
  }
}
