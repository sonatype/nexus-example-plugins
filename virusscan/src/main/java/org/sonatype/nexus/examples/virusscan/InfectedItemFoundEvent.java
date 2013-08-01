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

package org.sonatype.nexus.examples.virusscan;

import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.plexus.appevents.AbstractEvent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event fired when a virus has been detected in an item.
 *
 * @since 1.0
 */
public class InfectedItemFoundEvent
    extends AbstractEvent<Repository>
{
  private final StorageFileItem item;

  public InfectedItemFoundEvent(final Repository repository, final StorageFileItem item) {
    super(repository);
    this.item = checkNotNull(item);
  }

  public Repository getRepository() {
    return getEventSender();
  }

  public StorageFileItem getItem() {
    return item;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "item=" + item +
        '}';
  }
}
