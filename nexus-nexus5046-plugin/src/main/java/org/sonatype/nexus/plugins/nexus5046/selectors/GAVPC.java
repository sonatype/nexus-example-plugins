package org.sonatype.nexus.plugins.nexus5046.selectors;

import java.util.Map;
import java.util.regex.Pattern;

import org.sonatype.nexus.proxy.maven.gav.Gav;

public class GAVPC
{
    private final Pattern g;

    private final Pattern a;

    private final Pattern v;

    private final Pattern bv;

    private final Pattern e;

    private final Pattern c;

    public GAVPC( final Map<String, String> terms )
    {
        this.g = compileIfExists( "g", terms );
        this.a = compileIfExists( "a", terms );
        this.v = compileIfExists( "v", terms );
        this.bv = compileIfExists( "bv", terms );
        this.e = compileIfExists( "e", terms );
        this.c = compileIfExists( "c", terms );

        if ( g == null && a == null && v == null && bv == null && e == null && c == null )
        {
            throw new IllegalArgumentException( "No valid terms exists in map!" );
        }
    }

    protected Pattern compileIfExists( final String key, final Map<String, String> terms )
    {
        if ( terms.containsKey( key ) )
        {
            return Pattern.compile( terms.get( key ) );
        }
        else
        {
            return null;
        }
    }

    public boolean matches( final Gav gav )
    {
        if ( g != null && !g.matcher( gav.getGroupId() ).matches() )
        {
            return false;
        }
        if ( a != null && !a.matcher( gav.getArtifactId() ).matches() )
        {
            return false;
        }
        if ( v != null && !v.matcher( gav.getVersion() ).matches() )
        {
            return false;
        }
        if ( bv != null && !bv.matcher( gav.getBaseVersion() ).matches() )
        {
            return false;
        }
        if ( e != null && !e.matcher( gav.getExtension() ).matches() )
        {
            return false;
        }
        if ( c != null && !c.matcher( gav.getClassifier() ).matches() )
        {
            return false;
        }

        return true;
    }
}
