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
package br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject.simple;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelMultiRelation;

/**
 * @author Knop
 *
 */
public class SimpleSoftwareProject extends DefaultMetaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8863095003225378963L;
	private MetaModelClass developer;
	private MetaModelClass activity;
	private MetaModelMultiRelation team;

	/**
	 * 
	 */
	public SimpleSoftwareProject() {
		//Developer Class
		setName("Simple Software Project");
		developer = new DefaultMetaModelClass();
		developer.setName("Developer");
		MetaModelClassProperty property = new DefaultMetaModelClassProperty();
		property.setName("experience");
		property.setDefaultValue(1.0);
		developer.put(property.getName(),property);
		MetaModelClassAuxiliary proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("Productivity");
		proc.setExpression(new DefaultNameExpression("experience"));
		developer.put(proc.getName(), proc);
		developer.setMetaModel(this);
		put(developer);
		
		//Activity Class
		activity = new DefaultMetaModelClass();
		activity.setName("Activity");
		property = new DefaultMetaModelClassProperty();
		property.setName("duration");
		property.setDefaultValue(20.0);
		activity.put(property.getName(),property);
		MetaModelClassStock stock = new DefaultMetaModelClassStock();
		stock.setName("TimeToConclude");
		stock.setExpression(new DefaultNameExpression("duration"));
		activity.put(stock);
		MetaModelClassRate rate = new DefaultMetaModelClassRate();
		rate.setTarget(stock);// TimeToConclude above
		rate.setExpression(new DefaultNameExpression("Prod"));
		rate.setName("Work");
		activity.put(rate);
		proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("Prod");
		proc.setExpression(getProdExpression());
		activity.put(proc);
		put(activity);
		activity.setMetaModel(this);

		
		//Team relation
		team = new DefaultMetaModelMultiRelation();
		team.setName("Team");
		team.setSource(activity);
		team.setTarget(developer);
		team.setMetaModel(this);
		put(team);

	}
	private Expression getProdExpression() {
		// "GROUPMAX (Team, Productivity)"
		Expression eGro = new DefaultExpression(NumberOperator.GROUPSUM);
		eGro.setLeftOperand(new DefaultNameExpression("Team"));
		eGro.setRightOperand(new DefaultNameExpression("Productivity"));		
		return eGro;
	}

}
