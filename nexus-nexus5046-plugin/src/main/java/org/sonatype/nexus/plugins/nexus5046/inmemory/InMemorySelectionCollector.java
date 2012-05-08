package org.sonatype.nexus.plugins.nexus5046.inmemory;

import java.util.ArrayList;

import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.item.StorageItem;

public class InMemorySelectionCollector
    implements SelectionCollector
{
    private final ArrayList<RepositoryItemUid> selectedUids;

    public InMemorySelectionCollector()
    {
        this.selectedUids = new ArrayList<RepositoryItemUid>();
    }

    @Override
    public void add( final StorageItem item )
    {
        // duplicate? order? etc
        final RepositoryItemUid uid = item.getRepositoryItemUid();
        selectedUids.add( uid );
    }

    @Override
    public void remove( final StorageItem item )
    {
        final RepositoryItemUid uid = item.getRepositoryItemUid();
        selectedUids.remove( uid );
    }

    @Override
    public Selection done()
    {
        try
        {
            return new InMemorySelection( selectedUids );
        }
        finally
        {
            selectedUids.clear();
        }
    }
}
