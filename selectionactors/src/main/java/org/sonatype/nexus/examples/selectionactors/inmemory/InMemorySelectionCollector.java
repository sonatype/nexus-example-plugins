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

import org.sonatype.nexus.examples.selectionactors.Selection;
import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.item.StorageItem;

/**
 * ???
 *
 * @since 1.0
 */
public class InMemorySelectionCollector
    implements SelectionCollector
{
  private final ArrayList<RepositoryItemUid> selectedUids;

  public InMemorySelectionCollector() {
    this.selectedUids = new ArrayList<RepositoryItemUid>();
  }

  @Override
  public void add(final StorageItem item) {
    // duplicate? order? etc
    final RepositoryItemUid uid = item.getRepositoryItemUid();
    selectedUids.add(uid);
  }

  @Override
  public void remove(final StorageItem item) {
    final RepositoryItemUid uid = item.getRepositoryItemUid();
    selectedUids.remove(uid);
  }

  @Override
  public Selection done() {
    try {
      return new InMemorySelection(selectedUids);
    }
    finally {
      selectedUids.clear();
    }
  }
}
