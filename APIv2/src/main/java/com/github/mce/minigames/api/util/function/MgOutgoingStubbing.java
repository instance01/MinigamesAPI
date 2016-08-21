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

package com.github.mce.minigames.api.util.function;

import java.io.Serializable;

import com.github.mce.minigames.api.MinigameErrorCode;
import com.github.mce.minigames.api.MinigameException;

/**
 * A stubbing interface for outgoing answers.
 * 
 * @author mepeisen
 * @param <T>
 *            consumed object class
 */
public interface MgOutgoingStubbing<T>
{
    
    /**
     * Let the given consumer be invoked if the condition meets the criteria.
     * 
     * @param consumer
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> then(MgConsumer<T> consumer) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param consumer
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> thenThrow(MgFunction<T, MinigameException> consumer) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param code
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> thenThrow(MinigameErrorCode code) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition meets the criteria.
     * 
     * @param code
     * @param args
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> thenThrow(MinigameErrorCode code, MgFunction<T, Serializable[]> args) throws MinigameException;
    
    /**
     * Let the given consumer be invoked if the condition does not meet the criteria.
     * 
     * @param consumer
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> _else(MgConsumer<T> consumer) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param consumer
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> _elseThrow(MgFunction<T, MinigameException> consumer) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param code
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> _elseThrow(MinigameErrorCode code) throws MinigameException;
    
    /**
     * Let us throw an exception if the condition does not meet the criteria.
     * 
     * @param code
     * @param args
     * @return this object for chaining additional then or else consumers.
     * @throws MinigameException 
     */
    MgOutgoingStubbing<T> _elseThrow(MinigameErrorCode code, MgFunction<T, Serializable[]> args) throws MinigameException;
    
}
