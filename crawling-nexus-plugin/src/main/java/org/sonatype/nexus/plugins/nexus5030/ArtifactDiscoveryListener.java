package org.sonatype.nexus.plugins.nexus5030;

import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;

public interface ArtifactDiscoveryListener
{
    void beforeWalk( final MavenRepository mavenRepository );

    void onArtifactDiscovery( final MavenRepository mavenRepository, final Gav gav, final StorageItem item );

    void afterWalk( final MavenRepository mavenRepository );
}
