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
package br.ufjf.mmc.jynacore.systemdynamics.impl.examples;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInformation;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;

public class ExponencialGrowth extends DefaultSystemDynamicsModel {
	private static final String GROWTH_RATE = "GrowthRate";
	private static final String POPULATON_SOURCE = "PopulatonSource";
	private static final String CURRENT_FACTOR = "CurrentFactor";
	private static final String CURRENT_POPULATION = "CurrentPopulation";
	private static final double GROWTH_FACTOR_VALUE = 0.5;
	private static final String GROWTH_FACTOR = "GrowthFactor";
	private static final double POPULATION_INITIAL_VALUE = 1.0;
	private static final String POPULATION = "Population";

	public ExponencialGrowth() {
		super();

		InfiniteStock popSource = new DefaultInfiniteStock();
		popSource.setName(POPULATON_SOURCE);
		
		FiniteStock population = new DefaultFiniteStock();
		population.setName(POPULATION);
		//population.setInitialValue(POPULATION_INITIAL_VALUE);
		population.setInitialValue(new DefaultNumberConstantExpression(POPULATION_INITIAL_VALUE));

		Variable growthFactor = new DefaultVariable();
		growthFactor.setName(GROWTH_FACTOR);
//		growthFactor.setExpression(Double.toString(GROWTH_FACTOR_VALUE));
		growthFactor.setExpression(new DefaultNumberConstantExpression(GROWTH_FACTOR_VALUE));
		
		Rate growthRate = new DefaultRate();
		growthRate.setName(GROWTH_RATE);
		growthRate.setSourceAndTarget(popSource, population);
//		growthRate.setExpression(GROWTH_FACTOR+"*"+POPULATION);
		Expression exprTimes = new DefaultExpression(NumberOperator.TIMES);
		exprTimes.setLeftOperand(new DefaultNameExpression(GROWTH_FACTOR));
		exprTimes.setRightOperand(new DefaultNameExpression(POPULATION));
		growthRate.setExpression(exprTimes);

		Information currentPopulation = new DefaultInformation(population,growthRate);
		currentPopulation.setName(CURRENT_POPULATION);
		Information currentFactor = new DefaultInformation(growthFactor,growthRate);
		currentPopulation.setName(CURRENT_FACTOR);
		
		put(POPULATION, population);
		put(POPULATON_SOURCE, popSource);
		put(GROWTH_RATE, growthRate);
		put(GROWTH_FACTOR, growthFactor);
		put(CURRENT_POPULATION, currentPopulation);
		put(CURRENT_FACTOR, currentFactor);


	}

}
