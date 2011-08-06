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
package br.ufjf.mmc.jynacore.systemdynamics;

import br.ufjf.mmc.jynacore.JynaEvaluated;

/**
 * A generic rate that connect two Levels
 * 
 * @author Igor Knop
 * 
 */
public interface Rate extends SystemDynamicsItem, InformationSource,
		InformationConsumer, JynaEvaluated {

	/**
	 * toLevel and fromLevel setter
	 * 
	 * @param fromLevel
	 *            level that will be decreased by rate
	 * @param toLevel
	 *            level that will be increased by rate
	 */
	void setSourceAndTarget(Stock fromLevel, Stock toLevel);

	/**
	 * fromLevel setter
	 * 
	 * @param fromLevel
	 *            level that will be decreased by rate
	 */
	void setSource(Stock fromLevel);

	/**
	 * fromLevel getter
	 * 
	 * @return level that will be decreased by rate
	 */
	Stock getFromLevel();

	/**
	 * toLevel setter
	 * 
	 * @param toLevel
	 *            level that will be increased by rate
	 */
	void setTarget(Stock toLevel);

	/**
	 * toLevel getter
	 * 
	 * @return level that will increased by rate
	 */
	Stock getToLevel();

}
