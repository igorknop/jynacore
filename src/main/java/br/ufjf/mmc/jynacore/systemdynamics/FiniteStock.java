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

import br.ufjf.mmc.jynacore.expression.Expression;

/**
 * System Dynamics Finite Stock An stock that holds an level value Models any
 * state of a system
 * 
 * @author Igor Knop
 * 
 */
public interface FiniteStock extends Stock, InformationSource {
	/**
	 * Set internal initial value
	 * 
	 * @param newValue
	 */
	void setInitialValue(Double newValue);

//	/**
//	 * Get actual value
//	 * 
//	 * @return internal initial value
//	 */
	//@Deprecated
	//Double getInitialValue();

	/**
	 * Get initial value expression
	 * 
	 * @return internal initial value
	 */
	Expression getInitialValue();

	/**
	 * Set initial value expression
	 * 
	 * @return internal initial value
	 */
	void setInitialValue(Expression newInitialValue);
}
