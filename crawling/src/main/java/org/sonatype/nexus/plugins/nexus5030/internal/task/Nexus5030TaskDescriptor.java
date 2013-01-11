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
package org.sonatype.nexus.plugins.nexus5030.internal.task;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.RepoOrGroupComboFormField;
import org.sonatype.nexus.formfields.StringTextFormField;
import org.sonatype.nexus.tasks.descriptors.AbstractScheduledTaskDescriptor;
import org.sonatype.nexus.tasks.descriptors.ScheduledTaskDescriptor;

@Component( role = ScheduledTaskDescriptor.class, hint = "Nexus5030", description = "NEXUS-5030 Task" )
public class Nexus5030TaskDescriptor
    extends AbstractScheduledTaskDescriptor
{
    public static final String ID = "Nexus5030Task";

    public static final String REPOSITORY_FIELD_ID = "repositoryId";

    public static final String REPOSITORY_PATH_FIELD_ID = "repositoryPath";

    private final RepoOrGroupComboFormField repoField = new RepoOrGroupComboFormField( REPOSITORY_FIELD_ID,
        RepoOrGroupComboFormField.DEFAULT_LABEL, "Type in the repository in which to run the task.",
        FormField.MANDATORY );

    private final StringTextFormField resourceStorePathField = new StringTextFormField( REPOSITORY_PATH_FIELD_ID,
        "Repository path",
        "Enter a repository path to run the task in recursively (ie. \"/\" for root or \"/org/apache\").",
        FormField.OPTIONAL );

    public String getId()
    {
        return ID;
    }

    public String getName()
    {
        return "NEXUS-5030 Task";
    }

    public List<FormField> formFields()
    {
        List<FormField> fields = new ArrayList<FormField>();

        fields.add( repoField );
        fields.add( resourceStorePathField );

        return fields;
    }
}
