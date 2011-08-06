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
package br.ufjf.mmc.jynacore.metamodel.simulator.impl;

import java.util.HashMap;
import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluatorParametrized;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultOperatorEvaluatorWithParameters;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulationMethod;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstanceRK4Method implements
		MetaModelInstanceSimulationMethod {

	private Double initialTime;
	private MetaModelInstance model;
	private Double stepSize;
	private Double currentTime;
	private HashMap<String, ClassInstanceRate> rates;
	private HashMap<String, ClassInstanceStock> finiteLevels;
	private HashMap<String, ClassInstanceAuxiliary> auxiliaries;
	private HashMap<String, ClassInstanceProperty> properties;
	private int currentStep;
	private Variable _TIME_;
	private Variable _TIME_STEP_;

	public DefaultMetaModelInstanceRK4Method() {
		this.rates = new HashMap<String, ClassInstanceRate>();
		this.finiteLevels = new HashMap<String, ClassInstanceStock>();
		this.auxiliaries = new HashMap<String, ClassInstanceAuxiliary>();
		this.properties = new HashMap<String, ClassInstanceProperty>();
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
	public MetaModelInstance getModelInstance() {
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
		for (Entry<String, ClassInstanceStock> entry : finiteLevels.entrySet()) {
			entry.getValue().setValue(
					(Double) entry.getValue().getInitialValue().evaluate());
		}
		for (Entry<String, ClassInstanceRate> entry : rates.entrySet()) {
			entry.getValue().setValue((Double) entry.getValue().getExpression().evaluate());
//			entry.getValue().setValue(null);
		}
		for (Entry<String, ClassInstanceAuxiliary> entry : auxiliaries
				.entrySet()) {
			entry.getValue().setValue((Double) entry.getValue().getExpression().evaluate());
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
	public void setMetaModelInstance(MetaModelInstance model) throws Exception {
		this.model = model;
		this.rates = new HashMap<String, ClassInstanceRate>();
		this.finiteLevels = new HashMap<String, ClassInstanceStock>();
		for (Entry<String, ClassInstance> ciEntry : model.getClassInstances()
				.entrySet()) {
			ClassInstance classInstance = ciEntry.getValue();
			String classInstanceName = ciEntry.getKey();
			for (Entry<String, ClassInstanceItem> ciiEntry : classInstance
					.entrySet()) {
				ClassInstanceItem classInstanceItem = ciiEntry.getValue();
				String classInstanceItemName = ciiEntry.getKey();

				if (classInstanceItem instanceof ClassInstanceRate) {
					rates.put(classInstanceName + "." + classInstanceItemName,
							(ClassInstanceRate) classInstanceItem);
				}
				if (classInstanceItem instanceof ClassInstanceStock) {
					finiteLevels.put(classInstanceName + "."
							+ classInstanceItemName,
							(ClassInstanceStock) classInstanceItem);
				}
				if (classInstanceItem instanceof ClassInstanceAuxiliary) {
					auxiliaries.put(classInstanceName + "."
							+ classInstanceItemName,
							(ClassInstanceAuxiliary) classInstanceItem);
				}
				if (classInstanceItem instanceof ClassInstanceProperty) {
					properties.put(classInstanceName + "."
							+ classInstanceItemName,
							(ClassInstanceProperty) classInstanceItem);
				}
			}
		}
		setupReferences();
	}

	private void setupReferences() {
		try {
			model.updateReferences();
		} catch (Exception e) {
			e.printStackTrace();
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

		// Calculates all auxiliary values
		for (Entry<String, ClassInstanceAuxiliary> entry : auxiliaries.entrySet()) {
			ClassInstanceAuxiliary proc = entry.getValue();
				proc.setValue((Double) proc.getExpression().evaluate());
		}

		// Calculates rates
		for (Entry<String, ClassInstanceRate> ciiEntry : rates.entrySet()) {
			ClassInstanceRate rate = ciiEntry.getValue();
			Expression nf = rate.getExpression().getDeepCopy();
			OperatorEvaluator oev = new DefaultOperatorEvaluatorWithParameters();
			nf.setOperatorEvaluator(oev);

			x0 = (Double) rate.getExpression().evaluate();
			if (rate.getSource() instanceof ClassInstanceStock) {
				ClassInstanceStock flevel = (ClassInstanceStock) rate
						.getSource();
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
				rate.setValue(getStepSize() * (k1 + 2 * k2 + 2 * k3 + k4) / 6);
				if (rate.getTarget() instanceof ClassInstanceStock) {
					flevel = (ClassInstanceStock) rate.getTarget();
					flevel.setValue(flevel.getValue() + rate.getValue());
				}
			}else
			if (rate.getTarget() instanceof ClassInstanceStock) {

				ClassInstanceStock flevel = (ClassInstanceStock) rate
						.getTarget();
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
				// rate.setValue(x0 + x5);
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
		 * }
		 */

		currentTime += getStepSize();
		_TIME_.getExpression().setValue(currentTime);
		currentStep++;
	}
}
