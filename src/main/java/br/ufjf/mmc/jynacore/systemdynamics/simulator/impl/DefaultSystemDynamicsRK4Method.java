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
package br.ufjf.mmc.jynacore.systemdynamics.simulator.impl;

import java.util.HashMap;
import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluatorParametrized;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultOperatorEvaluatorWithParameters;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.systemdynamics.Auxiliary;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;

/**
 * @author Knop
 * 
 */
public class DefaultSystemDynamicsRK4Method implements
		SystemDynamicsSimulationMethod {

	private Double initialTime;
	private SystemDynamicsModel model;
	private Double stepSize;
	private Double currentTime;
	private HashMap<String, Rate> rates;
	private HashMap<String, FiniteStock> finiteLevels;
	private HashMap<String, Auxiliary> auxiliaries;
	private int currentStep;
	private Variable _TIME_;
	private Variable _TIME_STEP_;

	public DefaultSystemDynamicsRK4Method() {
		this.rates = new HashMap<String, Rate>();
		this.finiteLevels = new HashMap<String, FiniteStock>();
		this.auxiliaries = new HashMap<String, Auxiliary>();
		currentStep = 0;
		currentTime = 0.0;
		_TIME_ = new DefaultVariable();
		_TIME_.setName("_TIME_");
		_TIME_.setExpression(new DefaultNumberConstantExpression(currentTime));
		_TIME_STEP_ = new DefaultVariable();
		_TIME_STEP_.setName("_TIME_STEP_");
		_TIME_STEP_.setExpression(new DefaultNumberConstantExpression(stepSize));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #getInitialTime()
	 */
	@Override
	public Double getInitialTime() {
		return initialTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #getModel()
	 */
	@Override
	public SystemDynamicsModel getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #getStepSize()
	 */
	@Override
	public Double getStepSize() {
		return stepSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #getTime()
	 */
	@Override
	public Double getTime() {
		return currentTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #reset()
	 */
	@Override
	public void reset() throws Exception {
		for (Entry<String, FiniteStock> entry : finiteLevels.entrySet()) {
			entry.getValue().setValue(
					(Double) entry.getValue().getInitialValue().evaluate());
		}
		for (Entry<String, Rate> entry : rates.entrySet()) {
			entry.getValue().setValue(0.0);
		}
		for (Entry<String, Auxiliary> entry : auxiliaries.entrySet()) {
				 Auxiliary aux = entry.getValue();
				 if (aux instanceof Variable) {
					 Variable v = (Variable)aux;
					 v.setValue((Double) v.getExpression().evaluate());
				 }
		}
		currentStep = 0;
		currentTime = initialTime;
		_TIME_.getExpression().setValue(currentTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #setInitialTime(java.lang.Double)
	 */
	@Override
	public void setInitialTime(Double newInitialTime) {
		initialTime = newInitialTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #setModel(br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel)
	 */
	@Override
	public void setModel(SystemDynamicsModel model) throws Exception {
		this.model = model;
		this.rates = new HashMap<String, Rate>();
		this.finiteLevels = new HashMap<String, FiniteStock>();
		for (String key : model.getItems().keySet()) {
			if (model.get(key) instanceof Rate) {
				Rate rate = (Rate) model.get(key);
				rates.put(key, rate);
			}
			if (model.get(key) instanceof FiniteStock) {
				FiniteStock flevel = (FiniteStock) model.get(key);
				finiteLevels.put(key, flevel);
			}
			if (model.get(key) instanceof Auxiliary) {
				Auxiliary faux = (Auxiliary) model.get(key);
				auxiliaries.put(key, faux);
			}
		}
		setupReferences();
	}

	private void setupReferences() {
		for (FiniteStock o : model.getFiniteLevels()) {
			name2ref(o.getInitialValue());
		}
		for (Rate o : model.getRates()) {
			name2ref(o.getExpression());
		}
		for (Auxiliary o : model.getAuxiliaries()) {
			if (o instanceof Variable) {
				Variable v = (Variable) o;
				name2ref(v.getExpression());
			}
		}
	}

	private void name2ref(Expression expr) {
		if (expr == null)
			return;
		else if (expr.getOperator() instanceof NameOperator) {
			if (expr.getValue().equals("_TIME_")) {
				expr.setMiddleOperand(new DefaultReferenceExpression(_TIME_));
			} else if (expr.getValue().equals("_TIME_STEP_")) {
				expr.setMiddleOperand(new DefaultReferenceExpression(_TIME_STEP_));
			} else
			expr.setMiddleOperand(new DefaultReferenceExpression(model
					.get((String) expr.getValue())));
			return;
		} else {
			name2ref(expr.getLeftOperand());
			name2ref(expr.getMiddleOperand());
			name2ref(expr.getRightOperand());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #setStepSize(java.lang.Double)
	 */
	@Override
	public void setStepSize(Double newStepSize) {
		stepSize = newStepSize;
		_TIME_STEP_.getExpression().setValue(stepSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod
	 * #step()
	 */
	@Override
	public void step() throws Exception {
		Double x0, x1, x2, x3, x4, x5, k1, k2, k3, k4;

		// Calculates variables
		 for (String key : auxiliaries.keySet()) {
			 Auxiliary aux = auxiliaries.get(key);
			 if (aux instanceof Variable) {
				 Variable v = (Variable)aux;
				 v.setValue((Double) v.getExpression().evaluate());
			 }
		 }

		// Calculates rates effects
		for (String key : rates.keySet()) {
			Rate rate = rates.get(key);
			Expression nf = rate.getExpression().getDeepCopy();
			OperatorEvaluator oev = new DefaultOperatorEvaluatorWithParameters();
			nf.setOperatorEvaluator(oev);

			// x0 = (Double) rate.getExpression().evaluate();
			if (rate.getFromLevel() instanceof FiniteStock) {
				FiniteStock flevel = (FiniteStock) rate.getFromLevel();
				// flevel.setValue(flevel.getValue() - rate.getValue()
				// * getStepSize());
				x0 = flevel.getValue();
				x1 = x0;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x1);
				k1 = (Double) nf.evaluate();

				x2 = x0 + k1 * getStepSize() / 2;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x2);
				k2 = (Double) nf.evaluate();

				x3 = x0 + k2 * getStepSize() / 2;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x3);
				k3 = (Double) nf.evaluate();

				x4 = x0 + k3 * getStepSize();
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x4);
				k4 = (Double) nf.evaluate();

				x5 = x0 - getStepSize() * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
				flevel.setValue(x5);
				rate.setValue((k1 + 2 * k2 + 2 * k3 + k4) / 6);
				if (rate.getToLevel() instanceof FiniteStock) {
					flevel = (FiniteStock) rate.getToLevel();
					flevel.setValue(flevel.getValue() + rate.getValue());
				}
			} else if (rate.getToLevel() instanceof FiniteStock) {
				FiniteStock flevel = (FiniteStock) rate.getToLevel();
				x0 = flevel.getValue();
				x1 = x0;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x1);
				k1 = (Double) nf.evaluate();

				x2 = x0 + k1 * getStepSize() / 2;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x2);
				k2 = (Double) nf.evaluate();

				x3 = x0 + k2 * getStepSize() / 2;
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x3);
				k3 = (Double) nf.evaluate();

				x4 = x0 + k3 * getStepSize();
				((OperatorEvaluatorParametrized) oev).getParameters().put(
						flevel.getName(), x4);
				k4 = (Double) nf.evaluate();

				x5 = x0 + getStepSize() * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
				flevel.setValue(x5);
				rate.setValue((k1 + 2 * k2 + 2 * k3 + k4) / 6);
			}

		}

		// Calculates all rates effects
		/*
		 * for (String key : rates.keySet()) { Rate rate = rates.get(key); if
		 * (rate.getFromLevel() instanceof FiniteStock) { FiniteStock flevel =
		 * (FiniteStock) rate.getFromLevel(); flevel.setValue(flevel.getValue()
		 * - rate.getValue() getStepSize()); } if (rate.getToLevel() instanceof
		 * FiniteStock) { FiniteStock flevel = (FiniteStock) rate.getToLevel();
		 * flevel.setValue(flevel.getValue() + rate.getValue() getStepSize()); }
		 * 
		 * }
		 */

		currentTime += getStepSize();
		_TIME_.getExpression().setValue(currentTime);
		currentStep++;
	}
}
