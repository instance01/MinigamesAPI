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
 * Outgoing stub for true checks of minigames predicates.
 * @author mepeisen
 * @param <T> argument class
 */
public final class TrueStub<T> implements MgOutgoingStubbing<T>
{

    /**
     * stubbed element.
     */
    private final T elm;

    /**
     * Constructor to create the stub.
     * @param elm stubbed element.
     */
    public TrueStub(T elm)
    {
        this.elm = elm;
    }

    @Override
    public MgOutgoingStubbing<T> then(MgConsumer<T> consumer) throws MinigameException
    {
        consumer.accept(this.elm);
        return this;
    }

    @Override
    public MgOutgoingStubbing<T> thenThrow(MgFunction<T, MinigameException> consumer) throws MinigameException
    {
        throw consumer.apply(this.elm);
    }

    @Override
    public MgOutgoingStubbing<T> thenThrow(MinigameErrorCode code) throws MinigameException
    {
        throw new MinigameException(code);
    }

    @Override
    public MgOutgoingStubbing<T> thenThrow(MinigameErrorCode code, MgFunction<T, Serializable[]> args2) throws MinigameException
    {
        throw new MinigameException(code, args2.apply(this.elm));
    }

    @Override
    public MgOutgoingStubbing<T> _else(MgConsumer<T> consumer) throws MinigameException
    {
        // does nothing
        return this;
    }

    @Override
    public MgOutgoingStubbing<T> _elseThrow(MgFunction<T, MinigameException> consumer) throws MinigameException
    {
        // does nothing
        return this;
    }

    @Override
    public MgOutgoingStubbing<T> _elseThrow(MinigameErrorCode code) throws MinigameException
    {
        // does nothing
        return this;
    }

    @Override
    public MgOutgoingStubbing<T> _elseThrow(MinigameErrorCode code, MgFunction<T, Serializable[]> args2) throws MinigameException
    {
        // does nothing
        return this;
    }
    
}