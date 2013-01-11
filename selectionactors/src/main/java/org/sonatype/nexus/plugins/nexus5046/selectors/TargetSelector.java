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
package org.sonatype.nexus.plugins.nexus5046.selectors;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.plugins.nexus5046.Selector;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.target.Target;
import org.sonatype.nexus.proxy.target.TargetRegistry;

@Component( role = Selector.class, hint = "target" )
public class TargetSelector
    extends AbstractWalkingSelector
{
    /**
     * The key for target ID term.
     */
    public static final String TERM_TARGET_ID = "targetId";

    @Requirement
    private TargetRegistry targetRegistry;

    @Override
    public Selection select( final Repository repository, final Map<String, String> terms )
    {
        final String targetId = terms.get( TERM_TARGET_ID );
        if ( targetId == null )
        {
            throw new IllegalArgumentException( "Term " + TERM_TARGET_ID + " not found or is empty!" );
        }

        final Target target = targetRegistry.getRepositoryTarget( targetId );
        if ( target == null )
        {
            throw new IllegalArgumentException( "Target with ID=\"" + targetId + "\" not found!" );
        }

        final SelectionCollector collector = getSelectionFactory().getCollector();
        final TargetWalkerProcessor twp = new TargetWalkerProcessor( collector, target );
        walk( repository, new ResourceStoreRequest( "/" ), twp );
        return collector.done();
    }
}
