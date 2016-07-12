/*
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

package com.comze_instancelabs.minigamesapi.spigottest;

import java.util.Arrays;

import org.bukkit.plugin.Plugin;

/**
 * @author mepeisen
 */
public class PluginMessage
{
    
    /**
     * the sender plugin.
     */
    private Plugin plugin;
    /**
     * the message channel.
     */
    private String channel;
    /**
     * the data.
     */
    private byte[] data;
    /**
     * @param plugin
     * @param channel
     * @param data
     */
    public PluginMessage(Plugin plugin, String channel, byte[] data)
    {
        super();
        this.plugin = plugin;
        this.channel = channel;
        this.data = data;
    }
    /**
     * @return the plugin
     */
    public Plugin getPlugin()
    {
        return this.plugin;
    }
    /**
     * @return the channel
     */
    public String getChannel()
    {
        return this.channel;
    }
    /**
     * @return the data
     */
    public byte[] getData()
    {
        return this.data;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.channel == null) ? 0 : this.channel.hashCode());
        result = prime * result + Arrays.hashCode(this.data);
        result = prime * result + ((this.plugin == null) ? 0 : this.plugin.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PluginMessage other = (PluginMessage) obj;
        if (this.channel == null)
        {
            if (other.channel != null)
                return false;
        }
        else if (!this.channel.equals(other.channel))
            return false;
        if (!Arrays.equals(this.data, other.data))
            return false;
        if (this.plugin != other.plugin)
        {
            return false;
        }
        return true;
    }
    
}
