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
package br.ufjf.mmc.jynacore.systemdynamics.impl;

import java.util.HashMap;
import java.util.Map;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;

public class DefaultVariable implements Variable {

	private Double value;
	private String name;
	private Map<String, InformationSource> informationSources;
	private Expression nExpression;

	/**
	 * Default Constructor
	 */
	public DefaultVariable() {
		informationSources = new HashMap<String, InformationSource>();
	}

	@Override
	public void setValue(Double newValue) {
		value = newValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void addInformation(String key, InformationSource informationSource) {
		informationSources.put(key, informationSource);

	}

	@Override
	public Map<String, InformationSource> getInformations() {
		return informationSources;
	}

	@Override
	public void removeInformation(String key) {
		informationSources.remove(key);
	}

	@Override
	public void removeInformation(InformationSource informationSource) {
		if (informationSources.containsValue(informationSource)) {
			for (String key : informationSources.keySet()) {
				if (informationSources.get(key).equals(informationSource)) {
					informationSources.remove(key);
				}
			}
		}

	}

//	@Override
//	public void eval() throws Exception {
//		final String varstr = "V_A_R";
//		Object ovalue = null;
//		Interpreter interpreter = new Interpreter();
//		for (String key : informationSources.keySet()) {
//			InformationSource source = informationSources.get(key);
//			if (source instanceof EvaluatedValue) {
//				((EvaluatedValue) source).eval();
//			}
//			interpreter.set(key, source.getValue());
//
//		}
//		interpreter.eval(varstr + " = " + expression + ";");
//		ovalue = interpreter.get(varstr);
//		this.value = (Double) ovalue;
//	}

//	@Override
//	public Double evaluate() throws Exception {
//		setValue((Double) nExpression.evaluate()); 
//		return getValue();
//	}

	@Override
	public Expression getExpression() {
		return nExpression;
	}

	@Override
	public void setExpression(Expression newExpression) {
		nExpression = newExpression;
		
	}
	@Override
	public String toString(){
		return getName();
	}
}
