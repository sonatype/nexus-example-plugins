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

package org.sonatype.nexus.examples.attributes.rest;

import org.sonatype.nexus.examples.attributes.model.AttributeDTO;
import org.sonatype.nexus.examples.attributes.model.AttributesDTO;
import org.sonatype.nexus.proxy.attributes.Attributes;
import org.sonatype.sisu.litmus.testsupport.TestSupport;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link ItemAttributesResource}.
 */
public class ItemAttributesResourceTest
    extends TestSupport
{
  @Test(expected = IllegalArgumentException.class)
  public void applyTo_canNotOverrideSystemAttribute() throws Exception {
    AttributesDTO attributes = new AttributesDTO();
    attributes.getAttributes().add(new AttributeDTO(ItemAttributesResource.SYSTEM_ATTR_PREFIX + "foo", "bar"));
    Attributes target = mock(Attributes.class);

    ItemAttributesResource.applyTo(attributes, target);

    verifyNoMoreInteractions(target);
  }

  @Test
  public void applyTo_legalAttributeKey() throws Exception {
    AttributesDTO attributes = new AttributesDTO();
    attributes.getAttributes().add(new AttributeDTO("foo", "bar"));
    Attributes target = mock(Attributes.class);

    ItemAttributesResource.applyTo(attributes, target);

    verify(target).put("foo", "bar");
    verifyNoMoreInteractions(target);
  }
}
