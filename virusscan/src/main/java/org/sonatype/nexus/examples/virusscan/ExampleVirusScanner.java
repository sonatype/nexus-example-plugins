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
package org.sonatype.nexus.examples.virusscan;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.sisu.goodies.common.ComponentSupport;

/**
 * Example {@link VirusScanner} implementation which simulates an infection for any item name containing the string
 * "infected".
 *
 * @since 1.0
 */
@Named
@Singleton
public class ExampleVirusScanner
    extends ComponentSupport
    implements VirusScanner
{
  public boolean hasVirus(final StorageFileItem item) {
    return item.getName().contains("infected");
  }
}
