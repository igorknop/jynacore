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
package br.ufjf.mmc.jynacore.metamodel.instance.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultMultipleReferenceExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceFactory;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceScenarioConnect;

/**
 * @author Knop
 * 
 */
public class DefaultMetaModelInstance implements MetaModelInstance {
	public static final String _TIME_ = "_TIME_";
	public static final String _TIME_STEP_ = "_TIME_STEP_";
	private Map<String, ClassInstance> classInstances;
	private String name;
	private MetaModel metaModel;
	private ClassInstanceProperty stepTime;
	private ClassInstanceProperty currentTime;
	private String metaModelFileName;
	private List<MetaModelInstanceScenarioConnect> scenariosConnects;

	public DefaultMetaModelInstance() {
		classInstances = new HashMap<String, ClassInstance>();
		stepTime = new DefaultClassInstanceProperty();
		stepTime.setName(_TIME_STEP_);
		stepTime.setValue(0.1);
		currentTime = new DefaultClassInstanceProperty();
		currentTime.setName(_TIME_);
		currentTime.setValue(0.0);
		scenariosConnects = new ArrayList<MetaModelInstanceScenarioConnect>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#getClassInstances
	 * ()
	 */
	@Override
	public Map<String, ClassInstance> getClassInstances() {
		return classInstances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#getMetaModel()
	 */
	@Override
	public MetaModel getMetaModel() {
		return metaModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#setClassInstances
	 * (java.util.Map)
	 */
	@Override
	public void setClassInstances(Map<String, ClassInstance> newInstances) {
		classInstances = newInstances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#setName(java
	 * .lang.String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance#setMetaModel
	 * (br.ufjf.mmc.jynacore.metamodel.MetaModel)
	 */
	@Override
	public void setMetaModel(MetaModel newModel) {
		metaModel = newModel;
	}

	@Override
	public ClassInstance addNewClassInstance(String instanceName,
			String className) throws Exception {
		MetaModelClass clazz = (MetaModelClass) metaModel.get(className);
		if (clazz == null)
			throw new Exception("The class \"" + className
					+ "\" not found in MetaModel " + metaModel.getName());
		if (classInstances.get(instanceName) != null)
			throw new Exception("An instance \"" + instanceName
					+ "\" already defined");
		MetaModelInstanceFactory factory = new DefaultMetaModelInstanceFactory();
		ClassInstance newInstance = factory.createClassInstance(instanceName,
				clazz);
		newInstance.setMetaModelInstance(this);
		getClassInstances().put(instanceName, newInstance);

		return newInstance;
	}

	@Override
	public void updateReferences() throws Exception {
		for (Entry<String, ClassInstance> classInstanceEntry : classInstances
				.entrySet()) {
			ClassInstance ci = (ClassInstance) classInstanceEntry.getValue();
			for (Entry<String, ClassInstanceItem> ciItemEntry : ci.entrySet()) {
				ClassInstanceItem ciItem = ciItemEntry.getValue();
				if (ciItem instanceof ClassInstanceAuxiliary) {
					ClassInstanceAuxiliary ciProc = (ClassInstanceAuxiliary) ciItem;
					updateExpression(ciProc.getExpression(), ciItem, ci, this);
				}
				if (ciItem instanceof ClassInstanceStock) {
					ClassInstanceStock ciStock = (ClassInstanceStock) ciItem;
					updateExpression(ciStock.getInitialValue(), ciItem, ci,
							this);
				}
				if (ciItem instanceof ClassInstanceRate) {
					ClassInstanceRate ciRate = (ClassInstanceRate) ciItem;
					updateExpression(ciRate.getExpression(), ciItem, ci, this);
				}
			}
		}

	}

	private void updateExpression(Expression expression,
			ClassInstanceItem classInstanceItem, ClassInstance classInstance,
			MetaModelInstance metaModelInstance) throws Exception {

		if (expression == null)
			return;
		if (expression.getOperator() instanceof NameOperator) {
			String name = (String) expression.getValue();
			ClassInstanceItem reference = classInstance.get(name);

			if (reference != null) {
				Expression refExpr = new DefaultReferenceExpression(reference);
				expression.setMiddleOperand(refExpr);
				return;
			} else {
				if (name.contains(".")) {
					String[] tokens = name.split("\\.");
					String relation = tokens[0];
					String item = tokens[1];
					ClassInstanceItem rl = classInstance.get(relation);
					if (rl instanceof ClassInstanceSingleRelation) {
						ClassInstanceSingleRelation sr = (ClassInstanceSingleRelation) rl;
						Expression refExpr = new DefaultReferenceExpression(sr
								.getTarget().get(item));
						expression.setMiddleOperand(refExpr);
						return;
					} else if (rl instanceof ClassInstanceMultiRelation) {
						throw new Exception(
								"Using a multiple external reference without group functions: "
										+ name);
					}
				} else

				// LOOK AT globals
				if (name.equals(_TIME_STEP_)) {
					Expression refExpr = new DefaultReferenceExpression(
							stepTime);
					expression.setMiddleOperand(refExpr);
					return;
				} else if (name.equals(_TIME_)) {
					Expression refExpr = new DefaultReferenceExpression(
							currentTime);
					expression.setMiddleOperand(refExpr);
					return;

				} else
					throw new Exception("Unknow reference: "
							+ expression.getValue());
			}

		}
		if (Arrays.asList(NumberOperator.GROUP_OPERATORS).contains(
				expression.getOperator())) {
			String relationName = (String) expression.getLeftOperand()
					.getValue();
			String relationTarget = (String) expression.getRightOperand()
					.getValue();
			ClassInstanceRelation relation = (ClassInstanceRelation) classInstance
					.get(relationName);
			if (relation instanceof ClassInstanceSingleRelation) {
				ClassInstanceSingleRelation sRel = (ClassInstanceSingleRelation) relation;
				Set<Object> newSet = new HashSet<Object>();
				newSet.add(sRel.getTarget().get(relationTarget));
				Expression refExpr = new DefaultMultipleReferenceExpression(
						newSet);
				expression.setMiddleOperand(refExpr);
			} else if (relation instanceof ClassInstanceMultiRelation) {
				ClassInstanceMultiRelation mRel = (ClassInstanceMultiRelation) relation;
				Set<Object> newSet = new HashSet<Object>();
				for (Entry<String, ClassInstance> entry : mRel.getTargets()
						.entrySet()) {
					newSet.add(mRel.getTargets().get(entry.getKey()).get(
							relationTarget));
				}
				Expression refExpr = new DefaultMultipleReferenceExpression(
						newSet);
				expression.setMiddleOperand(refExpr);
			}
			return;
		}
		// if (expression.getOperator() == NumberOperator.GROUPSUM) {
		// String relationName = (String) expression.getLeftOperand()
		// .getValue();
		// String relationTarget = (String) expression.getRightOperand()
		// .getValue();
		// ClassInstanceRelation relation = (ClassInstanceRelation)
		// classInstance
		// .get(relationName);
		// if (relation instanceof ClassInstanceSingleRelation) {
		// ClassInstanceSingleRelation sRel = (ClassInstanceSingleRelation)
		// relation;
		// Set<Object> newSet = new HashSet<Object>();
		// newSet.add(sRel.getTarget().get(relationTarget));
		// Expression refExpr = new DefaultMultipleReferenceExpression(
		// newSet);
		// expression.setMiddleOperand(refExpr);
		// } else if (relation instanceof ClassInstanceMultiRelation) {
		// ClassInstanceMultiRelation mRel = (ClassInstanceMultiRelation)
		// relation;
		// Set<Object> newSet = new HashSet<Object>();
		// for (Entry<String, ClassInstance> entry : mRel.getTargets()
		// .entrySet()) {
		// newSet.add(mRel.getTargets().get(entry.getKey()).get(
		// relationTarget));
		// }
		// Expression refExpr = new DefaultMultipleReferenceExpression(
		// newSet);
		// expression.setMiddleOperand(refExpr);
		// }
		// return;
		// }
		updateExpression(expression.getLeftOperand(), classInstanceItem,
				classInstance, metaModelInstance);
		updateExpression(expression.getMiddleOperand(), classInstanceItem,
				classInstance, metaModelInstance);
		updateExpression(expression.getRightOperand(), classInstanceItem,
				classInstance, metaModelInstance);
	}

	@Override
	public String getMetaModelFileName() {
		return metaModelFileName;
	}

	@Override
	public void setMetaModelFileName(String newFileName) {
		metaModelFileName = newFileName;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public List<MetaModelInstanceScenarioConnect> getScenariosConnects() {
		return scenariosConnects;
	}

	@Override
	public Collection<JynaValued> getAllJynaValued() {
		List<JynaValued> l = new ArrayList<JynaValued>();
		for (Entry<String, ClassInstance> ciEntry : getClassInstances()
				.entrySet()) {
			ClassInstance ci = ciEntry.getValue();
			for (Entry<String, ClassInstanceItem> entry : ci.entrySet()) {
				if (entry.getValue() instanceof JynaValued) {
					l.add((JynaValued) entry.getValue());
				}
			}
		}
		return l;
	}
}
