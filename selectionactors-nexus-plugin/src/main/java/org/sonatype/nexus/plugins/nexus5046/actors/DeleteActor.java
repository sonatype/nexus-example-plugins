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
import org.sonatype.nexus.proxy.storage.UnsupportedStorageOperationException;

/**
 * A simple "delete" actor, that deletes all the items in selection.
 * 
 * @author cstamas
 */
@Component( role = Actor.class, hint = "delete" )
public class DeleteActor
    implements Actor
{
    @Override
    public int perform( final Selection selection, final Map<String, String> terms )
        throws IOException
    {
        int acted = 0;
        try
        {
            for ( SelectionEntry entry : selection )
            {
                try
                {
                    doDelete( entry );
                    acted++;
                }
                catch ( ItemNotFoundException e )
                {
                    // neglect and continue
                }
            }
        }
        catch ( UnsupportedStorageOperationException e )
        {
            // repository storage does not allow? bail out
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

    protected void doDelete( final SelectionEntry entry )
        throws UnsupportedStorageOperationException, IllegalOperationException, ItemNotFoundException, IOException
    {
        final ResourceStoreRequest request = new ResourceStoreRequest( entry.getPath() );
        request.setRequestLocalOnly( true );
        entry.getRepository().deleteItem( false, request );
    }
}
