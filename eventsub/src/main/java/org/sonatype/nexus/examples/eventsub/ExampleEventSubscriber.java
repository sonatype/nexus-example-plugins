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

package org.sonatype.nexus.examples.eventsub;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.events.EventSubscriber;
import org.sonatype.nexus.plugins.mavenbridge.NexusAether;
import org.sonatype.nexus.proxy.events.RepositoryItemEventStoreCreate;
import org.sonatype.nexus.proxy.registry.RepositoryRegistry;
import org.sonatype.sisu.goodies.common.ComponentSupport;

import com.google.common.eventbus.Subscribe;

import static com.google.common.base.Preconditions.checkNotNull;

@Named
@Singleton
public class ExampleEventSubscriber
    extends ComponentSupport
    implements EventSubscriber
{
  private final NexusAether nexusAether;
  private final RepositoryRegistry repositoryRegistry;

  @Inject
  public ExampleEventSubscriber(final NexusAether nexusAether, final RepositoryRegistry repositoryRegistry) {
    log.info("Initializing ExampleEventSubscriber...");
    this.nexusAether = checkNotNull(nexusAether);
    this.repositoryRegistry = checkNotNull(repositoryRegistry);
  }

  @Subscribe
  public void onDeployEvent(RepositoryItemEventStoreCreate event) {
    log.info("Notifying ExampleEventSubscriber...");
  }
}
