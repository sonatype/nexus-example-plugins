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
package org.sonatype.nexus.examples.selectionactors.selectors;

import java.util.Map;
import java.util.regex.Pattern;

import org.sonatype.nexus.proxy.maven.gav.Gav;

/**
 * ???
 *
 * @since 1.0
 */
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
