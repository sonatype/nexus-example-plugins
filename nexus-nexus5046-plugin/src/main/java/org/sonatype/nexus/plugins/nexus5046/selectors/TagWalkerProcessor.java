package org.sonatype.nexus.plugins.nexus5046.selectors;

import org.codehaus.plexus.util.StringUtils;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

public class TagWalkerProcessor
    extends AbstractWalkerProcessor
{
    private final SelectionCollector selectionCollector;

    private final String attributeKey;

    private final String attributeValue;

    public TagWalkerProcessor( final SelectionCollector selectionCollector, final String attributeKey,
                               final String attributeValue )
    {
        this.selectionCollector = selectionCollector;
        this.attributeKey = attributeKey;
        this.attributeValue = attributeValue;
    }

    @Override
    public void processItem( final WalkerContext context, final StorageItem item )
        throws Exception
    {
        // check for key existence
        if ( item.getRepositoryItemAttributes().containsKey( attributeKey ) )
        {
            // if value given, check for attribute value equality too
            if ( attributeValue != null
                && !StringUtils.equals( attributeValue, item.getRepositoryItemAttributes().get( attributeKey ) ) )
            {
                return;
            }
            selectionCollector.add( item );
        }
    }
}
