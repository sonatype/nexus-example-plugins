package org.sonatype.nexus.plugins.nexus5046.actors;

import java.io.IOException;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.plugins.nexus5046.Actor;
import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionEntry;
import org.sonatype.nexus.proxy.IllegalOperationException;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.item.StorageItem;

/**
 * A simple "tag" actor, that "tags" the selected items by setting the supplied attribute value for given attribute key.
 * 
 * @author cstamas
 */
@Component( role = Actor.class, hint = "tag" )
public class TagActor
    implements Actor
{
    /**
     * The key for tag key term (mandatory).
     */
    public static final String TERM_ATTRIBUTE_KEY = "tagAttributeKey";

    /**
     * The key for tag value term, optional (mandatory).
     */
    public static final String TERM_ATTRIBUTE_VALUE = "tagAttributeValue";

    @Override
    public int perform( final Selection selection, final Map<String, String> terms )
        throws IOException
    {
        final String attributeKey = terms.get( TERM_ATTRIBUTE_KEY );
        if ( attributeKey == null )
        {
            throw new IllegalArgumentException( "Term " + TERM_ATTRIBUTE_KEY + " not found or is empty!" );
        }
        final String attributeValue = terms.get( TERM_ATTRIBUTE_VALUE );
        if ( attributeValue == null )
        {
            throw new IllegalArgumentException( "Term " + TERM_ATTRIBUTE_VALUE + " not found or is empty!" );
        }

        int acted = 0;
        try
        {
            for ( SelectionEntry entry : selection )
            {
                try
                {
                    doMark( entry, attributeKey, attributeValue );
                    acted++;
                }
                catch ( ItemNotFoundException e )
                {
                    // neglect and continue
                }
            }
        }
        catch ( IllegalOperationException e )
        {
            // repo does not allow this, is out of service or so? bail out
        }
        finally
        {
            selection.close();
        }

        return acted;
    }

    // ==

    protected void doMark( final SelectionEntry entry, final String key, final String value )
        throws IllegalOperationException, ItemNotFoundException, IOException
    {
        final ResourceStoreRequest request = new ResourceStoreRequest( entry.getPath() );
        request.setRequestLocalOnly( true );
        final StorageItem item = entry.getRepository().retrieveItem( false, request );
        item.getRepositoryItemAttributes().put( key, value );
        item.getRepositoryItemUid().getRepository().getAttributesHandler().storeAttributes( item );
    }
}
