package org.sonatype.nexus.plugins.nexus5046.selectors;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.plugins.nexus5046.Selector;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.target.Target;
import org.sonatype.nexus.proxy.target.TargetRegistry;

@Component( role = Selector.class, hint = "target" )
public class TargetSelector
    extends AbstractWalkingSelector
{
    /**
     * The key for target ID term.
     */
    public static final String TERM_TARGET_ID = "targetId";

    @Requirement
    private TargetRegistry targetRegistry;

    @Override
    public Selection select( final Repository repository, final Map<String, String> terms )
    {
        final String targetId = terms.get( TERM_TARGET_ID );
        if ( targetId == null )
        {
            throw new IllegalArgumentException( "Term " + TERM_TARGET_ID + " not found or is empty!" );
        }

        final Target target = targetRegistry.getRepositoryTarget( targetId );
        if ( target == null )
        {
            throw new IllegalArgumentException( "Target with ID=\"" + targetId + "\" not found!" );
        }

        final SelectionCollector collector = getSelectionFactory().getCollector();
        final TargetWalkerProcessor twp = new TargetWalkerProcessor( collector, target );
        walk( repository, new ResourceStoreRequest( "/" ), twp );
        return collector.done();
    }
}
