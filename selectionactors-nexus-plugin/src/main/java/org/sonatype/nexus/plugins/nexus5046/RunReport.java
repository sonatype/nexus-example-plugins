package org.sonatype.nexus.plugins.nexus5046;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias( "runReport" )
public class RunReport
{
    private final String repositoryId;

    private final String selectorId;

    private final int selectedCount;

    private final String actorId;

    private final int actedCount;

    private final boolean success;

    public RunReport( final String repositoryId, final String selectorId, final int selectedCount,
                      final String actorId, final int actedCount, final boolean success )
    {
        super();
        this.repositoryId = repositoryId;
        this.selectorId = selectorId;
        this.selectedCount = selectedCount;
        this.actorId = actorId;
        this.actedCount = actedCount;
        this.success = success;
    }

    protected String getRepositoryId()
    {
        return repositoryId;
    }

    protected String getSelectorId()
    {
        return selectorId;
    }

    protected int getSelectedCount()
    {
        return selectedCount;
    }

    protected String getActorId()
    {
        return actorId;
    }

    protected int getActedCount()
    {
        return actedCount;
    }

    protected boolean isSuccess()
    {
        return success;
    }
}
