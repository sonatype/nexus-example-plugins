/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.nexus.examples.crawling.internal;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.codehaus.plexus.util.StringUtils;
import org.sonatype.nexus.plugins.nexus5030.ArtifactDiscoveryListener;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.utils.RepositoryStringUtils;

import com.google.common.base.Preconditions;

public class FileArtifactDiscoveryListener
    implements ArtifactDiscoveryListener
{
    private final File reportFile;

    private final PrintStream reportPrinter;

    private long started;

    private long itemsFound;

    private long artifactsFound;

    public FileArtifactDiscoveryListener( final File reportFile )
        throws IOException
    {
        this.reportFile = Preconditions.checkNotNull( reportFile );
        this.reportPrinter = new PrintStream( reportFile );
    }

    protected File getReportFile()
    {
        return reportFile;
    }

    @Override
    public void beforeWalk( final MavenRepository mavenRepository )
    {
        started = System.currentTimeMillis();
        println( "GAV Scan of repository %s", RepositoryStringUtils.getHumanizedNameString( mavenRepository ) );
        println( "Started at %s", new Date( started ) );
        println( "========" );
        println( "No.   Path (G:A:V:[C]:E)" );
        println( "========" );
        println( "" );
    }

    @Override
    public void onArtifactDiscovery( final MavenRepository mavenRepository, final Gav gav, final StorageItem item )
    {
        // update counters for stats
        itemsFound++;
        // write report
        if ( gav != null )
        {
            artifactsFound++;
            println( "%s. %s (%s)", itemsFound, item.getPath(), gavToString( gav ) );
        }
        else
        {
            println( "%s. %s", itemsFound, item.getPath() );
        }
    }

    @Override
    public void afterWalk( final MavenRepository mavenRepository )
    {
        println( "" );
        println( "========" );
        println( "GAV Scan of repository %s finished in %s seconds.",
            RepositoryStringUtils.getHumanizedNameString( mavenRepository ),
            ( System.currentTimeMillis() - started ) / 1000l );
        println( "Discovered total of %s items, out of which %s were Maven artifacts.", itemsFound, artifactsFound );
        reportPrinter.flush();
        reportPrinter.close();
    }

    // ==

    protected void println( final String format, final Object... args )
    {
        reportPrinter.println( String.format( format, args ) );
    }

    protected String gavToString( final Gav gav )
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( gav.getGroupId() ).append( ":" ).append( gav.getArtifactId() ).append( ":" ).append(
            gav.getVersion() );
        if ( !StringUtils.isBlank( gav.getClassifier() ) )
        {
            sb.append( ":" ).append( gav.getClassifier() );
        }
        sb.append( gav.getExtension() );
        return sb.toString();
    }

}
