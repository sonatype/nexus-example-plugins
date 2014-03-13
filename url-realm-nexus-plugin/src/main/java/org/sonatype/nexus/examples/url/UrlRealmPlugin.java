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

package org.sonatype.nexus.examples.url;

import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.nexus.plugin.PluginIdentity;

import org.eclipse.sisu.EagerSingleton;
import org.jetbrains.annotations.NonNls;

/**
 * URL Realm plugin.
 */
@Named
@EagerSingleton
public class UrlRealmPlugin
    extends PluginIdentity
{
  /**
   * Prefix for ID-like things.
   */
  @NonNls
  public static final String ID_PREFIX = "url-realm";

  /**
   * Expected groupId for plugin artifact.
   */
  @NonNls
  public static final String GROUP_ID = "org.sonatype.nexus.examples";

  /**
   * Expected artifactId for plugin artifact.
   */
  @NonNls
  public static final String ARTIFACT_ID = ID_PREFIX + "-nexus-plugin";

  @Inject
  public UrlRealmPlugin() throws Exception {
    super(GROUP_ID, ARTIFACT_ID);
  }
}