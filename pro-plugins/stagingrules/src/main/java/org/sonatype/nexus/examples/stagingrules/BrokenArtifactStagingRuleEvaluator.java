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
package org.sonatype.nexus.examples.stagingrules;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.sonatype.nexus.staging.rule.AbstractStagingRuleEvaluator;
import com.sonatype.nexus.staging.rule.AbstractStagingRuleWalkerProcessor;
import com.sonatype.nexus.staging.rule.RuleResult;

import org.sonatype.nexus.proxy.item.StorageCollectionItem;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.maven.gav.Gav;
import org.sonatype.nexus.proxy.maven.gav.GavCalculator;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.walker.WalkerContext;
import org.sonatype.nexus.proxy.walker.WalkerFilter;

/**
 * Checks if an artifacts path contains the word 'broken'.
 *
 * @since 1.0
 */
@Named(BrokenArtifactStagingRuleType.TYPE_ID)
@Singleton
public class BrokenArtifactStagingRuleEvaluator
    extends AbstractStagingRuleEvaluator
{
  private final WalkerFilter filter = new AllItemWalkerFilter();

 @Inject
  private GavCalculator gavCalculator;

  @Override
  protected WalkerFilter getWalkerFilter() {
    return filter;
  }

  @Override
  protected AbstractStagingRuleWalkerProcessor getWalkerProcessor(Repository repository,
                                                                  RuleResult ruleEvaluationResult)
  {
    return new BrokenArtifactStagingRuleWalkerProcessor(repository, ruleEvaluationResult);
  }

  private class BrokenArtifactStagingRuleWalkerProcessor
      extends AbstractStagingRuleWalkerProcessor
  {
    public BrokenArtifactStagingRuleWalkerProcessor(Repository repository, RuleResult ruleResult) {
      super(repository, ruleResult);
    }

    @Override
    protected void processFileItem(WalkerContext context, StorageFileItem item)
        throws Exception
    {
      if (item.getPath().contains("broken")) {
        getRuleResult().addFailure("<b>Broken Artifact:</b> '" + item.getPath() + ", the artifact is broken.");
      }
      else {
        getRuleResult().addSuccess(
            "Succeeded to validate artifact '" + item.getPath() + "' on repository '" + getRepository().getId() + "'.");
      }

    }
  }

  private class AllItemWalkerFilter
      implements WalkerFilter
  {

    public boolean shouldProcess(WalkerContext ctx, StorageItem item) {
      if (!(item instanceof StorageFileItem)) {
        return false;
      }

        Gav gav = gavCalculator.pathToGav( item.getPath() );

        if (gav != null && !gav.isHash() && !gav.isSignature()) {
            System.out.println("Group ID: " + gav.getGroupId() +
                    " Artifact Id : " + gav.getArtifactId() +
                    " Version: " + gav.getVersion() +
                    " Classifier: " + gav.getClassifier() +
                    " Extension: " + gav.getExtension());

            Map<String, String> attributesMap = item.getRepositoryItemAttributes().asMap();
            System.out.println("Deployed by: " + attributesMap.get("request.user"));
        }


      // NOTE: we could do the filtering here too
      // if ( item.getPath().contains( "broken" ) )
      // {
      // return true;
      // }

      return true;
    }

    public boolean shouldProcessRecursively(WalkerContext ctx, StorageCollectionItem coll) {
      return true;
    }
  }
}
