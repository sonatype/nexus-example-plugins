package org.sonatype.nexus.plugins.nexus5046;

public interface Selection
    extends Iterable<SelectionEntry>
{
    int size();
    
    void close();
}
