package org.sonatype.nexus.plugins.nexus5046.inmemory;

import org.sonatype.nexus.plugins.nexus5046.SelectionEntry;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.repository.Repository;

import com.google.common.base.Preconditions;

public class SimpleSelectionEntry
    implements SelectionEntry
{
    private final RepositoryItemUid uid;

    public SimpleSelectionEntry( final RepositoryItemUid uid )
    {
        this.uid = Preconditions.checkNotNull( uid );
    }

    @Override
    public Repository getRepository()
    {
        return uid.getRepository();
    }

    @Override
    public String getPath()
    {
        return uid.getPath();
    }
}
