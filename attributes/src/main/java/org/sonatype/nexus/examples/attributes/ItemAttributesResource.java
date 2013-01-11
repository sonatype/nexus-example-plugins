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

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.sonatype.nexus.examples.attributes.model.AttributeDTO;
import org.sonatype.nexus.examples.attributes.model.AttributesDTO;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.attributes.Attributes;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.rest.AbstractResourceStoreContentPlexusResource;
import org.sonatype.nexus.rest.repositories.AbstractRepositoryPlexusResource;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;

import com.thoughtworks.xstream.XStream;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Item attributes REST resource.
 *
 * @since 1.0
 */
@Named
@Singleton
public class ItemAttributesResource
    extends AbstractResourceStoreContentPlexusResource
{
    public static final String SYSTEM_ATTR_PREFIX = "storageItem-";

    public ItemAttributesResource() {
        setReadable(true);
        setModifiable(true);
    }

    @Override
    public String getResourceUri() {
        return "/repositories/{" + AbstractRepositoryPlexusResource.REPOSITORY_ID_KEY + "}/attributes";
    }

    @Override
    public AttributesDTO getPayloadInstance() {
        return new AttributesDTO();
    }

    @Override
    public boolean acceptsUpload() {
        // we handle PUT method only
        return false;
    }

    @Override
    public PathProtectionDescriptor getResourceProtection() {
        return new PathProtectionDescriptor("/repositories/*/attributes/**", "authcBasic");
    }

    @Override
    public void configureXStream(XStream xstream) {
        super.configureXStream(xstream);
        xstream.processAnnotations(AttributeDTO.class);
        xstream.processAnnotations(AttributesDTO.class);
    }

    @Override
    protected Repository getResourceStore(final Request request)
        throws NoSuchRepositoryException
    {
        return getUnprotectedRepositoryRegistry().getRepository(
            request.getAttributes().get(AbstractRepositoryPlexusResource.REPOSITORY_ID_KEY).toString());
    }

    private void applyTo(final AttributesDTO attributes, final Attributes proxyAttributes) {
        for (AttributeDTO attribute : attributes) {
            String key = attribute.getKey();
            checkArgument(!key.startsWith(SYSTEM_ATTR_PREFIX), "Can not override system attribute: %s", key);
            proxyAttributes.put(key, attribute.getValue());
        }
    }

    private AttributesDTO buildFrom(final Attributes proxyAttributes) {
        Map<String, String> attributesMap = proxyAttributes.asMap();
        AttributesDTO result = new AttributesDTO();
        for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
            AttributeDTO attribute = new AttributeDTO(entry.getKey(), entry.getValue());
            result.getAttributes().add(attribute);
        }
        return result;
    }

    // FIXME: Replace with siesta + JAX-RS

    @Override
    public Object get(Context context, Request request, Response response, Variant variant)
        throws ResourceException
    {
        try {
            // create request
            final ResourceStoreRequest req = getResourceStoreRequest(request);

            // retrieve the item
            final StorageItem item = getResourceStore(request).retrieveItem(req);

            // build and respond with attributes
            return buildFrom(item.getRepositoryItemAttributes());
        }
        catch (Exception e) {
            handleException(request, response, e);
            return null;
        }
    }

    @Override
    public Object put(Context context, Request request, Response response, Object payload)
        throws ResourceException
    {
        // PUT has attributes as payload
        final AttributesDTO attributes = (AttributesDTO) payload;

        try {
            // create request
            final ResourceStoreRequest req = getResourceStoreRequest(request);

            // retrieve the item
            final StorageItem item = getResourceStore(request).retrieveItem(req);

            // apply the payload attributes to item attributes
            applyTo(attributes, item.getRepositoryItemAttributes());

            // use handler to persist/save the attributes
            getResourceStore(request).getAttributesHandler().storeAttributes(item);

            // return the new state (rebuild from modified attributes)
            return buildFrom(item.getRepositoryItemAttributes());
        }
        catch (Exception e) {
            handleException(request, response, e);
            return null;
        }
    }

    @Override
    public void delete(Context context, Request request, Response response)
        throws ResourceException
    {
        // override what parent does, for attributes, it is not allowed for now
        throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }
}
