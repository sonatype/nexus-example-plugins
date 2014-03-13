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

import java.io.IOException;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.apachehttpclient.Hc4Provider;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.nexus.proxy.repository.UsernamePasswordRemoteAuthenticationSettings;
import org.sonatype.nexus.proxy.storage.remote.DefaultRemoteStorageContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.eclipse.sisu.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Realm that performs HTTP GET authenticated with user entered credentials against a remote URL to perform authc.
 */
@Singleton
@Typed(Realm.class)
@Named(UrlRealm.NAME)
@Description("URL Realm")
public class UrlRealm
    extends AuthorizingRealm
{
  public static final String NAME = "URLRealm";

  private static final Logger logger = LoggerFactory.getLogger(UrlRealm.class);

  private final ApplicationConfiguration applicationConfiguration;

  private final Hc4Provider hc4Provider;

  private final String baseUrl;

  private final String defaultRole;

  @Inject
  public UrlRealm(final ApplicationConfiguration applicationConfiguration,
                  final Hc4Provider hc4Provider,
                  @Named("${nexus.urlrealm.baseUrl}") final String baseUrl,
                  @Named("${nexus.urlrealm.defaultRole}") final String defaultRole)
  {
    this.applicationConfiguration = checkNotNull(applicationConfiguration);
    this.hc4Provider = checkNotNull(hc4Provider);
    this.baseUrl = checkNotNull(baseUrl);
    this.defaultRole = checkNotNull(defaultRole);
    setName(NAME);
    setAuthenticationCachingEnabled(true);
    setAuthorizationCachingEnabled(true);
  }

  @Override
  public boolean supports(final AuthenticationToken token) {
    return (token instanceof UsernamePasswordToken);
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
      throws AuthenticationException
  {
    final UsernamePasswordToken upToken = (UsernamePasswordToken) token;

    // if the user can authenticate we are good to go
    if (authenticateViaUrl(upToken)) {
      return buildAuthenticationInfo(upToken);
    }
    else {
      throw new UnknownAccountException("User \"" + upToken.getUsername()
          + "\" cannot be authenticated against URL Realm.");
    }
  }

  private AuthenticationInfo buildAuthenticationInfo(final UsernamePasswordToken token) {
    return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
  }

  private boolean authenticateViaUrl(final UsernamePasswordToken usernamePasswordToken) {
    final DefaultRemoteStorageContext ctx = new DefaultRemoteStorageContext(
        applicationConfiguration.getGlobalRemoteStorageContext());
    ctx.setRemoteAuthenticationSettings(new UsernamePasswordRemoteAuthenticationSettings(
        usernamePasswordToken.getUsername(), new String(usernamePasswordToken.getPassword())));
    final HttpClient client = hc4Provider.createHttpClient(ctx);
    try {
      final HttpResponse response = client.execute(new HttpGet(baseUrl));

      try {
        logger.debug("URL Realm user \"{}\" validated against URL={} as {}", usernamePasswordToken.getUsername(), baseUrl,
            response.getStatusLine());
        final boolean success =
            response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 299;
        return success;
      }
      finally {
        HttpClientUtils.closeQuietly(response);
      }
    }
    catch (IOException e) {
      logger.info("URL Realm was unable to perform authentication", e);
      return false;
    }
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
    // only if authenticated with this realm too
    if (!principals.getRealmNames().contains(getName())) {
      return null;
    }
    // add the default role
    final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.addRole(defaultRole);
    return authorizationInfo;
  }
}
