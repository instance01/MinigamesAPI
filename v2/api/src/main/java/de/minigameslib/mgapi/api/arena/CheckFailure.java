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

package de.minigameslib.mgapi.api.arena;

import java.io.Serializable;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * Single result of failure checks.
 */
public class CheckFailure
{
    
    /** the failure severity. */
    private final CheckSeverity severity;
    
    /** the check title. */
    private final Serializable title;
    
    /** the check details. */
    private final Serializable details;
    
    /**
     * Consturctor
     * @param severity 
     * @param title 
     * @param titleArgs 
     * @param details 
     * @param detailArgs 
     */
    public CheckFailure(CheckSeverity severity, LocalizedMessageInterface title, Serializable[] titleArgs, LocalizedMessageInterface details, Serializable... detailArgs)
    {
        this.severity = severity;
        this.title = title.toArg(titleArgs);
        this.details = details.toArg(detailArgs);
    }

    /**
     * @return the severity
     */
    public CheckSeverity getSeverity()
    {
        return this.severity;
    }

    /**
     * @return the title
     */
    public Serializable getTitle()
    {
        return this.title;
    }

    /**
     * @return the details
     */
    public Serializable getDetails()
    {
        return this.details;
    }
    
}