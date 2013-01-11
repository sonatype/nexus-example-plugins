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

import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.nexus.proxy.AccessDeniedException;
import org.sonatype.nexus.proxy.IllegalOperationException;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.access.Action;
import org.sonatype.nexus.proxy.item.AbstractStorageItem;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.repository.ProxyRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.repository.RequestProcessor;
import org.sonatype.plexus.appevents.ApplicationEventMulticaster;

import static com.google.common.base.Preconditions.checkNotNull;

@Named
public class VirusScannerRequestProcessor
    implements RequestProcessor
{
    private ApplicationEventMulticaster applicationEventMulticaster;

    private VirusScanner virusScanner;

    @Inject
    public VirusScannerRequestProcessor(final ApplicationEventMulticaster applicationEventMulticaster,
                                        final @Named("XY") VirusScanner virusScanner)
    {
        this.applicationEventMulticaster = checkNotNull(applicationEventMulticaster);
        this.virusScanner = checkNotNull(virusScanner);
    }

    @Override
    public boolean process(final Repository repository, final ResourceStoreRequest request, final Action action) {
        // don't decide until have content
        return true;
    }

    @Override
    public boolean shouldProxy(final ProxyRepository repository, final ResourceStoreRequest request) {
        // don't decide until have content
        return true;
    }

    @Override
    public boolean shouldRetrieve(Repository repository, ResourceStoreRequest request, StorageItem item)
        throws IllegalOperationException, ItemNotFoundException, AccessDeniedException
    {
        // don't decide until have content
        return true;
    }


    @Override
    public boolean shouldCache(final ProxyRepository repository, final AbstractStorageItem item) {
        if (item instanceof StorageFileItem) {
            StorageFileItem file = (StorageFileItem) item;

            // do a virus scan
            boolean hasVirus = virusScanner.hasVirus(file);

            if (hasVirus) {
                applicationEventMulticaster.notifyEventListeners(
                    new InfectedItemFoundEvent(item.getRepositoryItemUid().getRepository(), file));
            }

            return !hasVirus;
        }
        else {
            return true;
        }
    }
}
