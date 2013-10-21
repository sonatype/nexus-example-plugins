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

import javax.inject.Inject;

import org.sonatype.nexus.examples.selectionactors.SelectionFactory;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.walker.DefaultWalkerContext;
import org.sonatype.nexus.proxy.walker.Walker;
import org.sonatype.nexus.proxy.walker.WalkerException;
import org.sonatype.nexus.proxy.walker.WalkerProcessor;

import com.google.common.base.Preconditions;

/**
 * ???
 *
 * @since 1.0
 */
public abstract class AbstractWalkingSelector
    extends AbstractSelector
{
  private Walker walker;

  @Inject
  public AbstractWalkingSelector(final SelectionFactory selectionFactory, final Walker walker) {
      super(Preconditions.checkNotNull(selectionFactory));
      this.walker = Preconditions.checkNotNull(walker);
  }

  protected Walker getWalker() {
    return walker;
  }

  protected void walk(final Repository repository, final ResourceStoreRequest resourceStoreRequest,
                      final WalkerProcessor walkerProcessor)
      throws WalkerException
  {
    final DefaultWalkerContext context = new DefaultWalkerContext(repository, resourceStoreRequest);
    context.getProcessors().add(walkerProcessor);
    try {
      walker.walk(context);
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
