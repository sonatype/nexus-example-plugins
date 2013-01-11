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

package org.sonatype.nexus.examples.attributes.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Attribute key-value pair.
 *
 * @since 1.0
 */
@XStreamAlias("attribute")
public class AttributeDTO
{
    private final String key;

    private final String value;

    public AttributeDTO(final String key, final @Nullable String value) {
        this.key = checkNotNull(key);
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
