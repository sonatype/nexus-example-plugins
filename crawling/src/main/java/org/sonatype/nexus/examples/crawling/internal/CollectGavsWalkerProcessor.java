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

import org.sonatype.nexus.examples.crawling.ArtifactDiscoveryListener;
import org.sonatype.nexus.proxy.item.StorageCollectionItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.item.uid.IsHiddenAttribute;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

import com.google.common.base.Preconditions;

/**
 * ???
 *
 * @since 1.0
 */
public class CollectGavsWalkerProcessor
    extends AbstractWalkerProcessor
{
    private final MavenRepository mavenRepository;

    private final ArtifactDiscoveryListener artifactDiscoveryListener;

    public CollectGavsWalkerProcessor( final MavenRepository mavenRepository,
                                       final ArtifactDiscoveryListener artifactDiscoveryListener )
    {
        this.mavenRepository = Preconditions.checkNotNull( mavenRepository );
        this.artifactDiscoveryListener = Preconditions.checkNotNull( artifactDiscoveryListener );
    }

    public MavenRepository getRepository()
    {
        return mavenRepository;
    }

    // == WalkerProcessor

    @Override
    public void beforeWalk( final WalkerContext context )
        throws Exception
    {
        super.beforeWalk( context );
        artifactDiscoveryListener.beforeWalk( getRepository() );
    }

    @Override
    public final void processItem( final WalkerContext context, final StorageItem item )
        throws Exception
    {
        if ( item instanceof StorageCollectionItem )
        {
            return; // a directory
        }
        if ( item.getRepositoryItemUid().getBooleanAttributeValue( IsHiddenAttribute.class ) )
        {
            return; // leave out hidden stuff
        }

        // gav might be null, if item passed in does not obey maven2 layout
        // note: item still can be "file" or "link", but not "directory"!
        final Gav gav = getRepository().getGavCalculator().pathToGav( item.getPath() );
        artifactDiscoveryListener.onArtifactDiscovery( getRepository(), gav, item );
    }

    public void afterWalk( final WalkerContext context )
        throws Exception
    {
        super.afterWalk( context );
        artifactDiscoveryListener.afterWalk( getRepository() );
    }
}
