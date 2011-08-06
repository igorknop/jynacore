/*******************************************************************************
 * Copyright (c) 2009 Igor Knop.
 *     This file is part of JynaCore.
 * 
 *     JynaCore is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JynaCore is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JynaCore.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * 
 */
package br.ufjf.mmc.jynacore;

/**
 * Basic behavior of an Double valued item that can be used to store data in
 * charts,datasets, etc.
 * 
 * @author Knop
 * 
 */
public interface JynaValued extends JynaItem  {
	/**
	 * Get the current value of item
	 * 
	 * @return the current state of item
	 */
	Double getValue();

	/**
	 * Set the current value of item
	 * 
	 * @param newValue
	 *            the new state the item will store
	 */
	void setValue(Double newValue);
}
