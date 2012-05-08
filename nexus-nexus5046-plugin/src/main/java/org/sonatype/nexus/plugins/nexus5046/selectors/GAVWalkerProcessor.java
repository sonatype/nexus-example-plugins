package org.sonatype.nexus.plugins.nexus5046.selectors;

import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

public class GAVWalkerProcessor
    extends AbstractWalkerProcessor
{
    private final MavenRepository mavenRepository;

    private final GAVPC gavpc;

    private final SelectionCollector selectionCollector;

    public GAVWalkerProcessor( final MavenRepository mavenRepository, final GAVPC gavpc,
                               final SelectionCollector selectionCollector )
    {
        this.mavenRepository = mavenRepository;
        this.gavpc = gavpc;
        this.selectionCollector = selectionCollector;
    }

    @Override
    public void processItem( final WalkerContext context, final StorageItem item )
        throws Exception
    {
        final Gav gav = mavenRepository.getGavCalculator().pathToGav( item.getRepositoryItemUid().getPath() );
        if ( gav != null && gavpc.matches( gav ) )
        {
            selectionCollector.add( item );
        }
    }

    public SelectionCollector getSelectionCollector()
    {
        return selectionCollector;
    }
}
