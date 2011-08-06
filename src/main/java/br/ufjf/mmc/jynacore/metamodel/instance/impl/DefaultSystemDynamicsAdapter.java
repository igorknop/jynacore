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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NameOperator;
import br.ufjf.mmc.jynacore.expression.ReferenceOperator;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.SystemDynamicsAdapter;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.Stock;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;

/**
 * @author Knop
 * 
 */
@Deprecated
public class DefaultSystemDynamicsAdapter implements SystemDynamicsAdapter {

	private static final String TARGET = "TARGET";
	private static final String SOURCE = "SOURCE";
	private static final String SEPARATOR = "_";
	SystemDynamicsModel sdModel;

	private List<ClassInstanceProperty> props;
	private List<ClassInstanceAuxiliary> procs;
	private List<ClassInstanceRate> rates;
	private List<ClassInstanceStock> stocks;

	/**
	 * 
	 */
	public DefaultSystemDynamicsAdapter() {
		sdModel = new DefaultSystemDynamicsModel();
		props = new ArrayList<ClassInstanceProperty>();
		procs = new ArrayList<ClassInstanceAuxiliary>();
		rates = new ArrayList<ClassInstanceRate>();
		stocks = new ArrayList<ClassInstanceStock>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufjf.mmc.jynacore.metamodel.instance.SystemDynamicsAdapter#convertToSystemDynamics(br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance)
	 */
	@Override
	public SystemDynamicsModel convertToSystemDynamics(
			MetaModelInstance modelInstance) throws Exception {
		// Create procs to hold system variables
		Variable currentTime = new DefaultVariable();
		currentTime.setName("_CURRENT_TIME_");
//		currentTime.setExpression("0.0");
		sdModel.put(currentTime.getName(), currentTime);

		Variable stepTime = new DefaultVariable();
		stepTime.setName("_STEP_TIME_");
//		stepTime.setExpression("0.1");
		sdModel.put(stepTime.getName(), stepTime);

		// Create All Levels(Stocks) and Properties
		for (ClassInstance mmClassInstance : modelInstance.getClassInstances()
				.values()) {
			for (ClassInstanceItem ciItem : mmClassInstance
			/* .getClassInstanceItems() */.values()) {
				if (ciItem instanceof ClassInstanceStock) {
					ClassInstanceStock ciStock = (ClassInstanceStock) ciItem;
					createStock(ciStock);
					stocks.add(ciStock);
				}
				if (ciItem instanceof ClassInstanceProperty) {
					ClassInstanceProperty ciProp = (ClassInstanceProperty) ciItem;
					createProperty(ciProp);
					props.add(ciProp);
				}
				if (ciItem instanceof ClassInstanceAuxiliary) {
					ClassInstanceAuxiliary ciProc = (ClassInstanceAuxiliary) ciItem;
					createProc(ciProc);
					procs.add(ciProc);
				}
				if (ciItem instanceof ClassInstanceRate) {
					ClassInstanceRate ciRate = (ClassInstanceRate) ciItem;
					createRate(ciRate);
					rates.add(ciRate);
				}
			}

		}
		// Update All informations
		for (ClassInstanceAuxiliary proc : procs) {
			InformationConsumer sdProc = (InformationConsumer) sdModel
					.get(getSDItemName(proc));
			sdProc.getInformations().putAll(
					getAllInformations(proc.getExpression()));
		}
		for (ClassInstanceRate rate : rates) {
			InformationConsumer sdRate = (InformationConsumer) sdModel
					.get(getSDItemName(rate));
			sdRate.getInformations().putAll(
					getAllInformations(rate.getExpression()));
		}

		// Update All Rates(Flux)
		for (ClassInstance mmClassInstance : modelInstance.getClassInstances()
				.values()) {
			for (ClassInstanceItem ciItem : mmClassInstance.values()) {
				if (ciItem instanceof ClassInstanceRate) {
					ClassInstanceRate ciRate = (ClassInstanceRate) ciItem;
					Rate sdRate = (Rate) sdModel.get(getSDItemName(ciRate));
					setRateDetails(ciRate, sdRate);
				}
			}
		}

		return sdModel;
	}

	private void createProc(ClassInstanceAuxiliary ciProc) {
		Variable sdVar = new DefaultVariable();
		String varName = getSDItemName(ciProc);
		sdVar.setName(varName);
		// Double iValue = 0.0;//(Double)ciStock.getInitialValue().evaluate();
		// sdVar.setValue(ciProc.getExpression());
//		sdVar.setExpression(ciProc.getExpression().toString());
		sdModel.put(varName, sdVar);
	}

	private String getSDItemName(ClassInstanceItem ciItem) {
		if (ciItem.getClassInstance() == null)
			return ciItem.getName();
		return ciItem.getClassInstance().getName() + SEPARATOR
				+ ciItem.getName();
	}

	private void createProperty(ClassInstanceProperty ciProp) throws Exception {
		Variable sdVar = new DefaultVariable();
		String varName = getSDItemName(ciProp);
		sdVar.setName(varName);
		// Double iValue = 0.0;//(Double)ciStock.getInitialValue().evaluate();
		sdVar.setValue(ciProp.getValue());
	//	sdVar.setExpression(ciProp.getValue().toString());
		sdModel.put(varName, sdVar);

	}

	private void createStock(ClassInstanceStock ciStock) {
		FiniteStock sdLevel = new DefaultFiniteStock();
		String lvlName = getSDItemName(ciStock);
		sdLevel.setName(lvlName);
		// FIX ME: NameExpression
		Double iValue = 0.0;// (Double)ciStock.getInitialValue().evaluate();
		sdLevel.setInitialValue(iValue);
		sdModel.put(lvlName, sdLevel);
	}

	private void createRate(ClassInstanceRate ciRate) {

		Rate sdRate = new DefaultRate();
		String rateName = getSDItemName(ciRate);
		sdRate.setName(rateName);

		// setRateDetails(ciRate, sdRate);
		sdModel.put(sdRate.getName(), sdRate);
	}

	private void setRateDetails(ClassInstanceRate ciRate, Rate sdRate) {
		Stock sdSource;
		Stock sdTarget;

		if (ciRate.getSource() != null) {
			sdSource = (Stock) sdModel.get(getSDItemName(ciRate.getSource()));
		} else {
			sdSource = new DefaultInfiniteStock();
			sdSource.setName(ciRate.getClassInstance().getName() + SEPARATOR
					+ ciRate.getName() + SEPARATOR + SOURCE);
			sdModel.put(sdSource.getName(), sdSource);
		}
		if (ciRate.getTarget() != null) {
			sdTarget = (Stock) sdModel.get(getSDItemName(ciRate.getTarget()));
		} else {
			sdTarget = new DefaultInfiniteStock();
			sdTarget.setName(ciRate.getClassInstance().getName() + SEPARATOR
					+ ciRate.getName() + SEPARATOR + TARGET);
			sdModel.put(sdTarget.getName(), sdTarget);
		}
		sdRate.setSource(sdSource);
		sdRate.setTarget(sdTarget);
	//	sdRate.setExpression(ciRate.getExpression().toString());
	}

	private Map<String, InformationSource> getAllInformations(Expression e) {
		Map<String, InformationSource> map = new HashMap<String, InformationSource>();
		if (e == null)
			return map;
		if (e.getOperator() instanceof NameOperator) {
			if (e.getMiddleOperand() != null
					&& e.getMiddleOperand().getOperator() instanceof ReferenceOperator) {
				String name = getSDItemName((ClassInstanceItem) e
						.getMiddleOperand().getValue());
				map.put(name, (InformationSource) sdModel.get(name));
				return map;
			}
		} else {
			map.putAll(getAllInformations(e.getLeftOperand()));
			map.putAll(getAllInformations(e.getMiddleOperand()));
			map.putAll(getAllInformations(e.getRightOperand()));
		}
		return map;
	}
}
