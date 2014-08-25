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
package org.sonatype.nexus.examples.selectionactors.selectors;

import org.apache.commons.lang.StringUtils;
import org.sonatype.nexus.examples.selectionactors.SelectionCollector;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.walker.AbstractWalkerProcessor;
import org.sonatype.nexus.proxy.walker.WalkerContext;

/**
 * ???
 *
 * @since 1.0
 */
public class TagWalkerProcessor
    extends AbstractWalkerProcessor
{
  private final SelectionCollector selectionCollector;

  private final String attributeKey;

  private final String attributeValue;

  public TagWalkerProcessor(final SelectionCollector selectionCollector, final String attributeKey,
                            final String attributeValue)
  {
    this.selectionCollector = selectionCollector;
    this.attributeKey = attributeKey;
    this.attributeValue = attributeValue;
  }

  @Override
  public void processItem(final WalkerContext context, final StorageItem item)
      throws Exception
  {
    // check for key existence
    if (item.getRepositoryItemAttributes().containsKey(attributeKey)) {
      // if value given, check for attribute value equality too
      if (attributeValue != null
          && !StringUtils.equals(attributeValue, item.getRepositoryItemAttributes().get(attributeKey))) {
        return;
      }
      selectionCollector.add(item);
    }
  }
}
