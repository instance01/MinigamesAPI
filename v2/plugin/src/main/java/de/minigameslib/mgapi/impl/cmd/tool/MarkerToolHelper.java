/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mgapi.impl.cmd.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.GuiSessionInterface.SGuiMarkerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mgapi.impl.cmd.marker.MarkerColorInterface;

/**
 * @author mepeisen
 *
 */
public class MarkerToolHelper extends AnnotatedDataFragment
{
    
    /** object to colors. */
    private final Map<Object, MarkerColorInterface>              objectsToColors = new HashMap<>();
    
    /** colors to objects */
    private final Map<MarkerColorInterface, Object>              colorsToObjects = new HashMap<>();
    
    /** colors to markers */
    private final Map<MarkerColorInterface, SGuiMarkerInterface> colorsToMarkers = new HashMap<>();

    /** the underlying player */
    private McPlayerInterface player;
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(MarkerToolHelper.class.getName());
    
    /**
     * @param player
     */
    private MarkerToolHelper(McPlayerInterface player)
    {
        this.player = player;
    }

    /**
     * Returns helper tool for given player.
     * 
     * @param player
     * @return helper tool.
     */
    public static MarkerToolHelper instance(McPlayerInterface player)
    {
        MarkerToolHelper instance = player.getSessionStorage().get(MarkerToolHelper.class);
        if (instance == null)
        {
            instance = new MarkerToolHelper(player);
            player.getSessionStorage().set(MarkerToolHelper.class, instance);
        }
        return instance;
    }
    
    /**
     * Clears all markers
     */
    public void clearMarkers()
    {
        this.colorsToMarkers.values().forEach(t -> {
            try
            {
                t.remove();
            }
            catch (McException e)
            {
                LOGGER.log(Level.WARNING, "Error while removing marker", e); //$NON-NLS-1$
            }
        });
        this.colorsToMarkers.clear();
        this.colorsToObjects.clear();
        this.objectsToColors.clear();
    }
    
    /**
     * Creates a new marker for given sign and color
     * @param sign
     * @param color
     * @throws McException 
     */
    public void createMarker(SignInterface sign, MarkerColorInterface color) throws McException
    {
        this.clearMarker(sign);
        this.clearMarker(color);
        final SignIdInterface id = sign.getSignId();
        final SGuiMarkerInterface marker = this.player.openSmartGui().sguiShowMarker(sign, color.getR(), color.getG(), color.getB(), color.getAlpha(), color.getTitle());
        this.colorsToMarkers.put(color, marker);
        this.colorsToObjects.put(color, id);
        this.objectsToColors.put(id, color);
    }
    
    /**
     * Creates a new marker for given zone and color
     * @param zone
     * @param color
     * @throws McException 
     */
    public void createMarker(ZoneInterface zone, MarkerColorInterface color) throws McException
    {
        this.clearMarker(zone);
        this.clearMarker(color);
        final ZoneIdInterface id = zone.getZoneId();
        final SGuiMarkerInterface marker = this.player.openSmartGui().sguiShowMarker(zone, color.getR(), color.getG(), color.getB(), color.getAlpha(), color.getTitle());
        this.colorsToMarkers.put(color, marker);
        this.colorsToObjects.put(color, id);
        this.objectsToColors.put(id, color);
    }
    
    /**
     * Creates a new marker for given component and color
     * @param component
     * @param color
     * @throws McException 
     */
    public void createMarker(ComponentInterface component, MarkerColorInterface color) throws McException
    {
        this.clearMarker(component);
        this.clearMarker(color);
        final ComponentIdInterface id = component.getComponentId();
        final SGuiMarkerInterface marker = this.player.openSmartGui().sguiShowMarker(component, color.getR(), color.getG(), color.getB(), color.getAlpha(), color.getTitle());
        this.colorsToMarkers.put(color, marker);
        this.colorsToObjects.put(color, id);
        this.objectsToColors.put(id, color);
    }
    
    /**
     * Returns marker color for given sign
     * @param sign
     * @return color or {@code null} if no marker is set
     */
    public MarkerColorInterface getColor(SignInterface sign)
    {
        return this.objectsToColors.get(sign.getSignId());
    }
    
    /**
     * Returns marker color for given zone
     * @param zone
     * @return color or {@code null} if no marker is set
     */
    public MarkerColorInterface getColor(ZoneInterface zone)
    {
        return this.objectsToColors.get(zone.getZoneId());
    }
    
    /**
     * Returns marker color for given component
     * @param component
     * @return color or {@code null} if no marker is set
     */
    public MarkerColorInterface getColor(ComponentInterface component)
    {
        return this.objectsToColors.get(component.getComponentId());
    }
    
    /**
     * Returns the object id for given color
     * @param color
     * @return object id or {@code null} if color is not used; instance of {@link SignIdInterface}, {@code ZoneIdInterface} or {@code ComponentIdInterface}
     */
    public Object getObjectId(MarkerColorInterface color)
    {
        return this.colorsToObjects.get(color);
    }
    
    /**
     * Clears marker for given color
     * @param color
     * @throws McException 
     */
    public void clearMarker(MarkerColorInterface color) throws McException
    {
        final SGuiMarkerInterface marker = this.colorsToMarkers.remove(color);
        if (marker != null)
        {
            marker.remove();
            this.objectsToColors.remove(this.colorsToObjects.remove(color));
        }
    }
    
    /**
     * Clears marker for given sign
     * @param sign
     * @throws McException 
     */
    public void clearMarker(SignInterface sign) throws McException
    {
        final MarkerColorInterface color = this.getColor(sign);
        if (color != null)
        {
            this.clearMarker(color);
        }
    }
    
    /**
     * Clears marker for given zone
     * @param zone
     * @throws McException 
     */
    public void clearMarker(ZoneInterface zone) throws McException
    {
        final MarkerColorInterface color = this.getColor(zone);
        if (color != null)
        {
            this.clearMarker(color);
        }
    }
    
    /**
     * Clears marker for given component
     * @param component
     * @throws McException 
     */
    public void clearMarker(ComponentInterface component) throws McException
    {
        final MarkerColorInterface color = this.getColor(component);
        if (color != null)
        {
            this.clearMarker(color);
        }
    }
    
}
