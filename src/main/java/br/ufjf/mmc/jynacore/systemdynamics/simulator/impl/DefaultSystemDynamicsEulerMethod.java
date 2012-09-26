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
package br.ufjf.mmc.jynacore.systemdynamics.simulator.impl;

import br.ufjf.mmc.jynacore.JynaEvaluated;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.systemdynamics.Auxiliary;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.SystemDynamicsSimulationMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultSystemDynamicsEulerMethod implements
		SystemDynamicsSimulationMethod {

	private Double interval;
	private Map<String, Rate> rates;
	private Map<String, FiniteStock> finiteLevels;
	private Map<String, Auxiliary> auxiliaries;
	private SystemDynamicsModel model;
	private Double initialTime;
	private int currentStep;
	private double currentTime;
	private Variable _TIME_;
	private Variable _TIME_STEP_;

	public DefaultSystemDynamicsEulerMethod() {
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
		_TIME_STEP_
		.setExpression(new DefaultNumberConstantExpression(interval));
		setInterval(0.1);
	}

	@Override
	public Double getStepSize() {
		return interval;
	}

	@Override
	public void setStepSize(Double newInterval) {
		setInterval(newInterval);
	}

	private void setInterval(Double newInterval) {
		interval = newInterval;
		_TIME_STEP_.getExpression().setValue(interval);
	}

	@Override
	public void step() throws Exception {
		// Calculates variables
		for (String key : auxiliaries.keySet()) {
			Auxiliary aux = auxiliaries.get(key);
			if (aux instanceof Variable) {
				Variable v = (Variable) aux;
				v.setValue((Double) v.getExpression().evaluate());
			}
		}
		// Calculates all rates values
		for (String key : rates.keySet()) {
			Rate rate = rates.get(key);
			if (rate instanceof JynaEvaluated) {
            rate.setValue((Double) rate.getExpression().evaluate());
         }
		}

		// Calculates all rates effects
		for (Entry<String,Rate> entry : rates.entrySet()) {
			Rate rate = entry.getValue();
			if (rate.getFromLevel() instanceof FiniteStock) {
				FiniteStock flevel = (FiniteStock) rate.getFromLevel();
				flevel.setValue(flevel.getValue() - rate.getValue()
						* getStepSize());
			}
			if (rate.getToLevel() instanceof FiniteStock) {
				FiniteStock flevel = (FiniteStock) rate.getToLevel();
				flevel.setValue(flevel.getValue() + rate.getValue()
						* getStepSize());
			}

		}

		currentTime += interval;
		_TIME_.getExpression().setValue(currentTime);
		currentStep++;
	}

	public void setModel(SystemDynamicsModel model) {
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

	@Override
	public Double getInitialTime() {
		return initialTime;
	}

	@Override
	public void setInitialTime(Double newInitialTime) {
		initialTime = newInitialTime;
	}

	@Override
	public Double getTime() {
		return currentTime;
	}

	@Override
	public void reset() throws Exception {
		currentStep = 0;
		currentTime = initialTime;
		_TIME_.getExpression().setValue(currentTime);
		for (Entry<String, FiniteStock> entry : finiteLevels.entrySet()) {
			entry.getValue().setValue(
					(Double) entry.getValue().getInitialValue().evaluate());
		}
		for (Entry<String, Rate> entry : rates.entrySet()) {
			entry.getValue().setValue(
					(Double) entry.getValue().getExpression().evaluate());
		}
		for (Entry<String, Auxiliary> entry : auxiliaries.entrySet()) {
				 Auxiliary aux = entry.getValue();
				 if (aux instanceof Variable) {
					 Variable v = (Variable)aux;
					 v.setValue((Double) v.getExpression().evaluate());
				 }
		}
	}

	@Override
	public SystemDynamicsModel getModel() {
		return model;
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
		if (expr == null) {
      }
		else if (expr.getOperator() instanceof NameOperator) {
			if (expr.getValue().equals("_TIME_")) {
				expr.setMiddleOperand(new DefaultReferenceExpression(_TIME_));
			} else if (expr.getValue().equals("_TIME_STEP_")) {
				expr.setMiddleOperand(new DefaultReferenceExpression(
						_TIME_STEP_));
			} else {
            expr.setMiddleOperand(new DefaultReferenceExpression(model
                  .get((String) expr.getValue())));
         }
		} else {
			name2ref(expr.getLeftOperand());
			name2ref(expr.getMiddleOperand());
			name2ref(expr.getRightOperand());
		}

	}
}
