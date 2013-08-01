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
import org.sonatype.nexus.proxy.item.StorageItem;

import org.codehaus.plexus.component.annotations.Component;

/**
 * A simple "tag" actor, that "tags" the selected items by setting the supplied attribute value for given attribute
 * key.
 *
 * @since 1.0
 */
@Component(role = Actor.class, hint = "tag")
public class TagActor
    implements Actor
{
  /**
   * The key for tag key term (mandatory).
   */
  public static final String TERM_ATTRIBUTE_KEY = "tagAttributeKey";

  /**
   * The key for tag value term, optional (mandatory).
   */
  public static final String TERM_ATTRIBUTE_VALUE = "tagAttributeValue";

  @Override
  public int perform(final Selection selection, final Map<String, String> terms)
      throws IOException
  {
    final String attributeKey = terms.get(TERM_ATTRIBUTE_KEY);
    if (attributeKey == null) {
      throw new IllegalArgumentException("Term " + TERM_ATTRIBUTE_KEY + " not found or is empty!");
    }
    final String attributeValue = terms.get(TERM_ATTRIBUTE_VALUE);
    if (attributeValue == null) {
      throw new IllegalArgumentException("Term " + TERM_ATTRIBUTE_VALUE + " not found or is empty!");
    }

    int acted = 0;
    try {
      for (SelectionEntry entry : selection) {
        try {
          doMark(entry, attributeKey, attributeValue);
          acted++;
        }
        catch (ItemNotFoundException e) {
          // neglect and continue
        }
      }
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

  protected void doMark(final SelectionEntry entry, final String key, final String value)
      throws IllegalOperationException, ItemNotFoundException, IOException
  {
    final ResourceStoreRequest request = new ResourceStoreRequest(entry.getPath());
    request.setRequestLocalOnly(true);
    final StorageItem item = entry.getRepository().retrieveItem(false, request);
    item.getRepositoryItemAttributes().put(key, value);
    item.getRepositoryItemUid().getRepository().getAttributesHandler().storeAttributes(item);
  }
}
