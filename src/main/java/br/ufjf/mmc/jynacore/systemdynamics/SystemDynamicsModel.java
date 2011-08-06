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
package br.ufjf.mmc.jynacore.systemdynamics;

import java.util.Map;
import java.util.Set;

import br.ufjf.mmc.jynacore.JynaSimulableModel;

/**
 * A System Dinamics Model
 * @author knop
 *
 */
public interface SystemDynamicsModel extends JynaSimulableModel{
	/**
	 * Add a System Dynamics Object to Model
	 * @param key internal object's name 
	 * @param sdObject the object
	 */
	void put(String key, SystemDynamicsItem sdItem);

	/**
	 * Add a System Dynamics Object to Model
	 * @param key internal object's name 
	 * @param sdObject the object
	 */
	void put(SystemDynamicsItem sdItem);
	
	/**
	 * Get all objects and theirs names in the Model
	 * @return an SystemDynamicsItem key map
	 */
	Map<String,SystemDynamicsItem> getItems();
	/**
	 * Set all objects and theirs names in the Model
	 * @return an SystemDynamicsItem key map
	 */
	 void setItems(Map<String,SystemDynamicsItem> newItems);
	
	/**
	 * Get an object from the model 
	 * @param key object's name
	 * @return the object
	 */
	SystemDynamicsItem get(String key);
	
	/**
	 * Return object name
	 * @param sdObject the object
	 * @return the object's name
	 */
	String getObjectByName(SystemDynamicsItem sdObject);
	
	/**
	 * Return finite levels
	 * @return
	 */
	Set<InfiniteStock> getInfiniteLevels();

	/**
	 * Return infinite levels
	 * @return
	 */
	Set<FiniteStock> getFiniteLevels();
	
	/**
	 * Return rates
	 * @return
	 */
	Set<Rate> getRates();

	Set<Information> getInformations();

	Set<Auxiliary> getAuxiliaries();
	
	String getName();
	
	void setName(String newName);
	
}
