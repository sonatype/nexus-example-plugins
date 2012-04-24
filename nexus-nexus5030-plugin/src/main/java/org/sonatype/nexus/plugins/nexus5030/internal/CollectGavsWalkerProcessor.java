/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.nexus5030.internal;

import org.sonatype.nexus.plugins.nexus5030.ArtifactDiscoveryListener;
import org.sonatype.nexus.proxy.item.StorageCollectionItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.item.uid.IsHiddenAttribute;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

import com.google.common.base.Preconditions;

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
