package org.sonatype.nexus.plugins.nexus5046.inmemory;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.plugins.nexus5046.SelectionFactory;

@Component( role = SelectionFactory.class )
public class InMemorySelectionFactory
    implements SelectionFactory
{
    @Override
    public SelectionCollector getCollector()
    {
        return new InMemorySelectionCollector();
    }
}
