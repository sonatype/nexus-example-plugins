package org.sonatype.nexus.plugins.nexus5046.selectors;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.plugins.nexus5046.Selector;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.utils.RepositoryStringUtils;

@Component( role = Selector.class, hint = "gav" )
public class GAVSelector
    extends AbstractWalkingSelector
{
    @Override
    public Selection select( final Repository repository, final Map<String, String> terms )
    {
        final MavenRepository mavenRepository = repository.adaptToFacet( MavenRepository.class );
        if ( mavenRepository == null )
        {
            throw new IllegalArgumentException( RepositoryStringUtils.getFormattedMessage(
                "%s is not a maven repository!", repository ) );
        }
        else
        {
            final SelectionCollector collector = getSelectionFactory().getCollector();
            final GAVWalkerProcessor gwp = new GAVWalkerProcessor( mavenRepository, new GAVPC( terms ), collector );
            walk( mavenRepository, new ResourceStoreRequest( "/" ), gwp );
            return collector.done();
        }
    }
}
