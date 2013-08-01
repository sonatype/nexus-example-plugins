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
import org.sonatype.sisu.goodies.eventbus.EventBus;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link VirusScannerRequestProcessor}.
 */
public class VirusScannerRequestProcessorTest
    extends TestSupport
{
  private VirusScannerRequestProcessor underTest;

  @Mock
  private EventBus eventBus;

  @Mock
  private VirusScanner scanner1;

  @Mock
  private VirusScanner scanner2;

  @Before
  public void setUp() throws Exception {
    underTest = new VirusScannerRequestProcessor(eventBus, Lists.newArrayList(scanner1, scanner2));
  }

  @Test
  public void allScannersConsulted() throws Exception {
    // first scanner does not detect
    when(scanner1.hasVirus(any(StorageFileItem.class))).thenReturn(false);

    // second scanner does
    when(scanner2.hasVirus(any(StorageFileItem.class))).thenReturn(true);

    // use a deep mock here, as the scanner needs to reach into the item to post and event
    StorageFileItem item = mock(StorageFileItem.class, RETURNS_DEEP_STUBS);

    // virus is detected (by one of the scanners)
    assertThat(underTest.hasVirus(item), is(true));

    // both scanners are called
    verify(scanner1).hasVirus(item);
    verify(scanner2).hasVirus(item);

    // event is fired
    verify(eventBus).post(any(InfectedItemFoundEvent.class));
  }
}
