package org.sonatype.nexus.plugins.nexus5046.selectors;

import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.logging.AbstractLoggingComponent;
import org.sonatype.nexus.plugins.nexus5046.SelectionFactory;
import org.sonatype.nexus.plugins.nexus5046.Selector;

public abstract class AbstractSelector
    extends AbstractLoggingComponent
    implements Selector
{
    @Requirement
    private SelectionFactory selectionFactory;

    protected SelectionFactory getSelectionFactory()
    {
        return selectionFactory;
    }
}
