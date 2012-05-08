package org.sonatype.nexus.plugins.nexus5046;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.rest.AbstractNexusPlexusResource;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;
import org.sonatype.plexus.rest.resource.PlexusResource;

import com.thoughtworks.xstream.XStream;

@Component( role = PlexusResource.class, hint = "SelectorActorPlexusResource" )
public class SelectorActorPlexusResource
    extends AbstractNexusPlexusResource
{
    private static final String REPOSITORY_ID = "repositoryId";

    private static final String SELECTOR_ID = "selectorId";

    private static final String ACTOR_ID = "actorId";

    @Requirement( role = Selector.class )
    private Map<String, Selector> selectors;

    @Requirement( role = Actor.class )
    private Map<String, Actor> actors;

    public SelectorActorPlexusResource()
    {
        setReadable( true );
        setModifiable( false );
    }

    @Override
    public String getResourceUri()
    {
        return "/select/{" + REPOSITORY_ID + "}/{" + SELECTOR_ID + "}/{" + ACTOR_ID + "}";
    }

    @Override
    public Object getPayloadInstance()
    {
        return null;
    }

    @Override
    public PathProtectionDescriptor getResourceProtection()
    {
        return new PathProtectionDescriptor( "/select/*/*/*", "authcBasic" );
    }

    @Override
    public void configureXStream( XStream xstream )
    {
        super.configureXStream( xstream );
        xstream.processAnnotations( RunReport.class );
    }

    public Object get( final Context context, final Request request, final Response response, final Variant variant )
        throws ResourceException
    {
        try
        {
            final Form form = request.getResourceRef().getQueryAsForm();
            final Map<String, String> terms = new HashMap<String, String>();
            for ( Parameter parameter : form )
            {
                String paramName = parameter.getName();
                if ( paramName.startsWith( "t_" ) && paramName.length() > 2 )
                {
                    terms.put( paramName.substring( 2 ), parameter.getValue() );
                }
            }

            final String selectorKey = request.getAttributes().get( SELECTOR_ID ).toString();
            final Selector selector = selectors.get( selectorKey );
            if ( selector == null )
            {
                throw new IllegalArgumentException( "Selector not found!" );
            }

            final String actorKey = request.getAttributes().get( ACTOR_ID ).toString();
            final Actor actor = actors.get( actorKey );
            if ( actor == null )
            {
                throw new IllegalArgumentException( "Actor not found!" );
            }

            final Repository repository;
            try
            {
                repository =
                    getRepositoryRegistry().getRepository( request.getAttributes().get( REPOSITORY_ID ).toString() );
            }
            catch ( NoSuchRepositoryException e )
            {
                throw new IllegalArgumentException( "Repository not found!" );
            }

            final Selection selection = selector.select( repository, terms );
            final int selectionSize = selection.size();
            final int actedSize = actor.perform( selection, terms );

            return new RunReport( repository.getId(), selectorKey, selectionSize, actorKey, actedSize, true );
        }
        catch ( IllegalArgumentException t )
        {
            throw new ResourceException( Status.CLIENT_ERROR_BAD_REQUEST, t );
        }
        catch ( IOException t )
        {
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, t );
        }
    }

}
