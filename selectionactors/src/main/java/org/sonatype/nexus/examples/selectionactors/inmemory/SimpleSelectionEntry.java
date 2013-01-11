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
package org.sonatype.nexus.examples.selectionactors.inmemory;

import org.sonatype.nexus.examples.selectionactors.SelectionEntry;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.repository.Repository;

import com.google.common.base.Preconditions;

/**
 * ???
 *
 * @since 1.0
 */
public class SimpleSelectionEntry
    implements SelectionEntry
{
    private final RepositoryItemUid uid;

    public SimpleSelectionEntry( final RepositoryItemUid uid )
    {
        this.uid = Preconditions.checkNotNull( uid );
    }

    @Override
    public Repository getRepository()
    {
        return uid.getRepository();
    }

    @Override
    public String getPath()
    {
        return uid.getPath();
    }
}
