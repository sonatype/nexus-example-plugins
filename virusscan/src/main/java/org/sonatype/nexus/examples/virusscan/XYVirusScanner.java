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

import org.sonatype.nexus.logging.AbstractLoggingComponent;
import org.sonatype.nexus.proxy.item.StorageFileItem;

import javax.inject.Named;
import javax.inject.Singleton;

@Named("XY")
@Singleton
public class XYVirusScanner
    extends AbstractLoggingComponent
    implements VirusScanner
{
    public boolean hasVirus(final StorageFileItem file) {
        // DO THE JOB HERE
        getLogger().debug("Kung fu VirusScanner --- scanning for viruses on item: {}", file.getPath());

        // simulating virus hit by having the filename contain the "infected" string
        return file.getName().contains("infected");
    }
}
