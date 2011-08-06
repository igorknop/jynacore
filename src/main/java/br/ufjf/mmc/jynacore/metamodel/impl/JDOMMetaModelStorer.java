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
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassTable;
import br.ufjf.mmc.jynacore.metamodel.MetaModelExternalClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;

public class JDOMMetaModelStorer implements MetaModelStorer {

	public static final String EXPRESSION = "expression";
	public static final String INFINITE_STOCK = "infiniteStock";
	public static final String AUXILIARY = "auxiliary";
	public static final String INITIAL_VALUE = "initialValue";
	public static final String NAME = "name";
	public static final String FINITE_STOCK = "finiteStock";
	public static final String INFORMATIONS = "informations";
	public static final String AUXILIARIES = "auxiliaries";
	public static final String RATES = "rates";
	public static final String INFINITE_STOCKS = "infiniteStocks";
	public static final String STOCKS = "stocks";
	private static final String META_MODEL_NAME = "name";
	public static final String META_MODEL = "metaModel";
	private static final String RATE = "rate";
	private static final String RATE_TARGET = "target";
	private static final String SOURCE = "source";
	private static final String CLASSES = "classes";
	private static final String RELATIONS = "relations";
	private static final String CLASS = "class";
	private static final String SINGLE_RELATION = "singleRelation";
	private static final String MULTI_RELATION = "multiRelation";
	private static final String TARGET = "target";
	private static final String TARGET_ROLE = "targetRole";
	private static final String PROPERTY = "property";
	private static final String VALUE = "value";
	private static final String RATE_SOURCE = "source";
	private static final String TABLE = "table";
	private static final String TABLE_NAME = "name";
	private static final String TABLE_VALUES = "values";
	private static final String TABLE_VALUE = "value";
	private static final String CLASS_ITEMS = "items";
	private Document doc;
	private Element eRoot;
	private Element eMetaModelName;
	private Element eClasses;
	private Element eRelations;

	@Override
	public MetaModel loadFromFile(File fileName) throws Exception {
		MetaModel model = new DefaultMetaModel();

		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(fileName);

		eRoot = doc.getRootElement();
		model.setName(eRoot.getChildText(META_MODEL_NAME));

		eClasses = eRoot.getChild(CLASSES);
		for (Object obj : eClasses.getChildren(CLASS)) {
			Element eClass = (Element) obj;
			MetaModelClass mmClass = new DefaultMetaModelClass();
			mmClass.setName(eClass.getChildText(NAME));
			model.put(mmClass.getName(), mmClass);
			Element eClassItems = eClass.getChild(CLASS_ITEMS);
//			Element eLevels = eClass.getChild(STOCKS);
			for (Object oeLevel : eClassItems.getChildren(FINITE_STOCK)) {
//				for (Object oeLevel : eLevels.getChildren(FINITE_STOCK)) {
				Element eLevel = (Element) oeLevel;
				MetaModelClassStock oLevel = new DefaultMetaModelClassStock();
				oLevel.setName(eLevel.getChildText(NAME));
				Element eExpr = eLevel.getChild(INITIAL_VALUE);
				oLevel.setExpression(ExpressionToMathMLConverter
						.readFromMathML(eExpr.getChild(
								ExpressionToMathMLConverter.MATHML_ROOT,
								ExpressionToMathMLConverter.mathmlNS)));

				mmClass.put(oLevel);
			}
			for (Object oeRate : eClassItems.getChildren(RATE)) {
//				Element eRates = eClass.getChild(RATES);
//				for (Object oeRate : eRates.getChildren(RATE)) {
				Element eRate = (Element) oeRate;
				MetaModelClassRate oRate = new DefaultMetaModelClassRate();
				oRate.setName(eRate.getChildText(NAME));
				// Parse Target
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
						oRate.setTarget(mmClass.get(affstr));
					}
					if (oRate.getTarget() == null)
						throw new Exception(
								"Invalid rate affected item! Rate '"
										+ oRate.getName() + " 'Affected: '"
										+ affstr + "' in class '"
										+ mmClass.getName() + "'");
				}
				// Parse Source
				affstr = eRate.getChildText(RATE_SOURCE);
				if (affstr != null) {
					if (affstr.contains(".")) {
						String[] tokens = affstr.split("\\.");
						String relationName = tokens[0];
						String targetName = tokens[1];
						MetaModelExternalClassItem affected = new DefaultMetaModelClassExternalItem();
						affected.SetRelationName(relationName);
						affected.setName(targetName);
						oRate.setSource(affected);
					} else {
						oRate.setSource(mmClass.get(affstr));
					}
					if (oRate.getSource() == null)
						throw new Exception(
								"Invalid rate affected item! Rate '"
										+ oRate.getName() + " 'Affected: '"
										+ affstr + "' in class '"
										+ mmClass.getName() + "'");
				}
				Element eExpr = eRate.getChild(EXPRESSION);
				oRate.setExpression(ExpressionToMathMLConverter
						.readFromMathML(eExpr.getChild(
								ExpressionToMathMLConverter.MATHML_ROOT,
								ExpressionToMathMLConverter.mathmlNS)));
				mmClass.put(oRate.getName(), oRate);
			}
//			Element eAuxiliaries = eClass.getChild(AUXILIARIES);
			for (Object oeAuxiliary : eClassItems.getChildren(AUXILIARY)) {
//				for (Object oeAuxiliary : eAuxiliaries.getChildren(AUXILIARY)) {
				Element eAuxiliary = (Element) oeAuxiliary;
				MetaModelClassAuxiliary oAuxiliary = new DefaultMetaModelClassAuxiliary();
				oAuxiliary.setName(eAuxiliary.getChildText(NAME));
				Element eExpr = eAuxiliary.getChild(EXPRESSION);
				oAuxiliary.setExpression(ExpressionToMathMLConverter
						.readFromMathML(eExpr.getChild(
								ExpressionToMathMLConverter.MATHML_ROOT,
								ExpressionToMathMLConverter.mathmlNS)));
				mmClass.put(oAuxiliary.getName(), oAuxiliary);
			}
			for (Object oeAuxiliary : eClassItems.getChildren(PROPERTY)) {
//				for (Object oeAuxiliary : eAuxiliaries.getChildren(PROPERTY)) {
				Element eAuxiliary = (Element) oeAuxiliary;
				MetaModelClassProperty oAuxiliary = new DefaultMetaModelClassProperty();
				oAuxiliary.setName(eAuxiliary.getChildText(NAME));
				oAuxiliary.setDefaultValue(Double.valueOf(eAuxiliary
						.getChildText(VALUE)));
				mmClass.put(oAuxiliary.getName(), oAuxiliary);
			}
			for (Object oeTable : eClassItems.getChildren(TABLE)) {
//				for (Object oeTable : eAuxiliaries.getChildren(TABLE)) {
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
				mmClass.put(oTable.getName(), oTable);
			}


		}

		eRelations = eRoot.getChild(RELATIONS);
		for (Object obj : eRelations.getChildren(SINGLE_RELATION)) {
			Element eSRel = (Element) obj;
			MetaModelSingleRelation mmsr = new DefaultMetaModelSingleRelation();
			mmsr.setName(eSRel.getChildText(NAME));
			mmsr.setSource((MetaModelClass) model.get(eSRel
					.getChildText(SOURCE)));
			mmsr.setTarget((MetaModelClass) model.get(eSRel
					.getChildText(TARGET)));
			mmsr.setTargetRole(eSRel.getChildText(TARGET_ROLE));
			model.put(mmsr.getName(), mmsr);
		}
		for (Object obj : eRelations.getChildren(MULTI_RELATION)) {
			Element eSRel = (Element) obj;
			MetaModelMultiRelation mmmr = new DefaultMetaModelMultiRelation();
			mmmr.setName(eSRel.getChildText(NAME));
			mmmr.setSource((MetaModelClass) model.get(eSRel
					.getChildText(SOURCE)));
			mmmr.setTarget((MetaModelClass) model.get(eSRel
					.getChildText(TARGET)));
			mmmr.setTargetRole(eSRel.getChildText(TARGET_ROLE));
			model.put(mmmr.getName(), mmmr);
		}

		return model;
	}

	@Override
	public void saveToFile(MetaModel model, File fileName) throws Exception {

		doc = new Document();
		eRoot = new Element(META_MODEL);
		doc.setRootElement(eRoot);
		eMetaModelName = new Element(META_MODEL_NAME);
		eMetaModelName.setText(model.getName());
		eRoot.addContent(eMetaModelName);

		eClasses = new Element(CLASSES);
		eRoot.addContent(eClasses);

		eRelations = new Element(RELATIONS);
		eRoot.addContent(eRelations);

		for (String k : model.keySet()) {
			MetaModelItem mmi = model.get(k);
			if (mmi instanceof MetaModelClass) {
				eClasses
						.addContent(getMetaModelClassElement((MetaModelClass) mmi));
				continue;
			}
			if (mmi instanceof MetaModelRelation) {
				eRelations
						.addContent(getMetaModelRelationElement((MetaModelRelation) mmi));
				continue;
			}

		}

		XMLOutputter outputter = new XMLOutputter();

		outputter.setFormat(Format.getPrettyFormat());

		FileOutputStream fo = new FileOutputStream(fileName);
		outputter.output(doc, fo);
		fo.close();
	}

	private Element getMetaModelRelationElement(MetaModelRelation mmr) {
		Element eRelation;
		if (mmr instanceof MetaModelSingleRelation) {
			eRelation = new Element(SINGLE_RELATION);
		} else {
			eRelation = new Element(MULTI_RELATION);
		}

		eRelation.addContent(new Element(NAME).setText(mmr.getName()));
		Element eSource = new Element(SOURCE);
		eSource.setText(mmr.getSource().getName());
		eRelation.addContent(eSource);
		Element eTarget = new Element(TARGET);
		eTarget.setText(mmr.getTarget().getName());
		eRelation.addContent(eTarget);
		if (mmr.getTargetRole() != null && !mmr.getTargetRole().isEmpty()) {
			eRelation.addContent(new Element(TARGET_ROLE).setText(mmr
					.getTargetRole()));
		}
		return eRelation;
	}

	private Element getMetaModelClassElement(MetaModelClass mmClass)
			throws Exception {
		Element eClass = new Element(CLASS);

		// eClass.addContent(new Element(NAME).setText(mmClass.getName()));
		eClass.addContent(new Element(NAME).setText(mmClass.getName()));
//		Element eLevels = new Element(STOCKS);
//		eClass.addContent(eLevels);

//		Element eInfiniteLevels = new Element(INFINITE_STOCKS);
//		eClass.addContent(eInfiniteLevels);

//		Element eRates = new Element(RATES);
//		eClass.addContent(eRates);

//		Element eAuxiliares = new Element(AUXILIARIES);
//		eClass.addContent(eAuxiliares);

//		Element eInformations = new Element(INFORMATIONS);
//		eClass.addContent(eInformations);
		Element eClassItems = new Element(CLASS_ITEMS);
		eClass.addContent(eClassItems);

		for (Entry<String, MetaModelClassItem> entry : mmClass.entrySet()) {
			MetaModelClassItem mmci = entry.getValue();
			if (mmci instanceof MetaModelClassStock) {
				MetaModelClassStock fobj = (MetaModelClassStock) mmci;
				Element eObj = new Element(FINITE_STOCK);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eObj.addContent(new Element(INITIAL_VALUE)
						.addContent(ExpressionToMathMLConverter
								.convertToMathml(fobj.getExpression())));

				eClassItems.addContent(eObj);
//				eLevels.addContent(eObj);
			} else

			if (mmci instanceof MetaModelClassAuxiliary) {
				MetaModelClassAuxiliary fobj = (MetaModelClassAuxiliary) mmci;
				Element eObj = new Element(AUXILIARY);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eObj.addContent(new Element(EXPRESSION)
						.addContent(ExpressionToMathMLConverter
								.convertToMathml(fobj.getExpression())));
//				eAuxiliares.addContent(eObj);
				eClassItems.addContent(eObj);
			} else if (mmci instanceof MetaModelClassProperty) {
				MetaModelClassProperty fobj = (MetaModelClassProperty) mmci;
				Element eObj = new Element(PROPERTY);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eObj.addContent(new Element(VALUE).setText(fobj
						.getDefaultValue().toString()));
//				eAuxiliares.addContent(eObj);
				eClassItems.addContent(eObj);
			} else if (mmci instanceof MetaModelClassRate) {
				MetaModelClassRate fobj = (MetaModelClassRate) mmci;
				Element eObj = new Element(RATE);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eObj.addContent(new Element(EXPRESSION)
						.addContent(ExpressionToMathMLConverter
								.convertToMathml(fobj.getExpression())));
				if (fobj.getTarget() != null)
					eObj.addContent(new Element(RATE_TARGET).setText(fobj.getTarget()
							.getName()));
				if (fobj.getSource() != null)
					eObj.addContent(new Element(RATE_SOURCE).setText(fobj.getSource()
							.getName()));
//				eRates.addContent(eObj);
				eClassItems.addContent(eObj);
			}else if (mmci instanceof MetaModelClassTable) {
				MetaModelClassTable fobj = (DefaultMetaModelClassTable) mmci;
				Element eObj = new Element(TABLE);
				eObj.addContent(new Element(TABLE_NAME).setText(fobj
						.getName()));
				Element eVls = new Element(TABLE_VALUES);
				for (Double d : fobj.getValues()) {
					eVls.addContent(new Element(TABLE_VALUE).addContent(d
							.toString()));
				}
				eObj.addContent(eVls);
//				eAuxiliares.addContent(eObj);
				eClassItems.addContent(eObj);
			}

		}

		return eClass;
	}
}
