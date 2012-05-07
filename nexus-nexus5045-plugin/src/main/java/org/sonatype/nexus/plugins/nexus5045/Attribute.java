package org.sonatype.nexus.plugins.nexus5045;

import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias( "attribute" )
public class Attribute
{
    private final String key;

    private final String value;

    public Attribute( String key, String value )
    {
        this.key = Preconditions.checkNotNull( key );
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    // ==

    @Override
    public String toString()
    {
        return "Attribute [key=" + key + ", value=" + value + "]";
    }
}
