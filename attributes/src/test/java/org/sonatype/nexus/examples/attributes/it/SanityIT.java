package org.sonatype.nexus.examples.attributes.it;

import org.junit.Test;

/**
 * Ensures basic plugin sanity.
 */
public class SanityIT
    extends AttributesITSupport
{
    public SanityIT(final String nexusBundleCoordinates) {
        super(nexusBundleCoordinates);
    }

    @Test
    public void pluginIsLoaded() throws Exception {
        // TODO: Check that the plugin is actually loaded
    }
}
