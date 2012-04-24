package org.sonatype.nexus.plugins.nexus5030.internal;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.codehaus.plexus.util.StringUtils;
import org.sonatype.nexus.logging.AbstractLoggingComponent;
import org.sonatype.nexus.plugins.nexus5030.ArtifactDiscoveryListener;
import org.sonatype.nexus.plugins.nexus5030.GavCollector;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.walker.DefaultWalkerContext;
import org.sonatype.nexus.proxy.walker.Walker;
import org.sonatype.nexus.proxy.walker.WalkerContext;
import org.sonatype.nexus.proxy.walker.WalkerException;

@Named
@Singleton
public class GavCollectorImpl
    extends AbstractLoggingComponent
    implements GavCollector
{
    private final Walker walker;

    @Inject
    public GavCollectorImpl( final Walker walker )
    {
        this.walker = walker;
    }

    @Override
    public void collectGAVs( final ResourceStoreRequest request, final MavenRepository mavenRepository,
                             final ArtifactDiscoveryListener listener )
        throws IOException
    {
        if ( StringUtils.isEmpty( request.getRequestPath() ) )
        {
            request.setRequestPath( RepositoryItemUid.PATH_ROOT );
        }
        // make sure we crawl local content (caches) only
        request.setRequestLocalOnly( true );
        final WalkerContext walkerContext = new DefaultWalkerContext( mavenRepository, request );
        final CollectGavsWalkerProcessor collectGavsWalkerProcessor =
            new CollectGavsWalkerProcessor( mavenRepository, listener );
        walkerContext.getProcessors().add( collectGavsWalkerProcessor );

        try
        {
            walker.walk( walkerContext );
        }
        catch ( WalkerException e )
        {
            if ( !( e.getWalkerContext().getStopCause() instanceof ItemNotFoundException ) )
            {
                // everything that is not ItemNotFound should be reported,
                // otherwise just neglect it
                throw e;
            }
        }
    }
}
