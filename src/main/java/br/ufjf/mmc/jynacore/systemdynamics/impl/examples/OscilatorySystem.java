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

/**
 * @author Knop
 * 
 */
public class OscilatorySystem extends DefaultSystemDynamicsModel {

	public InfiniteStock XSource;
	public FiniteStock X;
	public InfiniteStock YSource;
	public FiniteStock Y;
	public Variable factor;
	public Variable desiredX;
	public Variable difference;
	public Rate dX;
	public Rate dY;

	/**
	 * 
	 */
	public OscilatorySystem() {
		super();

		XSource = new DefaultInfiniteStock();
		X = new DefaultFiniteStock();
		X.setName("X");
		//X.setInitialValue(1.0);
		X.setInitialValue(new DefaultNumberConstantExpression(1.0));

		YSource = new DefaultInfiniteStock();
		Y = new DefaultFiniteStock();
		Y.setName("Y");
		//Y.setInitialValue(0.0);
		Y.setInitialValue(new DefaultNumberConstantExpression(0.0));

		factor = new DefaultVariable();
		factor.setName("factor");
		// factor.setValue(0.00);
		//factor.setExpression("0.01");
		factor.setExpression(new DefaultNumberConstantExpression(0.01));

		desiredX = new DefaultVariable();
		desiredX.setName("desiredX");
		// desiredX.setValue(0.0);
		//desiredX.setExpression("0.1");
		desiredX.setExpression(new DefaultNumberConstantExpression(0.0));

		difference = new DefaultVariable();
		difference.setName("difference");
		//difference.setExpression("X-desiredX");
		Expression exprDiff = new DefaultExpression(NumberOperator.MINUS);
		exprDiff.setLeftOperand(new DefaultNameExpression("X"));
		exprDiff.setRightOperand(new DefaultNameExpression("desiredX"));
		difference.setExpression(exprDiff);

		dX = new DefaultRate();
		dX.setSourceAndTarget(XSource, X);
		//((EvaluatedValue) dX).setExpression("Y");
//		((EvaluatedValue) dX).setNExpression(new DefaultNameExpression("X"));
		dX.setExpression(new DefaultNameExpression("Y"));

		dY = new DefaultRate();
		dY.setSourceAndTarget(YSource, Y);
		//((EvaluatedValue) dY).setExpression("-factor*difference");
		Expression exprTimes = new DefaultExpression(NumberOperator.TIMES);
		Expression exprNeg = new DefaultExpression(NumberOperator.NEGATION);
		exprNeg.setMiddleOperand(new DefaultNameExpression("factor"));
		exprTimes.setLeftOperand(exprNeg);
		exprTimes.setRightOperand(new DefaultNameExpression("difference"));
		dY.setExpression(exprTimes);

		Information currentX = new DefaultInformation(X, difference);
		Information currentDesiredX = new DefaultInformation(desiredX,
				difference);
		Information currentY = new DefaultInformation(Y, dX);
		Information currentFactor = new DefaultInformation(factor, dY);
		Information currentDiff = new DefaultInformation(difference, dY);

		put("X", X);
		put("Y", Y);
		put("dX", dX);
		put("dY", dY);
		put("XSource", XSource);
		put("YSource", YSource);
		put("difference", difference);
		put("desiredX", desiredX);
		put("factor", factor);

		put("CurrentX", currentX);
		put("CurrentY", currentY);
		put("CurrentFactor", currentFactor);
		put("CurrentDesiredX", currentDesiredX);
		put("CurrentDifference", currentDiff);

	}

}
