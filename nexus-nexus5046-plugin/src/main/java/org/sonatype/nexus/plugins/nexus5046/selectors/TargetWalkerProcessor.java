package org.sonatype.nexus.plugins.nexus5046.selectors;

import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.target.Target;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

public class TargetWalkerProcessor
    extends AbstractWalkerProcessor
{
    private final SelectionCollector selectionCollector;

    private final Target target;

    public TargetWalkerProcessor( final SelectionCollector selectionCollector, final Target target )
    {
        this.selectionCollector = selectionCollector;
        this.target = target;
    }

    @Override
    public void processItem( final WalkerContext context, final StorageItem item )
        throws Exception
    {
        if ( target.isPathContained( context.getRepository().getRepositoryContentClass(),
            item.getRepositoryItemUid().getPath() ) )
        {
            selectionCollector.add( item );
        }
    }
}
