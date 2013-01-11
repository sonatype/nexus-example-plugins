package org.sonatype.nexus.plugins.nexus5046;

import org.sonatype.nexus.proxy.item.StorageItem;

public interface SelectionCollector
{
    void add( StorageItem item );

    void remove( StorageItem item );

    Selection done();
}
