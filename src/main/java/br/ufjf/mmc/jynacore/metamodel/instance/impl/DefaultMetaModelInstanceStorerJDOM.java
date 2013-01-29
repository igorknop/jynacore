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
package br.ufjf.mmc.jynacore.metamodel.instance.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import br.ufjf.mmc.jynacore.JynaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.exceptions.instance.MetaModelInstanceException;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelScenarioStorer;
import br.ufjf.mmc.jynacore.metamodel.impl.JDOMMetaModelStorer;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceItem;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceRate;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceStock;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceTable;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceScenarioConnect;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstanceStorer;

public class DefaultMetaModelInstanceStorerJDOM implements MetaModelInstanceStorer {

	public static final String EXPRESSION = "expression";
	public static final String INFINITE_LEVEL = "infiniteLevel";
	public static final String AUXILIARY = "auxiliary";

	public static final String SCENARIOS = "scenarios";
	public static final String SCENARIO = "scenario";
	public static final String SCENARIO_NAME = "name";
	public static final String SCENARIO_FILE_NAME = "file";
	public static final String SCENARIO_CONNECTS = "connects";
	public static final String SCENARIO_CONNECT = "connect";
	public static final String SCENARIO_CONNECT_NAME = "name";
	public static final String SCENARIO_CONNECT_INSTANCE = "instance";
	private static final String SCENARIOS_FILES = "files";

	public static final String INITIAL_VALUE = "initialValue";
	public static final String CLASS_INSTANCE_NAME = "name";
	public static final String FINITE_LEVEL = "finiteLevel";
	public static final String INFORMATIONS = "informations";
	public static final String AUXILIARIES = "auxiliaries";
	public static final String RATES = "rates";
	public static final String INFINITE_LEVELS = "infiniteLevels";
	public static final String LEVELS = "levels";
	private static final String META_MODEL_INSTANCE_NAME = "name";
	public static final String META_MODEL_INSTANCE = "metaModelInstance";
	private static final String CLASS_INSTANCES = "classInstances";
	private static final String CLASS_INSTANCE = "classInstance";
	private static final String META_MODEL_FILE = "metaModelFile";
	private static final String CLASS_INSTANCE_CLASS = "class";
	private static final String CLASS_INSTANCE_ITEMS = "items";
	private static final String CLASS_INSTANCE_PROPERTY = "property";
	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_VALUE = "value";
	private static final String CLASS_INSTANCE_STOCK = "stock";
	private static final String STOCK_NAME = "name";
	private static final String STOCK_VALUE = "value";
	private static final String CLASS_INSTANCE_SINGLE_RELATION = "singleRelation";
	private static final String RELATION_NAME = "name";
	private static final String RELATION_TARGET = "target";
	private static final String CLASS_INSTANCE_MULTI_RELATION = "multiRelation";
	private static final String RELATION_TARGETS = "targets";
	private static final String TABLE = "table";
	private static final String TABLE_NAME = "name";
	private static final String TABLE_VALUES = "values";
	private static final String TABLE_VALUE = "value";

	private Document doc;
	private Element eRoot;
	// private Element eMetaModelInstanceName;
	private Element eClassInstances;

	// private Element eRelations;

	// @Override
	public void saveToFile(MetaModelInstance modelInstance, File fileName)
			throws Exception {

		doc = new Document();
		eRoot = new Element(META_MODEL_INSTANCE);
		doc.setRootElement(eRoot);
		Element eMetaModelInstanceName = new Element(META_MODEL_INSTANCE_NAME);
		eMetaModelInstanceName.setText(modelInstance.getName());
		eRoot.addContent(eMetaModelInstanceName);

		Element eMetaModelFile = new Element(META_MODEL_FILE);
		eMetaModelFile.setText(modelInstance.getMetaModelFileName());
		eRoot.addContent(eMetaModelFile);

		eClassInstances = new Element(CLASS_INSTANCES);
		eRoot.addContent(eClassInstances);

		for (Entry<String, ClassInstance> entry : modelInstance
				.getClassInstances().entrySet()) {
			ClassInstance classInstance = entry.getValue();
			String classInstanceName = entry.getKey();
			eClassInstances.addContent(getClassInstanceElement(
					classInstanceName, classInstance));
		}

		Element eScenarios = new Element(SCENARIOS);
		eRoot.addContent(eScenarios);
		Element eScenarioFiles = new Element(SCENARIOS_FILES);
		eScenarios.addContent(eScenarioFiles);
		Set<String> scenarioFiles = new HashSet<String>();
		for (Entry<String, MetaModelScenario> e : modelInstance.getMetaModel()
				.getScenarios().entrySet()) {
			scenarioFiles.add(e.getValue().getFileName());
		}
		for (String f : scenarioFiles) {
			Element eScenarioFileName = new Element(SCENARIO_FILE_NAME)
					.setText(f);
			eScenarioFiles.addContent(eScenarioFileName);
		}
		Element eScenarioConnects = new Element(SCENARIO_CONNECTS);
		eScenarios.addContent(eScenarioConnects);

		for (MetaModelInstanceScenarioConnect c : modelInstance
				.getScenariosConnects()) {
			Element eScenarioConnect = new Element(SCENARIO_CONNECT);
			eScenarioConnects.addContent(eScenarioConnect);

			 Element eScenarioName = new Element(SCENARIO).setText(c
			 .getScenario().getName());
			 eScenarioConnect.addContent(eScenarioName);
			Element eScenarioConnectName = new Element(SCENARIO_CONNECT_NAME)
					.setText(c.getConnect().getName());
			eScenarioConnect.addContent(eScenarioConnectName);
			Element eScenarioConnectInstance = new Element(
					SCENARIO_CONNECT_INSTANCE).setText(c.getInstance()
					.getName());
			;
			eScenarioConnect.addContent(eScenarioConnectInstance);
		}

		XMLOutputter outputter = new XMLOutputter();

		outputter.setFormat(Format.getPrettyFormat());

		FileOutputStream fo = new FileOutputStream(fileName);
		outputter.output(doc, fo);
		fo.close();
	}

	private Element getClassInstanceElement(String classInstanceName,
			ClassInstance classInstance) throws Exception {
		Element eClassInstance = new Element(CLASS_INSTANCE);
		eClassInstance.addContent(new Element(CLASS_INSTANCE_NAME)
				.setText(classInstance.getName()));
		eClassInstance.addContent(new Element(CLASS_INSTANCE_CLASS)
				.setText(classInstance.getMetaModelClass().getName()));
		Element eClassInstanceItems = new Element(CLASS_INSTANCE_ITEMS);
		eClassInstance.addContent(eClassInstanceItems);
		for (Entry<String, ClassInstanceItem> entry : classInstance.entrySet()) {
			ClassInstanceItem item = entry.getValue();
			if (item instanceof ClassInstanceAuxiliary) {

			} else if (item instanceof ClassInstanceProperty) {
				ClassInstanceProperty prop = (ClassInstanceProperty) item;
				Element eProp = new Element(CLASS_INSTANCE_PROPERTY);
				eProp.addContent(new Element(PROPERTY_NAME).setText(prop
						.getName()));
				eProp.addContent(new Element(PROPERTY_VALUE).setText(prop
						.getValue().toString()));
				eClassInstanceItems.addContent(eProp);

			} else if (item instanceof ClassInstanceRate) {

			} else if (item instanceof ClassInstanceSingleRelation) {
				ClassInstanceSingleRelation relation = (ClassInstanceSingleRelation) item;
				Element eRelation = new Element(CLASS_INSTANCE_SINGLE_RELATION);
				eRelation.addContent(new Element(RELATION_NAME)
						.setText(relation.getName()));
				eRelation.addContent(new Element(RELATION_TARGET)
						.setText(relation.getTarget().getName()));

				// eRelation.addContent(new
				// Element(RELATION_ROLE).setText(relation.getRole()));

				eClassInstanceItems.addContent(eRelation);
			} else if (item instanceof ClassInstanceMultiRelation) {
				ClassInstanceMultiRelation relation = (ClassInstanceMultiRelation) item;
				Element eRelation = new Element(CLASS_INSTANCE_MULTI_RELATION);
				eRelation.addContent(new Element(RELATION_NAME)
						.setText(relation.getName()));
				Element eTargets = new Element(RELATION_TARGETS);
				eRelation.addContent(eTargets);
				for (Entry<String, ClassInstance> entryTargets : relation
						.getTargets().entrySet()) {
					ClassInstance target = entryTargets.getValue();
					eTargets.addContent(new Element(RELATION_TARGET)
							.setText(target.getName()));
				}
				eClassInstanceItems.addContent(eRelation);
			} else if (item instanceof ClassInstanceStock) {
				ClassInstanceStock stock = (ClassInstanceStock) item;
				Element eStock = new Element(CLASS_INSTANCE_STOCK);
				eStock.addContent(new Element(STOCK_NAME).setText(stock
						.getName()));
				eStock.addContent(new Element(STOCK_VALUE).setText(stock
						.getValue().toString()));
				eClassInstanceItems.addContent(eStock);

			} else if (item instanceof ClassInstanceTable) {
				ClassInstanceTable fobj = (DefaultClassInstanceTable) item;
				Element eObj = new Element(TABLE);
				eObj
						.addContent(new Element(TABLE_NAME).setText(fobj
								.getName()));
				Element eVls = new Element(TABLE_VALUES);
				for (Double d : fobj.getValues()) {
					eVls.addContent(new Element(TABLE_VALUE).addContent(d
							.toString()));
				}
				eObj.addContent(eVls);
				eClassInstanceItems.addContent(eObj);
			} else {
				throw new MetaModelInstanceException(
						"Unknow Class Instace Item: " + item.getName() + " "
								+ item.getClassInstance().getName());
			}
		}
		return eClassInstance;
	}

 @Override
	public MetaModelInstance loadFromFile(File fileName) throws Exception {
		MetaModelInstance modelInstance = new DefaultMetaModelInstance();

		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(fileName);

		eRoot = doc.getRootElement();
		String metaModelFileName = eRoot.getChildText(META_MODEL_FILE);
		File metaModelFile = new File(fileName.getParent(), metaModelFileName);
		MetaModelStorer metaModelStorer = new JDOMMetaModelStorer();
		MetaModel metaModel = metaModelStorer.loadFromFile(metaModelFile);
		modelInstance.setMetaModel(metaModel);

		modelInstance.setName(eRoot.getChildText(META_MODEL_INSTANCE_NAME));
		modelInstance.setMetaModelFileName(eRoot.getChildText(META_MODEL_FILE));

		eClassInstances = eRoot.getChild(CLASS_INSTANCES);

		// Create all instances, properties and set initial levels
		for (Object obj : eClassInstances.getChildren(CLASS_INSTANCE)) {
			Element eClassInstance = (Element) obj;
			String classInstanceName = eClassInstance
					.getChildText(CLASS_INSTANCE_NAME);
			String classInstanceClass = eClassInstance
					.getChildText(CLASS_INSTANCE_CLASS);
			ClassInstance classInstance = modelInstance.addNewClassInstance(
					classInstanceName, classInstanceClass);
			Element eClassItems = eClassInstance.getChild(CLASS_INSTANCE_ITEMS);
			for (Object oProp : eClassItems
					.getChildren(CLASS_INSTANCE_PROPERTY)) {
				Element eClassInstanceProp = (Element) oProp;
				String propName = eClassInstanceProp
						.getChildText(PROPERTY_NAME);
				Double propValue = Double.valueOf(eClassInstanceProp
						.getChildText(PROPERTY_VALUE));
				classInstance.setProperty(propName, propValue);
			}
			for (Object oLevel : eClassItems.getChildren(CLASS_INSTANCE_STOCK)) {
				Element eClassInstanceLevel = (Element) oLevel;
				String levelName = eClassInstanceLevel.getChildText(STOCK_NAME);
				Double levelValue = Double.valueOf(eClassInstanceLevel
						.getChildText(STOCK_VALUE));
				ClassInstanceStock level = (ClassInstanceStock) classInstance
						.get(levelName);
				level.setValue(levelValue);
			}
			for (Object oeTable : eClassItems.getChildren(TABLE)) {
				Element eTable = (Element) oeTable;
				ClassInstanceTable oTable = (ClassInstanceTable) classInstance
						.get(eTable.getChildText(TABLE_NAME));
				oTable.getValues().clear();
				Element eVls = eTable.getChild(TABLE_VALUES);
				if (eVls != null) {
					for (Object ovl : eVls.getChildren(TABLE_VALUE)) {
						Element evl = (Element) ovl;
						oTable.getValues().add(Double.valueOf(evl.getText()));
					}
				}
			}
		}

		// Set all links
		for (Object obj : eClassInstances.getChildren(CLASS_INSTANCE)) {
			Element eClassInstance = (Element) obj;
			Element eItems = (Element) eClassInstance
					.getChild(CLASS_INSTANCE_ITEMS);
			String classInstanceName = eClassInstance
					.getChildText(CLASS_INSTANCE_NAME);
			ClassInstance classInstance = modelInstance.getClassInstances()
					.get(classInstanceName);
			for (Object oRel : eItems
					.getChildren(CLASS_INSTANCE_SINGLE_RELATION)) {
				Element eClassInstanceRelation = (Element) oRel;
				String relationName = eClassInstanceRelation
						.getChildText(RELATION_NAME);
				String relationTarget = eClassInstanceRelation
						.getChildText(RELATION_TARGET);
				classInstance.setLink(relationName, relationTarget);
			}
			for (Object oRel : eItems
					.getChildren(CLASS_INSTANCE_MULTI_RELATION)) {
				Element eClassInstanceRelation = (Element) oRel;
				String relationName = eClassInstanceRelation
						.getChildText(RELATION_NAME);
				Element eTargets = eClassInstanceRelation
						.getChild(RELATION_TARGETS);
				for (Object oTgt : eTargets.getChildren(RELATION_TARGET)) {
					Element eTarget = (Element) oTgt;
					String relationTarget = eTarget.getText();
					classInstance.setLink(relationName, relationTarget);
				}
			}
		}

		// Load all scenarios
		Element eScenarios = eRoot.getChild(SCENARIOS);
		Element eScenarioFiles = eScenarios.getChild(SCENARIOS_FILES);
		for (Object obj : eScenarioFiles.getChildren(SCENARIO_FILE_NAME)) {
			Element eScenarioFile = (Element) obj;
			String scenarioFileName = eScenarioFile.getText();
			File scenarioFile = new File(fileName.getParent(), scenarioFileName);
			MetaModelScenarioStorer metaModelScenarioStorer = new JDOMMetaModelScenarioStorer();
			MetaModelScenario scenario = metaModelScenarioStorer.loadFromFile(
					scenarioFile, metaModel);
			metaModel.putScenario(scenario);
		}
		Element eConnects = eScenarios.getChild(SCENARIO_CONNECTS);
		for (Object obj : eConnects.getChildren(SCENARIO_CONNECT)) {
			Element eConnect = (Element) obj;
			ClassInstance instance = modelInstance.getClassInstances().get(
					eConnect.getChildText(SCENARIO_CONNECT_INSTANCE));
			instance.setScenarioConnection(eConnect.getChildText(SCENARIO),
					eConnect.getChildText(SCENARIO_CONNECT_NAME));
		}
		return modelInstance;
	}

	public void saveToFile(JynaModel model, File fileName) throws Exception {
		saveToFile((MetaModelInstance) model, fileName);
	}

	@Override
	public JynaModel load(URI modelURI) throws Exception {
		return loadFromFile(new File(modelURI));
	}

	@Override
	public void save(JynaModel model, URI modelURI) throws Exception {
		saveToFile(model, new File(modelURI));
	}
}
