package org.sonatype.nexus.plugins.nexus5046;

import org.sonatype.nexus.proxy.repository.Repository;

public interface SelectionEntry
{
    Repository getRepository();

    String getPath();
}
