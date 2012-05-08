package org.sonatype.nexus.plugins.nexus5046.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionEntry;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;

public class InMemorySelection
    implements Selection
{
    private final ArrayList<SelectionEntry> entries;

    public InMemorySelection( final Collection<RepositoryItemUid> entries )
    {
        this.entries = new ArrayList<SelectionEntry>( entries.size() );
        for ( RepositoryItemUid uid : entries )
        {
            this.entries.add( new SimpleSelectionEntry( uid ) );
        }
    }

    @Override
    public Iterator<SelectionEntry> iterator()
    {
        return entries.iterator();
    }

    @Override
    public int size()
    {
        return entries.size();
    }

    @Override
    public void close()
    {
        // nothing particular
        entries.clear();
    }
}
