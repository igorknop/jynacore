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
package br.ufjf.mmc.jynacore;

import java.util.Collection;
import java.util.List;

public interface JynaSimulationData {
	/**
	 * Add a {@link JynaValued} to the list of watched items
	 * 
	 * @param itemToWatch
	 */
	
	void add(JynaValued itemToWatch);

	/**
	 * Add a {@link JynaValued} to the list of watched items
	 * 
	 * @param itemToWatch
	 */
	
	void addAll(Collection<JynaValued> itemsToWatch);
	/**
	 * Remove a set of {@link JynaValued} to the list of watched items and erases
	 * all data stored.
	 * 
	 * @param itemsToWatch
	 */

	void remove(JynaValued itemToWatch);

	/**
	 * Remove all {@link JynaValued} from list of watched items and erases
	 * all data stored.
	 * 
	 */
	void removeAll();

	/**
	 * Register all watched {@link JynaValued}.
	 * 
	 * @param time
	 *            the time to associate current values
	 * @throws Exception
	 */
	void register(Double time) throws Exception;

	/**
	 * Return the value of an {@link JynaValued} in a given time
	 * 
	 * @param series
	 *            internal watched {@link JynaValued} index
	 * @param time
	 *            time of interest
	 * @return the value
	 */
	Number getValue(int series, int time);

	/**
	 * Total of watched {@link JynaValued}.
	 * @return
	 */
	Integer getWatchedCount();
	
	/**
	 * Total of registred times.
	 * @return
	 */
	Integer getWatchedSize();

	List<String> getWatchedNames();

	Number getTime(int time);

	void add(String string, JynaValued value);
	

	/**
	 * Clear all data
	 */
	void clearAll();

	
}
