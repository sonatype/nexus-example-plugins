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

import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.target.Target;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

/**
 * ???
 *
 * @since 1.0
 */
public class TargetWalkerProcessor
    extends AbstractWalkerProcessor
{
    private final SelectionCollector selectionCollector;

    private final Target target;

    public TargetWalkerProcessor( final SelectionCollector selectionCollector, final Target target )
    {
        this.selectionCollector = selectionCollector;
        this.target = target;
    }

    @Override
    public void processItem( final WalkerContext context, final StorageItem item )
        throws Exception
    {
        if ( target.isPathContained( context.getRepository().getRepositoryContentClass(),
            item.getRepositoryItemUid().getPath() ) )
        {
            selectionCollector.add( item );
        }
    }
}
