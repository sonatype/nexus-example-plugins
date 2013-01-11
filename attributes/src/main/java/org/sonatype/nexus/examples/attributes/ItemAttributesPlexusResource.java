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
package org.sonatype.nexus.examples.attributes;

import org.codehaus.plexus.component.annotations.Component;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.rest.AbstractResourceStoreContentPlexusResource;
import org.sonatype.nexus.rest.repositories.AbstractRepositoryPlexusResource;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;
import org.sonatype.plexus.rest.resource.PlexusResource;

import com.thoughtworks.xstream.XStream;

@Component( role = PlexusResource.class, hint = "ItemAttributesPlexusResource" )
public class ItemAttributesPlexusResource
    extends AbstractResourceStoreContentPlexusResource
{
    public ItemAttributesPlexusResource()
    {
        setReadable( true );
        setModifiable( true );
    }

    @Override
    public String getResourceUri()
    {
        return "/repositories/{" + AbstractRepositoryPlexusResource.REPOSITORY_ID_KEY + "}/attributes";
    }

    @Override
    public Attributes getPayloadInstance()
    {
        return new Attributes();
    }

    @Override
    public boolean acceptsUpload()
    {
        // we handle PUT method only
        return false;
    }

    @Override
    public PathProtectionDescriptor getResourceProtection()
    {
        return new PathProtectionDescriptor( "/repositories/*/attributes/**", "authcBasic" );
    }

    @Override
    public void configureXStream( XStream xstream )
    {
        super.configureXStream( xstream );
        xstream.processAnnotations( Attribute.class );
        xstream.processAnnotations( Attributes.class );
    }

    @Override
    protected Repository getResourceStore( final Request request )
        throws NoSuchRepositoryException
    {
        return getUnprotectedRepositoryRegistry().getRepository(
            request.getAttributes().get( AbstractRepositoryPlexusResource.REPOSITORY_ID_KEY ).toString() );
    }

    @Override
    public Object get( Context context, Request request, Response response, Variant variant )
        throws ResourceException
    {
        try
        {
            // create request
            final ResourceStoreRequest req = getResourceStoreRequest( request );
            // retrieve the item
            final StorageItem item = getResourceStore( request ).retrieveItem( req );
            // build and respond with attributes
            return Attributes.buildFrom( item.getRepositoryItemAttributes() );
        }
        catch ( Exception e )
        {
            handleException( request, response, e );
            return null;
        }
    }

    @Override
    public Object put( Context context, Request request, Response response, Object payload )
        throws ResourceException
    {
        // PUT has attributes as payload
        final Attributes attributes = (Attributes) payload;

        try
        {
            // create request
            final ResourceStoreRequest req = getResourceStoreRequest( request );
            // retrieve the item
            final StorageItem item = getResourceStore( request ).retrieveItem( req );
            // apply the payload attributes to item attributes
            attributes.applyTo( item.getRepositoryItemAttributes() );
            // use handler to persist/save the attributes
            getResourceStore( request ).getAttributesHandler().storeAttributes( item );
            // return the new state (rebuild from modified attributes)
            return Attributes.buildFrom( item.getRepositoryItemAttributes() );
        }
        catch ( Exception e )
        {
            handleException( request, response, e );
            return null;
        }
    }

    @Override
    public void delete( Context context, Request request, Response response )
        throws ResourceException
    {
        // override what parent does, for attributes, it is not allowed for now
        throw new ResourceException( Status.CLIENT_ERROR_METHOD_NOT_ALLOWED );
    }
}
