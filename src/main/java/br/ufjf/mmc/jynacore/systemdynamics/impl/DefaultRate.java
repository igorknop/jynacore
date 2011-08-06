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
import br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.Stock;

/**
 * @author Knop
 *
 */
public class DefaultRate implements Rate,
		InformationConsumer {

	private Map<String, InformationSource> informationSources;
	private Expression expression;
	private String name;
	private Stock fromLevel;
	private Stock toLevel;
	private Double value;

	public DefaultRate() {
		informationSources = new HashMap<String, InformationSource>();
	}

//	@Override
//	public void eval() throws Exception {
//		final String varstr = "R_A_T_E";
//		Object ovalue = null;
//		Interpreter interpreter = new Interpreter();
//		for (String key : informationSources.keySet()) {
//			InformationSource source = informationSources.get(key);
//			if (source instanceof EvaluatedValue) {
//				((EvaluatedValue) source).eval();
//			}
//			interpreter.set(key, source.getValue());
//		}
//		interpreter.eval(varstr + " = " + expression + ";");
//		ovalue = interpreter.get(varstr);
//		this.value = (Double) ovalue;
//	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer#addInformation(java.lang.String, br.ufjf.mmc.jynacore.systemdynamics.InformationSource)
	 */
	@Override
	public void addInformation(String key, InformationSource informationSource) {
		informationSources.put(key, informationSource);

	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer#getInformations()
	 */
	@Override
	public Map<String, InformationSource> getInformations() {
		return informationSources;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer#removeInformation(java.lang.String)
	 */
	@Override
	public void removeInformation(String key) {
		informationSources.remove(key);
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer#removeInformation(br.ufjf.mmc.jynacore.systemdynamics.InformationSource)
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
/*		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append("[");
		sb.append((getFromLevel()==null?"empty?":getFromLevel().getName()));
		sb.append("=(");
		sb.append(getExpression());
		sb.append(")=>");
		sb.append((getToLevel()==null?"empty?":getToLevel().getName()));
		return sb.toString();
*/	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.Rate#getToLevel()
	 */
	public Stock getToLevel() {
		return toLevel;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.Rate#getFromLevel()
	 */
	public Stock getFromLevel() {
		return fromLevel;
	}


	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaEvaluated#getExpression()
	 */
	@Override
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaEvaluated#setExpression(br.ufjf.mmc.jynacore.expression.Expression)
	 */
	@Override
	public void setExpression(Expression newExpression) {
		expression = newExpression;
		
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.Rate#setFromLevel(br.ufjf.mmc.jynacore.systemdynamics.Stock)
	 */
	@Override
	public void setSource(Stock fromLevel) {
		this.fromLevel = fromLevel;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.Rate#setLevel(br.ufjf.mmc.jynacore.systemdynamics.Stock, br.ufjf.mmc.jynacore.systemdynamics.Stock)
	 */
	@Override
	public void setSourceAndTarget(Stock fromLevel, Stock toLevel) {
		this.fromLevel = fromLevel;
		this.toLevel = toLevel;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.systemdynamics.Rate#setToLevel(br.ufjf.mmc.jynacore.systemdynamics.Stock)
	 */
	@Override
	public void setTarget(Stock toLevel) {
		this.toLevel = toLevel;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaValued#getValue()
	 */
	@Override
	public Double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see br.ufjf.mmc.jynacore.JynaValued#setValue(java.lang.Double)
	 */
	@Override
	public void setValue(Double newValue) {
		this.value = newValue;
	}
	
}
