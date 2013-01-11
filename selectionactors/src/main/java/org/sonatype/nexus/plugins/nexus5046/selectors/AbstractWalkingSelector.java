package org.sonatype.nexus.plugins.nexus5046.selectors;

import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.walker.DefaultWalkerContext;
import org.sonatype.nexus.proxy.walker.Walker;
import org.sonatype.nexus.proxy.walker.WalkerException;
import org.sonatype.nexus.proxy.walker.WalkerProcessor;

public abstract class AbstractWalkingSelector
    extends AbstractSelector
{
    @Requirement
    private Walker walker;

    protected Walker getWalker()
    {
        return walker;
    }

    protected void walk( final Repository repository, final ResourceStoreRequest resourceStoreRequest,
                         final WalkerProcessor walkerProcessor )
        throws WalkerException
    {
        final DefaultWalkerContext context = new DefaultWalkerContext( repository, resourceStoreRequest );
        context.getProcessors().add( walkerProcessor );
        try
        {
            walker.walk( context );
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
