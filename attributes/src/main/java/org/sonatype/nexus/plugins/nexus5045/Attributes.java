package org.sonatype.nexus.plugins.nexus5045;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A simple non-thread safe DTO for attributes.
 * 
 * @author cstamas
 */
@XStreamAlias( "attributes" )
public class Attributes
{
    /**
     * The actual list holding the payload.
     */
    private final List<Attribute> attributes;

    /**
     * A transient map for easier lookups.
     */
    private transient final Map<String, Attribute> attributesMap;

    public Attributes()
    {
        this.attributes = new ArrayList<Attribute>();
        this.attributesMap = new HashMap<String, Attribute>();
    }

    public Attribute addAttribute( final Attribute attribute )
    {
        final Attribute result = attributesMap.put( attribute.getKey(), attribute );
        if ( result != null )
        {
            attributes.remove( result );
        }
        attributes.add( attribute );
        return result;
    }

    public Attribute getAttribute( final String key )
    {
        return attributesMap.get( key );
    }

    public void clear()
    {
        attributes.clear();
        attributesMap.clear();
    }

    public void applyTo( org.sonatype.nexus.proxy.attributes.Attributes proxyAttributes )
        throws IllegalArgumentException
    {
        for ( Attribute attribute : attributes )
        {
            if ( attribute.getKey().startsWith( "storageItem-" ) )
            {
                // these would override "system" attributes
                throw new IllegalArgumentException( String.format(
                    "Attribute %s was about to override a system attribute!", attribute ) );
            }

            proxyAttributes.put( attribute.getKey(), attribute.getValue() );
        }
    }

    // ==

    public static Attributes buildFrom( org.sonatype.nexus.proxy.attributes.Attributes proxyAttributes )
    {
        final Map<String, String> attributesMap = proxyAttributes.asMap();
        final Attributes result = new Attributes();
        for ( Map.Entry<String, String> entry : attributesMap.entrySet() )
        {
            final Attribute attribute = new Attribute( entry.getKey(), entry.getValue() );
            result.addAttribute( attribute );
        }
        return result;
    }
}
