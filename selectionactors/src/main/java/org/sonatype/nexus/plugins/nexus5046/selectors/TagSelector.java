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
import org.sonatype.nexus.plugins.nexus5046.Selection;
import org.sonatype.nexus.plugins.nexus5046.SelectionCollector;
import org.sonatype.nexus.plugins.nexus5046.Selector;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;

@Component( role = Selector.class, hint = "target" )
public class TagSelector
    extends AbstractWalkingSelector
{
    /**
     * The key for tag key term (mandatory).
     */
    public static final String TERM_ATTRIBUTE_KEY = "attributeKey";

    /**
     * The key for tag value term, optional. If given, will check for value equality, otherwise will check only for the
     * presence of {@link #TERM_ATTRIBUTE_KEY}.
     */
    public static final String TERM_ATTRIBUTE_VALUE = "attributeValue";

    @Override
    public Selection select( Repository repository, Map<String, String> terms )
    {
        final String attributeKey = terms.get( TERM_ATTRIBUTE_KEY );
        if ( attributeKey == null )
        {
            throw new IllegalArgumentException( "Term " + TERM_ATTRIBUTE_KEY + " not found or is empty!" );
        }

        final String attributeValue = terms.get( TERM_ATTRIBUTE_VALUE );

        final SelectionCollector collector = getSelectionFactory().getCollector();
        final TagWalkerProcessor twp = new TagWalkerProcessor( collector, attributeKey, attributeValue );
        walk( repository, new ResourceStoreRequest( "/" ), twp );
        return collector.done();
    }
}
