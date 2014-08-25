/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.sonatype.nexus.examples.selectionactors.Selection;
import org.sonatype.nexus.examples.selectionactors.SelectionEntry;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;

/**
 * ???
 *
 * @since 1.0
 */
public class InMemorySelection
    implements Selection
{
  private final ArrayList<SelectionEntry> entries;

  public InMemorySelection(final Collection<RepositoryItemUid> entries) {
    this.entries = new ArrayList<SelectionEntry>(entries.size());
    for (RepositoryItemUid uid : entries) {
      this.entries.add(new SimpleSelectionEntry(uid));
    }
  }

  @Override
  public Iterator<SelectionEntry> iterator() {
    return entries.iterator();
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public void close() {
    // nothing particular
    entries.clear();
  }
}
