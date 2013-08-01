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

package org.sonatype.nexus.examples.selectionactors.actors;

import java.io.IOException;
import java.util.Map;

import org.sonatype.nexus.examples.selectionactors.Actor;
import org.sonatype.nexus.examples.selectionactors.Selection;
import org.sonatype.nexus.examples.selectionactors.SelectionEntry;
import org.sonatype.nexus.proxy.IllegalOperationException;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.storage.UnsupportedStorageOperationException;

import org.codehaus.plexus.component.annotations.Component;

/**
 * A simple "delete" actor, that deletes all the items in selection.
 *
 * @since 1.0
 */
@Component(role = Actor.class, hint = "delete")
public class DeleteActor
    implements Actor
{
  @Override
  public int perform(final Selection selection, final Map<String, String> terms)
      throws IOException
  {
    int acted = 0;
    try {
      for (SelectionEntry entry : selection) {
        try {
          doDelete(entry);
          acted++;
        }
        catch (ItemNotFoundException e) {
          // neglect and continue
        }
      }
    }
    catch (UnsupportedStorageOperationException e) {
      // repository storage does not allow? bail out
    }
    catch (IllegalOperationException e) {
      // repo does not allow this, is out of service or so? bail out
    }
    finally {
      selection.close();
    }

    return acted;
  }

  // ==

  protected void doDelete(final SelectionEntry entry)
      throws UnsupportedStorageOperationException, IllegalOperationException, ItemNotFoundException, IOException
  {
    final ResourceStoreRequest request = new ResourceStoreRequest(entry.getPath());
    request.setRequestLocalOnly(true);
    entry.getRepository().deleteItem(false, request);
  }
}
