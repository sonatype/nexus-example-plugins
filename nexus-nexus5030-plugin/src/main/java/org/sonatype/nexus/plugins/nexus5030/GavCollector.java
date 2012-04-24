package org.sonatype.nexus.plugins.nexus5030;

import java.io.IOException;

import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.maven.MavenRepository;

public interface GavCollector
{
    void collectGAVs( ResourceStoreRequest request, MavenRepository mavenRepository, ArtifactDiscoveryListener listener )
        throws IOException;
}
