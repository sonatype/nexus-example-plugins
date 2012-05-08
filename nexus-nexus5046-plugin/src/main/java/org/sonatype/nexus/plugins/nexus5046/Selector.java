package org.sonatype.nexus.plugins.nexus5046;

import java.util.Map;

import org.sonatype.nexus.proxy.repository.Repository;

public interface Selector
{
    /**
     * Performs a selection against given repository with given terms.
     * 
     * @param repository
     * @param terms
     * @return
     */
    Selection select( Repository repository, Map<String, String> terms );
}
