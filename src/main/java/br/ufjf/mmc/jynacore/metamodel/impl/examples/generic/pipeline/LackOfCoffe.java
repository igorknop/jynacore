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
package br.ufjf.mmc.jynacore.metamodel.impl.examples.generic.pipeline;

import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelScenarioAffect;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelScenarioConnection;

/**
 * @author Knop
 *
 */
public class LackOfCoffe extends DefaultMetaModelScenario{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7923780202393277998L;

	public LackOfCoffe() {
		setName("Lack Of Coffe");
		setMetaModelName("PipeLine Project");
		MetaModelScenarioConnection connection = new DefaultMetaModelScenarioConnection();
		connection.setName("TheDeveloper");
		connection.setClassName("Developer");
		MetaModelClassProperty prop = new DefaultMetaModelClassProperty();
		prop.setName("lackOfCoffeFactor");
		prop.setDefaultValue(0.5);
		connection.getClassItems().put(prop.getName(), prop);
		MetaModelScenarioAffect affect = new DefaultMetaModelScenarioAffect();
		affect.setName("Productivity");
		DefaultExpression expr = new DefaultExpression(NumberOperator.TIMES);
		expr.setLeftOperand(new DefaultNameExpression("experience"));
		expr.setRightOperand(new DefaultNameExpression("lackOfCoffeFactor"));
		affect.setExpression(expr);
		connection.getAffectList().add(affect);
		put(connection.getName(),connection);
	}
}
