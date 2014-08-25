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
package org.sonatype.nexus.examples.attributes.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Attribute collection.
 *
 * @since 1.0
 */
@XStreamAlias("attributes")
public class AttributesDTO
    implements Iterable<AttributeDTO>
{
  private final List<AttributeDTO> attributes = Lists.newArrayList();

  public List<AttributeDTO> getAttributes() {
    return attributes;
  }

  @Override
  public Iterator<AttributeDTO> iterator() {
    return attributes.iterator();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "attributes=" + attributes +
        '}';
  }
}
