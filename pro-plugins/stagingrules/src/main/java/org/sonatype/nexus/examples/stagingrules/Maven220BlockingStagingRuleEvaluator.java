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

package org.sonatype.nexus.examples.stagingrules;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.staging.StagingManager;
import com.sonatype.nexus.staging.persist.model.CCondition;
import com.sonatype.nexus.staging.persist.model.CStageRepository;
import com.sonatype.nexus.staging.rule.RuleResult;
import com.sonatype.nexus.staging.rule.StagingRule;
import com.sonatype.nexus.staging.rule.StagingRuleEvaluator;

import org.sonatype.nexus.logging.AbstractLoggingComponent;
import org.sonatype.nexus.proxy.NoSuchRepositoryException;

/**
 * Evaluates a staging repository to see if Maven 2.2.0 was used. Maven 2.2.0 generates incorrect signatures.
 *
 * @since 1.0
 */
@Named(BrokenArtifactStagingRuleType.TYPE_ID)
@Singleton
public class Maven220BlockingStagingRuleEvaluator
    extends ComponentSupport
    implements StagingRuleEvaluator
{
  private final StagingManager stagingManager;

  /**
   * The actual user-agent would be something like: Apache-Maven/2.2 (Java 1.6.0_16; Linux 2.6.26-2-amd64)
   * maven-artifact/2.2.0
   */
  private static String maven220UserAgentPart = "maven-artifact/2.2.0";

  @Inject
  public Maven220BlockingStagingRuleEvaluator(final StagingManager stagingManager) {
      this.stagingManager = Preconditions.checkNotNull(stagingManager);
  }

  public RuleResult evaluate(StagingRule stagingRule) {
    RuleResult result = new RuleResult(stagingRule);

    String stagingRepositoryId = stagingRule.getRepository().getId();
    CStageRepository stagingRepo;

    try {
      stagingRepo = this.getStagingRepository(stagingRepositoryId);
    }
    catch (NoSuchRepositoryException e) {
      // this should NEVER happen, we are running this rule against this repo
      getLogger().error("Error finding the staging profile while executing rule.", e);
      result.addFailure("<b>Invalid Staging Profile:</b> This staging profile could not be found.");
      return result; // guard
    }

    // again, this should never happen
    if (stagingRepo == null) {
      result.addFailure("<b>Invalid Staging Profile:</b> This staging repository could not be found.");
      return result; // guard
    }

    // check if Maven 2.2.0 is used
    boolean maven220Used = false;
    for (CCondition condition : stagingRepo.getConditions()) {
      if (condition.getType().equals(CCondition.TYPE_USER_AGENT)) {
        if (condition.getValue().contains(maven220UserAgentPart)) {
          maven220Used = true;
        }
      }
    }

    if (maven220Used) {
      result.addFailure("<b>Invalid Maven Version:</b> Do not use Maven 2.2.0 signatures are calculated incorrectly.");
    }
    else {
      result.addSuccess("<b>Success:</b> Maven 2.2.0 is not use");
    }

    return result;
  }

  private CStageRepository getStagingRepository(String stagingRepositoryId)
      throws NoSuchRepositoryException
  {
    return stagingManager.getStageRepositoryById(stagingRepositoryId);
  }
}
