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
package org.sonatype.nexus.examples.attributes.it;

import org.junit.runners.Parameterized;
import org.sonatype.nexus.bundle.launcher.NexusBundleConfiguration;
import org.sonatype.nexus.testsuite.support.NexusRunningParametrizedITSupport;
import org.sonatype.nexus.testsuite.support.NexusStartAndStopStrategy;

import java.util.Collection;

import static org.sonatype.nexus.testsuite.support.NexusStartAndStopStrategy.Strategy.EACH_TEST;
import static org.sonatype.nexus.testsuite.support.ParametersLoaders.firstAvailableTestParameters;
import static org.sonatype.nexus.testsuite.support.ParametersLoaders.systemTestParameters;
import static org.sonatype.nexus.testsuite.support.ParametersLoaders.testParameters;
import static org.sonatype.sisu.goodies.common.Varargs.$;

/**
 * Support for attributes example plugin integration tests.
 */
@NexusStartAndStopStrategy(EACH_TEST)
public abstract class AttributesITSupport
    extends NexusRunningParametrizedITSupport
{
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return firstAvailableTestParameters(
            systemTestParameters(),
            testParameters(
                $("${it.nexus.bundle.groupId}:${it.nexus.bundle.artifactId}:zip:bundle")
            )
        ).load();
    }

    public AttributesITSupport(final String nexusBundleCoordinates) {
        super(nexusBundleCoordinates);
    }

    @Override
    protected NexusBundleConfiguration configureNexus(final NexusBundleConfiguration configuration) {
        // override the format of the nexus.log file
        configuration.setLogPattern("%d{HH:mm:ss.SSS} %-5level - %msg%n");

        // configure logging level of example plugins running in nexus
        configuration.setLogLevel("org.sonatype.nexus.examples", "DEBUG");

        // install the plugin we are testing
        configuration.addPlugins(
            artifactResolver().resolvePluginFromDependencyManagement(
                "org.sonatype.nexus.examples", "attributes-nexus-plugin"
            )
        );

        return configuration;
    }
}
