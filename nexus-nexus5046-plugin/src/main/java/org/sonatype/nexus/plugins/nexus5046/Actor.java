package org.sonatype.nexus.plugins.nexus5046;

import java.io.IOException;
import java.util.Map;

public interface Actor
{
    int perform( Selection selection, Map<String, String> terms )
        throws IOException;
}
