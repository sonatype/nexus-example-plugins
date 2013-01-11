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

import org.junit.Before;
import org.junit.Test;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ExampleVirusScanner}.
 */
public class ExampleVirusScannerTest
    extends TestSupport
{
    private ExampleVirusScanner underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ExampleVirusScanner();
    }

    @Test
    public void nonInfectedItem() throws Exception {
        StorageFileItem item = mock(StorageFileItem.class);
        when(item.getName()).thenReturn("some-clean-item");
        assertThat(underTest.hasVirus(item), is(false));
    }

    @Test
    public void infectedItem() throws Exception {
        StorageFileItem item = mock(StorageFileItem.class);
        when(item.getName()).thenReturn("some-infected-item");
        assertThat(underTest.hasVirus(item), is(true));
    }
}
