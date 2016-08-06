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

package com.comze_instancelabs.minigamesapi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

/**
 * Checks for updates by querying a nexus updater.
 * 
 * @author mepeisen
 */
public class UpdaterNexus implements Runnable
{
    
    /** the plugin to be checked. */
    private final Plugin plugin;
    
    /** the file to be checked. */
    private final File   file;
    
    /** the asynchronous update thread. */
    private final Thread thread;
    
    /** the url to be queried for updates. */
    private final String queryUrl;
    
    /** the queried group id. */
    private final String groupId;
    
    /**
     * Constructor
     * 
     * @param plugin
     *            the plugin that will be checked.
     * @param file
     *            The file that the plugin is running from, get this by doing this.getFile() from within your main class.
     */
    public UpdaterNexus(final Plugin plugin, final File file)
    {
        this(plugin, file, "http://nexus.xworlds.eu/minigames/query", "com.github.MCE-Plugins"); //$NON-NLS-1$//$NON-NLS-2$
    }
    
    /**
     * Constructor
     * 
     * @param plugin
     *            the plugin that will be checked.
     * @param file
     *            The file that the plugin is running from, get this by doing this.getFile() from within your main class.
     * @param queryUrl
     *            the url to be queried
     * @param groupId
     *            the queried group id
     */
    private UpdaterNexus(final Plugin plugin, final File file, final String queryUrl, String groupId)
    {
        this.plugin = plugin;
        this.file = file;
        this.queryUrl = queryUrl;
        this.groupId = groupId;
        
        this.thread = new Thread(this);
        this.thread.start();
    }
    
    @Override
    public void run()
    {
        final String version = this.plugin.getDescription().getVersion().split("-")[0]; //$NON-NLS-1$
        
        try
        {
            final URL url = new URL(
                    this.queryUrl + "?group=" + this.groupId + "&artifact=" + this.plugin.getName() + "&version=" + version + "&server=" + MinigamesAPI.SERVER_VERSION.mavenVersionString()); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();
            
            final JSONObject array = (JSONObject) JSONValue.parse(response);
            if (array.size() == 0)
            {
                this.plugin.getLogger().log(Level.WARNING, "Invalid response while checking plugin version"); //$NON-NLS-1$
            }
            if ((Boolean)array.get("success")) //$NON-NLS-1$
            {
                final JSONObject data = (JSONObject) array.get("data"); //$NON-NLS-1$
                if (! ((Boolean) data.get("upToDate"))) //$NON-NLS-1$
                {
                    final JSONObject vobj = (JSONObject) data.get("version"); //$NON-NLS-1$
                    final String newversion = (String) vobj.get("version"); //$NON-NLS-1$
                    final String downloadurl = (String) vobj.get("url"); //$NON-NLS-1$
                    
                    this.plugin.getLogger().log(Level.WARNING, "We found a new version #" + newversion + ". You can download the new version at " + downloadurl); //$NON-NLS-1$ //$NON-NLS-2$
                    // TODO Auto-Updating
                }
            }
            else
            {
                this.plugin.getLogger().log(Level.WARNING, "Error while checking plugin version " + array.get("msg")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        catch (final IOException e)
        {
            this.plugin.getLogger().log(Level.WARNING, "Unable to check plugin version", e); //$NON-NLS-1$
        }
    }
    
}
