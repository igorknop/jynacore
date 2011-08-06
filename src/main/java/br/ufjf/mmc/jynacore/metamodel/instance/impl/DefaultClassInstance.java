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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufjf.mmc.jynacore.JynaEvaluated;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.MetaModelItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceException;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceInvalidLinkException;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceInvalidPropertyException;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceScenarioConnect;

/**
 * @author Knop
 * 
 */
public class DefaultClassInstance implements ClassInstance {

	private Map<String, ClassInstanceItem> instanceItens;
	private MetaModelClass metaModelClass;
	private String name;
	private MetaModelInstance metaModelInstance;
	private Map<String, ClassInstanceRate> rates;
	private Map<String, ClassInstanceSingleRelation> singleRelations;
	private Map<String, ClassInstanceAuxiliary> procs;
	private Map<String, ClassInstanceProperty> properties;
	private Map<String, ClassInstanceStock> stocks;
	private Map<String, ClassInstanceMultiRelation> multiRelations;

	public DefaultClassInstance() {
		instanceItens = new HashMap<String, ClassInstanceItem>();
		rates = new HashMap<String, ClassInstanceRate>();
		procs = new HashMap<String, ClassInstanceAuxiliary>();
		properties = new HashMap<String, ClassInstanceProperty>();
		stocks = new HashMap<String, ClassInstanceStock>();
		singleRelations = new HashMap<String, ClassInstanceSingleRelation>();
		multiRelations = new HashMap<String, ClassInstanceMultiRelation>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#createFrom(br.ufjf
	 * .mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void createFrom(MetaModelClass metaModelClass) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#getClassInstanceItems
	 * ()
	 */
	// @Override
	// public Map<String, ClassInstanceItem> getClassInstanceItems() {
	// return instanceItens;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#getMetaModelClass()
	 */
	@Override
	public MetaModelClass getMetaModelClass() {
		return metaModelClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#setClassInstanceItems(java.util.Map)
	// */
	// @Override
	// public void setClassInstanceItems(Map<String, ClassInstanceItem>
	// newValues) {
	// instanceItens = newValues;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#setMetaModelClass
	 * (br.ufjf.mmc.jynacore.metamodel.MetaModelClass)
	 */
	@Override
	public void setMetaModelClass(MetaModelClass newClass) {
		metaModelClass = newClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public Double getProperty(String propertyName) throws Exception {
		ClassInstanceProperty prop = (ClassInstanceProperty) get(propertyName);
		if (prop == null)
			throw new MetaModelInstanceInvalidPropertyException(
					getMetaModelClass().getName(), propertyName);
		return prop.getValue();
	}

	@Override
	public void setProperty(String propertyName, Double newValue)
			throws MetaModelInstanceInvalidPropertyException {
		ClassInstanceProperty prop = (ClassInstanceProperty) get(propertyName);
		if (prop == null)
			throw new MetaModelInstanceInvalidPropertyException(
					getMetaModelClass().getName(), propertyName);
		prop.setValue(newValue);
	}

	@Override
	public void setLink(String relationName, String targetName) {
		MetaModelItem mmi = getMetaModelClass().getMetaModel()
				.get(relationName);
		ClassInstance target = getMetaModelInstance().getClassInstances().get(
				targetName);
		if (mmi instanceof MetaModelSingleRelation) {
			MetaModelSingleRelation relation = (MetaModelSingleRelation) mmi;
			if (target.getMetaModelClass().equals(relation.getTarget())
					&& getMetaModelClass().equals(relation.getSource())) {
				DefaultClassInstanceSingleRelation sr = new DefaultClassInstanceSingleRelation();
				sr.setName(relationName);
				sr.setTarget(target);
				put(relationName, sr);
			}

		}
		if (mmi instanceof MetaModelMultiRelation) {
			MetaModelMultiRelation relation = (MetaModelMultiRelation) mmi;
			if (target.getMetaModelClass().equals(relation.getTarget())
					&& getMetaModelClass().equals(relation.getSource())) {

				ClassInstanceItem mi = get(relationName);
				DefaultClassInstanceMultiRelation mr;

				if (mi == null) {
					mr = new DefaultClassInstanceMultiRelation();
					mr.setName(relationName);
					put(relationName, mr);
				} else {
					mr = (DefaultClassInstanceMultiRelation) mi;
				}
				mr.getTargets().put(target.getName(), target);
			}

		}

	}

	@Override
	public void setLink(String relationName, String targetName,
			String targetRole) throws MetaModelInstanceInvalidLinkException {
		MetaModelItem mmi = getMetaModelClass().getMetaModel()
				.get(relationName);
		ClassInstance target = getMetaModelInstance().getClassInstances().get(
				targetName);
		if (mmi instanceof MetaModelSingleRelation) {
			MetaModelSingleRelation relation = (MetaModelSingleRelation) mmi;
			if (target.getMetaModelClass().equals(relation.getTarget())
					&& getMetaModelClass().equals(relation.getSource())) {
				DefaultClassInstanceSingleRelation sr = new DefaultClassInstanceSingleRelation();
				sr.setName(relationName);
				sr.setTarget(target);
				put(relationName, sr);
				DefaultClassInstanceSingleRelation sr2 = new DefaultClassInstanceSingleRelation();
				sr2.setName(targetRole);
				sr2.setTarget(this);
				target.put(targetRole, sr2);
			} else {
				throw new MetaModelInstanceInvalidLinkException(relationName,
						targetName, target.getMetaModelClass().getName());
			}

		}
		if (mmi instanceof MetaModelMultiRelation) {
			MetaModelMultiRelation relation = (MetaModelMultiRelation) mmi;
			if (target.getMetaModelClass().equals(relation.getTarget())
					&& getMetaModelClass().equals(relation.getSource())) {

				ClassInstanceItem mi = get(relationName);
				DefaultClassInstanceMultiRelation mr;

				if (mi == null) {
					mr = new DefaultClassInstanceMultiRelation();
					put(relationName, mr);
				} else {
					mr = (DefaultClassInstanceMultiRelation) mi;
				}
				mr.setName(relationName);
				mr.getTargets().put(target.getName(), target);
				DefaultClassInstanceSingleRelation sr = new DefaultClassInstanceSingleRelation();
				sr.setName(targetRole);
				sr.setTarget(this);
				target.put(targetRole, sr);
			} else {
				throw new MetaModelInstanceInvalidLinkException(relationName,
						targetName, target.getMetaModelClass().getName());
			}

		}

	}

	@Override
	public MetaModelInstance getMetaModelInstance() {
		return metaModelInstance;
	}

	@Override
	public void setMetaModelInstance(MetaModelInstance newMetaModelInstance) {
		metaModelInstance = newMetaModelInstance;
	}

	@Override
	public void clear() {
		instanceItens.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return instanceItens.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return instanceItens.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, ClassInstanceItem>> entrySet() {
		return instanceItens.entrySet();
	}

	@Override
	public ClassInstanceItem get(Object key) {
		return instanceItens.get(key);
	}

	@Override
	public boolean isEmpty() {
		return instanceItens.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return instanceItens.keySet();
	}

	@Override
	public ClassInstanceItem put(String key, ClassInstanceItem value) {
		value.setClassInstance(this);
		if (value instanceof ClassInstanceRate) {
			rates.put(key, (ClassInstanceRate) value);
		}
		if (value instanceof ClassInstanceStock) {
			stocks.put(key, (ClassInstanceStock) value);
		}
		if (value instanceof ClassInstanceAuxiliary) {
			procs.put(key, (ClassInstanceAuxiliary) value);
		}
		if (value instanceof ClassInstanceProperty) {
			properties.put(key, (ClassInstanceProperty) value);
		}
		if (value instanceof ClassInstanceSingleRelation) {
			singleRelations.put(key, (ClassInstanceSingleRelation) value);
		}
		if (value instanceof ClassInstanceMultiRelation) {
			multiRelations.put(key, (ClassInstanceMultiRelation) value);
		}

		return instanceItens.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends ClassInstanceItem> m) {
		for (Entry<? extends String, ? extends ClassInstanceItem> entry : m
				.entrySet()) {
			entry.getValue().setClassInstance(this);
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public ClassInstanceItem remove(Object key) {
		rates.remove(key);
		stocks.remove(key);
		procs.remove(key);
		properties.remove(key);
		singleRelations.remove(key);
		multiRelations.remove(key);
		return instanceItens.remove(key);
	}

	@Override
	public int size() {
		return instanceItens.size();
	}

	@Override
	public Collection<ClassInstanceItem> values() {
		return instanceItens.values();
	}

	public Map<String, ClassInstanceRate> getRates() {
		return rates;
	}

	public Map<String, ClassInstanceAuxiliary> getProcs() {
		return procs;
	}

	public Map<String, ClassInstanceProperty> getProperties() {
		return properties;
	}

	public Map<String, ClassInstanceStock> getStocks() {
		return stocks;
	}

	public Map<String, ClassInstanceSingleRelation> getSingleRelation() {
		return singleRelations;
	}

	public Map<String, ClassInstanceMultiRelation> getMultiRelation() {
		return multiRelations;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setScenarioConnection(String scenarioName, String connectionName)
			throws Exception {
		MetaModelScenario scenario = getMetaModelClass().getMetaModel()
				.getScenario(scenarioName);
		MetaModelScenarioConnection connection = scenario.get(connectionName);
		if (!connection.getClassName().equals(getMetaModelClass().getName()))
			throw new MetaModelInstanceException(
					"Invalid Scenario Connection Class. Expecting: "
							+ connection.getClassName() + " found: "
							+ getMetaModelClass().getName());
		for (MetaModelScenarioConstraint c : connection.getConstraints()) {
			if (c.getRelationName() == null) {
				setScenarioConnection(c.getScenarioName(), c
						.getConnectionName());
			} else {
				ClassInstanceMultiRelation mr = getMultiRelation().get(
						c.getRelationName());
				for (Entry<String, ClassInstance> t : mr.getTargets()
						.entrySet()) {
					t.getValue().setScenarioConnection(c.getScenarioName(),
							c.getConnectionName());
				}
			}
		}
		for (Entry<String, MetaModelClassItem> e : connection.getClassItems()
				.entrySet()) {
			if (e.getValue() instanceof MetaModelClassRate)
				continue;
			ClassInstanceItem newInstance = e.getValue().getNewInstance();
			put(newInstance.getName(), newInstance);
		}
		// for (Entry<String, ClassInstanceItem> e : this.entrySet()) {
		for (Entry<String, MetaModelClassItem> e : connection.getClassItems()
				.entrySet()) {
			if (e.getValue() instanceof MetaModelClassRate) {
				MetaModelClassRate mmRate = (MetaModelClassRate) e.getValue();
				ClassInstanceRate newRate = (ClassInstanceRate) mmRate
						.getNewInstance();
				put(newRate.getName(), newRate);
				if (mmRate.getTarget() != null) {
					ClassInstanceItem t;
					String mmt = mmRate.getTarget().getName();
					if (mmt.contains(".")) {
						String[] tokens = mmt.split("\\.");
						ClassInstanceSingleRelation r = (ClassInstanceSingleRelation) get(tokens[0]);
						t = r.getTarget().get(tokens[1]);
					} else {
						t = this.get(mmt);
					}
					newRate.setTarget((ClassInstanceStock) t);
				}
				if (mmRate.getSource() != null) {
					String mms = mmRate.getSource().getName();
					ClassInstanceItem s;
					if (mms.contains(".")) {
						String[] tokens = mms.split("\\.");
						ClassInstanceSingleRelation r = (ClassInstanceSingleRelation) get(tokens[0]);
						s = r.getTarget().get(tokens[1]);
					} else {
						s = this.get(mms);
					}
					newRate.setSource((ClassInstanceStock) s);
				}
			}
		}

		getMetaModelInstance().updateReferences();
		for (MetaModelScenarioAffect affect : connection.getAffectList()) {
			ClassInstanceItem affectedItem = instanceItens
					.get(affect.getName());
			Expression oldExpression = ((JynaEvaluated) affectedItem)
					.getExpression();
			Expression newExpression = affect.getExpression().getDeepCopy();

			((JynaEvaluated) affectedItem).setExpression(replaceOldExpression(
					newExpression, oldExpression, affectedItem.getName()));
		}
		getMetaModelInstance().getScenariosConnects().add(
				new DefaultMetaModelInstanceScenarioConnect(scenario,
						connection, this));
	}

	private Expression replaceOldExpression(Expression expression,
			Expression oldExpression, String nameToReplace) {

		if (expression == null)
			return null;
		else if (expression.getOperator() instanceof NameOperator) {
			if (nameToReplace.equals(expression.getValue())) {
				// expression = new DefaultExpression();
				expression = oldExpression.getDeepCopy();
				return expression;
			}
		}
		expression.setLeftOperand(replaceOldExpression(expression
				.getLeftOperand(), oldExpression, nameToReplace));
		expression.setMiddleOperand(replaceOldExpression(expression
				.getMiddleOperand(), oldExpression, nameToReplace));
		expression.setRightOperand(replaceOldExpression(expression
				.getRightOperand(), oldExpression, nameToReplace));
		expression.setExtraOperand(replaceOldExpression(expression
				.getExtraOperand(), oldExpression, nameToReplace));
		return expression;
	}

	@Override
	public List<MetaModelScenario> getConnectedScenarios() {
		List<MetaModelScenario> l = new ArrayList<MetaModelScenario>();
		for (MetaModelInstanceScenarioConnect c : getMetaModelInstance()
				.getScenariosConnects()) {
			if (c.getInstance().equals(this))
				l.add(c.getScenario());
		}
		return l;
	}

}
