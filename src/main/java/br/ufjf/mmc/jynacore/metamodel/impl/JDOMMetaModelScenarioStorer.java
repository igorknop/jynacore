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
package br.ufjf.mmc.jynacore.metamodel.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import br.ufjf.mmc.jynacore.expression.impl.ExpressionToMathMLConverter;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassTable;
import br.ufjf.mmc.jynacore.metamodel.MetaModelExternalClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenario;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioAffect;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConnection;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioConstraint;
import br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer;

/**
 * @author Knop
 * 
 */
public class JDOMMetaModelScenarioStorer implements MetaModelScenarioStorer {

	private static final String SCENARIO = "scenario";
	private static final String SCENARIO_NAME = "name";
	private static final String METAMODEL_NAME = "metaModelName";
	private static final String CONNECTIONS = "connections";
	private static final String CONNECTION = "connection";
	private static final String CONNECTION_NAME = "name";
	private static final String CONNECTION_CONSTRAINTS = "constraints";
	private static final String CONNECTION_CONSTRAINT = "constraint";
	private static final String CONSTRAINT_RELATION = "relation";
	private static final String CONSTRAINT_SCENARIO = "scenario";
	private static final String CONSTRAINT_CONNECTION = "connection";
	private static final String METAMODEL_CLASS_NAME = "className";
	private static final String CLASS_INSTANCE_ITEMS = "classInstanceItems";
	private static final String FINITE_STOCK = "finiteStock";
	private static final String FINITE_STOCK_NAME = "name";
	private static final String INITIAL_VALUE = "initialValue";
	private static final String RATE = "rate";
	private static final String RATE_NAME = "name";
	private static final String RATE_TARGET = "target";
	private static final String RATE_SOURCE = "source";
	private static final String EXPRESSION = "expression";
	private static final String AUXILIARY = "auxiliary";
	private static final String AUXILIARY_NAME = "name";
	private static final String PROPERTY = "property";
	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_VALUE = "value";
	private static final String AFFECTS = "affects";
	private static final String AFFECT = "affect";
	private static final String AFFECT_NAME = "name";
	private static final String TABLE = "table";
	private static final String TABLE_NAME = "name";
	private static final String TABLE_VALUES = "values";
	private static final String TABLE_VALUE = "value";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer#loadFromFile(java
	 * .io.File)
	 */
	@Override
	public MetaModelScenario loadFromFile(File fileName, MetaModel metaModel)
			throws Exception {
		MetaModelScenario scenario = new DefaultMetaModelScenario();
		scenario.setMetaModel(metaModel);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(fileName);

		Element eRoot = doc.getRootElement();
		scenario.setName(eRoot.getChildText(SCENARIO_NAME));
		scenario.setMetaModelName(eRoot.getChildText(METAMODEL_NAME));

		Element eConnections = eRoot.getChild(CONNECTIONS);
		for (Object obj : eConnections.getChildren(CONNECTION)) {
			Element eConnection = (Element) obj;
			MetaModelScenarioConnection connection = new DefaultMetaModelScenarioConnection();
			connection.setName(eConnection.getChildText(CONNECTION_NAME));
			connection.setClassName(eConnection
					.getChildText(METAMODEL_CLASS_NAME));
			scenario.put(connection.getName(), connection);
			Element eClassItems = eConnection.getChild(CLASS_INSTANCE_ITEMS);
			if (eClassItems != null) {
				for (Object oeLevel : eClassItems.getChildren(FINITE_STOCK)) {
					Element eLevel = (Element) oeLevel;
					MetaModelClassStock oLevel = new DefaultMetaModelClassStock();
					oLevel.setName(eLevel.getChildText(FINITE_STOCK_NAME));
					Element eExpr = eLevel.getChild(INITIAL_VALUE);
					oLevel.setExpression(ExpressionToMathMLConverter
							.readFromMathML(eExpr.getChild(
									ExpressionToMathMLConverter.MATHML_ROOT,
									ExpressionToMathMLConverter.mathmlNS)));

					connection.getClassItems().put(oLevel.getName(), oLevel);
				}
				for (Object oeRate : eClassItems.getChildren(RATE)) {
					Element eRate = (Element) oeRate;
					MetaModelClassRate oRate = new DefaultMetaModelClassRate();
					oRate.setName(eRate.getChildText(RATE_NAME));
					String affstr = eRate.getChildText(RATE_TARGET);
					if (affstr != null) {
						if (affstr.contains(".")) {
							String[] tokens = affstr.split("\\.");
							String relationName = tokens[0];
							String targetName = tokens[1];
							MetaModelExternalClassItem affected = new DefaultMetaModelClassExternalItem();
							affected.SetRelationName(relationName);
							affected.setName(targetName);
							oRate.setTarget(affected);
						} else {
							oRate.setTarget(connection.getClassItems().get(
									affstr));

							if (oRate.getTarget() == null) {
								MetaModelClass mmClass = (MetaModelClass) metaModel
										.get(connection.getClassName());
								oRate.setTarget(mmClass.get(affstr));
								if (oRate.getTarget() == null) {
									throw new Exception(
											"Invalid rate affected item! Rate '"
													+ oRate.getName()
													+ " 'Affected: '" + affstr
													+ "' in class '"
													+ mmClass.getName() + "'");
								}
							}
						}
					}
					affstr = eRate.getChildText(RATE_SOURCE);
					if (affstr != null) {
						if (affstr.contains(".")) {
							String[] tokens = affstr.split("\\.");
							String relationName = tokens[0];
							String sourceName = tokens[1];
							MetaModelExternalClassItem affected = new DefaultMetaModelClassExternalItem();
							affected.SetRelationName(relationName);
							affected.setName(sourceName);
							oRate.setSource(affected);
						} else {
							oRate.setSource(connection.getClassItems().get(
									affstr));

							if (oRate.getSource() == null) {
								MetaModelClass mmClass = (MetaModelClass) metaModel
										.get(connection.getClassName());
								oRate.setSource(mmClass.get(affstr));
								if (oRate.getSource() == null) {
									throw new Exception(
											"Invalid rate source item! Rate '"
													+ oRate.getName()
													+ " 'source: '" + affstr
													+ "' in class '"
													+ mmClass.getName() + "'");
								}
							}
						}
					}
					Element eExpr = eRate.getChild(EXPRESSION);
					oRate.setExpression(ExpressionToMathMLConverter
							.readFromMathML(eExpr.getChild(
									ExpressionToMathMLConverter.MATHML_ROOT,
									ExpressionToMathMLConverter.mathmlNS)));
					connection.getClassItems().put(oRate.getName(), oRate);
				}
				for (Object oeAuxiliary : eClassItems.getChildren(AUXILIARY)) {
					Element eAuxiliary = (Element) oeAuxiliary;
					MetaModelClassAuxiliary oAuxiliary = new DefaultMetaModelClassAuxiliary();
					oAuxiliary.setName(eAuxiliary.getChildText(AUXILIARY_NAME));
					Element eExpr = eAuxiliary.getChild(EXPRESSION);
					oAuxiliary.setExpression(ExpressionToMathMLConverter
							.readFromMathML(eExpr.getChild(
									ExpressionToMathMLConverter.MATHML_ROOT,
									ExpressionToMathMLConverter.mathmlNS)));
					connection.getClassItems().put(oAuxiliary.getName(),
							oAuxiliary);
				}
				for (Object oeAuxiliary : eClassItems.getChildren(PROPERTY)) {
					Element eAuxiliary = (Element) oeAuxiliary;
					MetaModelClassProperty oAuxiliary = new DefaultMetaModelClassProperty();
					oAuxiliary.setName(eAuxiliary.getChildText(PROPERTY_NAME));
					oAuxiliary.setDefaultValue(Double.valueOf(eAuxiliary
							.getChildText(PROPERTY_VALUE)));
					connection.getClassItems().put(oAuxiliary.getName(),
							oAuxiliary);
				}
				for (Object oeTable : eClassItems.getChildren(TABLE)) {
					Element eTable = (Element) oeTable;
					MetaModelClassTable oTable = new DefaultMetaModelClassTable();
					oTable.setName(eTable.getChildText(TABLE_NAME));
					Element eVls = eTable.getChild(TABLE_VALUES);
					if (eVls != null) {
						for (Object ovl : eVls.getChildren(TABLE_VALUE)) {
							Element evl = (Element) ovl;
							oTable.getValues().add(
									Double.valueOf(evl.getText()));
						}
					}
					connection.getClassItems().put(oTable.getName(), oTable);
				}
			}
			Element eAffects = eConnection.getChild(AFFECTS);
			if (eAffects != null) {
				for (Object oeAffect : eAffects.getChildren(AFFECT)) {
					Element eAffect = (Element) oeAffect;
					MetaModelScenarioAffect oAffect = new DefaultMetaModelScenarioAffect();
					oAffect.setName(eAffect.getChildText(AFFECT_NAME));
					Element eExpr = eAffect.getChild(EXPRESSION);
					oAffect.setExpression(ExpressionToMathMLConverter
							.readFromMathML(eExpr.getChild(
									ExpressionToMathMLConverter.MATHML_ROOT,
									ExpressionToMathMLConverter.mathmlNS)));
					connection.getAffectList().add(oAffect);
				}
			}
			Element eConstraints = eConnection.getChild(CONNECTION_CONSTRAINTS);
			if (eConstraints != null) {
				for (Object oeConstraint : eConstraints
						.getChildren(CONNECTION_CONSTRAINT)) {
					Element eConstraint = (Element) oeConstraint;
					MetaModelScenarioConstraint oConstraint = new DefaultMetaModelScenarioConstraint();
					oConstraint.setRelationName(eConstraint
							.getChildText(CONSTRAINT_RELATION));
					oConstraint.setScenarioName(eConstraint
							.getChildText(CONSTRAINT_SCENARIO));
					oConstraint.setConnectionName(eConstraint
							.getChildText(CONSTRAINT_CONNECTION));
					connection.getConstraints().add(oConstraint);
				}
			}

		}

		return scenario;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufjf.mmc.jynacore.metamodel.MetaModelScenarioStorer#saveToFile(br.
	 * ufjf.mmc.jynacore.metamodel.MetaModelScenario, java.io.File)
	 */
	@Override
	public void saveToFile(MetaModelScenario scenario, File fileName)
			throws Exception {
		Document doc = new Document();
		Element eRoot = new Element(SCENARIO);
		doc.setRootElement(eRoot);

		Element eScenarioName = new Element(SCENARIO_NAME);
		eScenarioName.setText(scenario.getName());
		eRoot.addContent(eScenarioName);

		Element eMetaModelName = new Element(METAMODEL_NAME);
		eMetaModelName.setText(scenario.getMetaModelName());
		eRoot.addContent(eMetaModelName);

		Element eConnections = new Element(CONNECTIONS);
		eRoot.addContent(eConnections);
		for (Entry<String, MetaModelScenarioConnection> entryConnection : scenario
				.entrySet()) {
			MetaModelScenarioConnection connection = entryConnection.getValue();
			Element eConnection = new Element(CONNECTION);
			eConnection.addContent(new Element(CONNECTION_NAME)
					.setText(connection.getName()));
			eConnection.addContent(new Element(METAMODEL_CLASS_NAME)
					.setText(connection.getClassName()));
			Element eClassItems = new Element(CLASS_INSTANCE_ITEMS);
			eConnection.addContent(eClassItems);
			for (Entry<String, MetaModelClassItem> entryClassItems : connection
					.getClassItems().entrySet()) {
				MetaModelClassItem mmClassItem = entryClassItems.getValue();
				if (mmClassItem instanceof MetaModelClassStock) {
					MetaModelClassStock fobj = (MetaModelClassStock) mmClassItem;
					Element eObj = new Element(FINITE_STOCK);
					eObj.addContent(new Element(FINITE_STOCK_NAME).setText(fobj
							.getName()));
					eObj.addContent(new Element(INITIAL_VALUE)
							.addContent(ExpressionToMathMLConverter
									.convertToMathml(fobj.getExpression())));

					eClassItems.addContent(eObj);
				} else

				if (mmClassItem instanceof MetaModelClassAuxiliary) {
					MetaModelClassAuxiliary fobj = (MetaModelClassAuxiliary) mmClassItem;
					Element eObj = new Element(AUXILIARY);
					eObj.addContent(new Element(AUXILIARY_NAME).setText(fobj
							.getName()));
					eObj.addContent(new Element(EXPRESSION)
							.addContent(ExpressionToMathMLConverter
									.convertToMathml(fobj.getExpression())));
					eClassItems.addContent(eObj);
				} else if (mmClassItem instanceof MetaModelClassProperty) {
					MetaModelClassProperty fobj = (MetaModelClassProperty) mmClassItem;
					Element eObj = new Element(PROPERTY);
					eObj.addContent(new Element(PROPERTY_NAME).setText(fobj
							.getName()));
					eObj.addContent(new Element(PROPERTY_VALUE).setText(fobj
							.getDefaultValue().toString()));
					eClassItems.addContent(eObj);
				} else if (mmClassItem instanceof MetaModelClassRate) {
					MetaModelClassRate fobj = (MetaModelClassRate) mmClassItem;
					Element eObj = new Element(RATE);
					eObj.addContent(new Element(RATE_NAME).setText(fobj
							.getName()));
					eObj.addContent(new Element(EXPRESSION)
							.addContent(ExpressionToMathMLConverter
									.convertToMathml(fobj.getExpression())));
					eObj.addContent(new Element(RATE_TARGET).setText(fobj
							.getTarget().getName()));
					eClassItems.addContent(eObj);
				} else if (mmClassItem instanceof MetaModelClassTable) {
					MetaModelClassTable fobj = (DefaultMetaModelClassTable) mmClassItem;
					Element eObj = new Element(TABLE);
					eObj.addContent(new Element(TABLE_NAME).setText(fobj
							.getName()));
					Element eVls = new Element(TABLE_VALUES);
					for (Double d : fobj.getValues()) {
						eVls.addContent(new Element(TABLE_VALUE).addContent(d
								.toString()));
					}
					eObj.addContent(eVls);
					eClassItems.addContent(eObj);
				}
			}
			Element eAffects = new Element(AFFECTS);
			eConnection.addContent(eAffects);
			for (MetaModelScenarioAffect affect : connection.getAffectList()) {
				Element eAffect = new Element(AFFECT);
				eAffect.addContent(new Element(AFFECT_NAME).setText(affect
						.getName()));
				eAffect.addContent(new Element(EXPRESSION)
						.addContent(ExpressionToMathMLConverter
								.convertToMathml(affect.getExpression())));
				eAffects.addContent(eAffect);
			}
			Element eConstraints = new Element(CONNECTION_CONSTRAINTS);
			eConnection.addContent(eConstraints);
			for (MetaModelScenarioConstraint constraint : connection
					.getConstraints()) {
				Element eConstraint = new Element(CONNECTION_CONSTRAINT);
				eConstraint.addContent(new Element(CONSTRAINT_SCENARIO)
						.setText(constraint.getScenarioName()));
				eConstraint.addContent(new Element(CONSTRAINT_CONNECTION)
						.setText(constraint.getConnectionName()));
				if (constraint.getRelationName() != null) {
					eConstraint.addContent(new Element(CONSTRAINT_RELATION)
							.setText(constraint.getRelationName()));
				}
				eAffects.addContent(eConstraint);
			}
			eConnections.addContent(eConnection);
		}

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		FileOutputStream fo = new FileOutputStream(fileName);
		outputter.output(doc, fo);
		fo.close();
	}

}
