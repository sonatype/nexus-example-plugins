/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
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
