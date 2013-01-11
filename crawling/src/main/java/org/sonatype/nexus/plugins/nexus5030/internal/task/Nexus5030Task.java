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
package org.sonatype.nexus.plugins.nexus5030.internal.task;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.nexus.plugins.nexus5030.ArtifactDiscoveryListener;
import org.sonatype.nexus.plugins.nexus5030.GavCollector;
import org.sonatype.nexus.plugins.nexus5030.internal.FileArtifactDiscoveryListener;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.scheduling.AbstractNexusRepositoriesPathAwareTask;
import org.sonatype.scheduling.SchedulerTask;

/**
 * Empty trash.
 */
@Component( role = SchedulerTask.class, hint = Nexus5030TaskDescriptor.ID, instantiationStrategy = "per-lookup" )
public class Nexus5030Task
    extends AbstractNexusRepositoriesPathAwareTask<Object>
{
    private static final String ACTION = "NEXUS5030";

    @Requirement
    private ApplicationConfiguration applicationConfiguration;

    @Requirement
    private GavCollector gavCollector;

    @Override
    protected String getRepositoryFieldId()
    {
        return Nexus5030TaskDescriptor.REPOSITORY_FIELD_ID;
    }

    @Override
    protected String getRepositoryPathFieldId()
    {
        return Nexus5030TaskDescriptor.REPOSITORY_PATH_FIELD_ID;
    }

    @Override
    protected Object doRun()
        throws IOException
    {
        if ( getRepositoryId() != null )
        {
            try
            {
                final MavenRepository mavenRepository =
                    getRepositoryRegistry().getRepositoryWithFacet( getRepositoryId(), MavenRepository.class );
                if ( mavenRepository != null )
                {
                    gavCollector.collectGAVs( createRequest(), mavenRepository, createListener( mavenRepository ) );
                }
            }
            catch ( NoSuchRepositoryException e )
            {
                getLogger().warn( "No MavenRepository with ID={} exists!", getRepositoryId() );
            }
        }
        else
        {
            for ( MavenRepository mavenRepository : getRepositoryRegistry().getRepositoriesWithFacet(
                MavenRepository.class ) )
            {
                gavCollector.collectGAVs( createRequest(), mavenRepository, createListener( mavenRepository ) );
            }
        }

        return null;
    }

    @Override
    protected String getAction()
    {
        return ACTION;
    }

    @Override
    protected String getMessage()
    {
        return "Harvesting all the artifacts in Maven repository.";
    }

    // ==

    protected ResourceStoreRequest createRequest()
    {
        return new ResourceStoreRequest( getResourceStorePath(), true );
    }

    protected ArtifactDiscoveryListener createListener( final MavenRepository mavenRepository )
        throws IOException
    {
        return new FileArtifactDiscoveryListener( new File(
            applicationConfiguration.getWorkingDirectory( "nexus-5030" ), mavenRepository.getId() + ".txt" ) );
    }
}
