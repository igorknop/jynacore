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
package br.ufjf.mmc.jynacore.systemdynamics.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import br.ufjf.mmc.jynacore.JynaModel;
import br.ufjf.mmc.jynacore.expression.impl.ExpressionToMathMLConverter;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.InformationConsumer;
import br.ufjf.mmc.jynacore.systemdynamics.InformationSource;
import br.ufjf.mmc.jynacore.systemdynamics.Stock;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModelStorer;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsItem;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;

public class DefaultSystemDynamicsModelStorerJDOM implements SystemDynamicsModelStorer {

	//public static final String EXPRESSION = "expression";
	public static final String INFINITE_STOCK = "infiniteStock";
	public static final String AUXILIARY = "auxiliary";
	public static final String INITIAL_VALUES = "initialValues";
	public static final String NAME = "name";
	public static final String FINITE_STOCK = "finiteStock";
	public static final String INFORMATIONS = "informations";
	public static final String AUXILIARIES = "auxiliaries";
	public static final String RATES = "rates";
	public static final String INFINITE_STOCKS = "infiniteStocks";
	public static final String STOCKS = "stocks";
	private static final String MODEL_NAME = "name";
	public static final String SYSTEM_DYNAMICS_MODEL = "systemDynamicsModel";
	private static final String RATE = "rate";
	private static final String FROM = "source";
	private static final String TO = "target";
	private static final String INFORMATION = "information";
	private static final String SOURCE = "source";
	private static final String CONSUMER = "consumer";
	private static final String EXPRESSION = "expression";
	private static final String INITIAL_VALUE = "initialValue";
	private static final String MATHML_NS_URI = "http://www.w3.org/1998/Math/MathML";
	private static final String MATHML_NS_PREFIX = "m";
	private static final String MATHML_ROOT = "math";
	private static final Namespace mathmlNS = Namespace.getNamespace(
			MATHML_NS_PREFIX, MATHML_NS_URI);
	private Document doc;
	private Element eRoot;
	private Element eStocks;
	private Element eRates;
	private Element eAuxiliares;
	private Element eInformations;
	private Element eInfiniteLevels;
	private Element eModelName;

	//@Override
	public SystemDynamicsModel loadFromFile(File fileName) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(fileName);
		SystemDynamicsModel model = new DefaultSystemDynamicsModel();
		eRoot = doc.getRootElement();
		model.setName(eRoot.getChildText(MODEL_NAME));
		eStocks = eRoot.getChild(STOCKS);
		for (Object obj : eStocks.getChildren(FINITE_STOCK)) {
			Element eObj = (Element) obj;
			FiniteStock oObj = new DefaultFiniteStock();
			oObj.setName(eObj.getChildText(NAME));
//			oObj.setInitialValue(Double.valueOf(eObj
//					.getChildText(INITIAL_VALUES)));
			Element eExpr = eObj.getChild(INITIAL_VALUE);
			oObj.setInitialValue(ExpressionToMathMLConverter
					.readFromMathML(eExpr.getChild(MATHML_ROOT,mathmlNS)));
			model.put(oObj.getName(), oObj);
		}
		eInfiniteLevels = eRoot.getChild(INFINITE_STOCKS);
		for (Object obj : eInfiniteLevels.getChildren(INFINITE_STOCK)) {
			Element eObj = (Element) obj;
			InfiniteStock oObj = new DefaultInfiniteStock();
			oObj.setName(eObj.getChildText(NAME));
			model.put(oObj.getName(), oObj);
		}
		eRates = eRoot.getChild(RATES);
		for (Object obj : eRates.getChildren(RATE)) {
			Element eObj = (Element) obj;
			Rate oObj = new DefaultRate();
			oObj.setName(eObj.getChildText(NAME));
//			oObj.setExpression(eObj.getChildText(EXPRESSION));
			Element eExpr = eObj.getChild(EXPRESSION);
			oObj.setExpression(ExpressionToMathMLConverter
					.readFromMathML(eExpr.getChild(MATHML_ROOT,mathmlNS)));
			model.put(oObj.getName(), oObj);

			String sFrom = eObj.getChildText(FROM);
			SystemDynamicsItem olvl = model.get(sFrom);
			if (olvl instanceof Stock) {
				oObj.setSource((Stock) olvl);
			} else
				throw new Exception("Invalid source Stock " + sFrom
						+ " on Rate" + oObj.getName());

			String sTo = eObj.getChildText(TO);
			SystemDynamicsItem flvl = model.get(sTo);
			if (flvl instanceof Stock) {
				oObj.setTarget((Stock) flvl);
			} else
				throw new Exception("Invalid target Stock " + sTo + " on Rate"
						+ oObj.getName());
		}
		eAuxiliares = eRoot.getChild(AUXILIARIES);
		for (Object obj : eAuxiliares.getChildren(AUXILIARY)) {
			Element eObj = (Element) obj;
			Variable oObj = new DefaultVariable();
			oObj.setName(eObj.getChildText(NAME));
//			oObj.setExpression(eObj.getChildText(EXPRESSION));
			Element eExpr = eObj.getChild(EXPRESSION);
			oObj.setExpression(ExpressionToMathMLConverter
					.readFromMathML(eExpr.getChild(MATHML_ROOT,mathmlNS)));
			model.put(oObj.getName(), oObj);
		}
		eInformations = eRoot.getChild(INFORMATIONS);
		for (Object obj : eInformations.getChildren(INFORMATION)) {
			Element eObj = (Element) obj;
			String source = eObj.getChildText(SOURCE);
			String consumer = eObj.getChildText(CONSUMER);
			InformationSource isObj;
			InformationConsumer icObj;

			SystemDynamicsItem sdObj = model.get(source);
			if (sdObj instanceof InformationSource) {
				isObj = (InformationSource) sdObj;
			} else
				throw new Exception("Invalid Source: " + source);

			SystemDynamicsItem sdObj2 = model.get(consumer);
			if (sdObj2 instanceof InformationConsumer) {
				icObj = (InformationConsumer) sdObj2;
			} else
				throw new Exception("Invalid Consumer: " + consumer);

			Information oObj = new DefaultInformation(isObj, icObj);
			oObj.setName(eObj.getChildText(NAME));
			model.put(oObj.getName(), oObj);
		}

		return model;
	}

	@Override
	public void saveToFile(SystemDynamicsModel model, File fileName)
			throws Exception {

		doc = new Document();
		eRoot = new Element(SYSTEM_DYNAMICS_MODEL);
		doc.setRootElement(eRoot);
		eModelName = new Element(MODEL_NAME);
		eModelName.setText(model.getName());

		eStocks = new Element(STOCKS);
		eRoot.addContent(eStocks);

		eInfiniteLevels = new Element(INFINITE_STOCKS);
		eRoot.addContent(eInfiniteLevels);

		eRates = new Element(RATES);
		eRoot.addContent(eRates);

		eAuxiliares = new Element(AUXILIARIES);
		eRoot.addContent(eAuxiliares);

		eInformations = new Element(INFORMATIONS);
		eRoot.addContent(eInformations);

		for (SystemDynamicsItem obj : model.getItems().values()) {
			if (obj instanceof FiniteStock) {
				FiniteStock fobj = (FiniteStock) obj;
				Element eObj = new Element(FINITE_STOCK);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
//				eObj.addContent(new Element(INITIAL_VALUES).setText(fobj
//						.getInitialValue().toString()));
				eObj.addContent(new Element(INITIAL_VALUE)
						.addContent(ExpressionToMathMLConverter
								.convertToMathml(fobj.getInitialValue())));

				eStocks.addContent(eObj);
			}

			if (obj instanceof InfiniteStock) {
				InfiniteStock fobj = (InfiniteStock) obj;
				Element eObj = new Element(INFINITE_STOCK);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eInfiniteLevels.addContent(eObj);
			}

			/*
			 * if (obj instanceof Auxiliary) { Auxiliary fobj = (Auxiliary) obj;
			 * Element eObj = new Element(AUXILIARY); eObj.addContent(new
			 * Element(NAME).setText(fobj.getName())); // if (obj instanceof
			 * Variable) { eObj.addContent(new Element(EXPRESSION)
			 * .setText(((EvaluatedValue) fobj).getExpression())); } else {
			 * eObj.addContent(new Element(EXPRESSION).setText(fobj
			 * .getValue().toString())); } eAuxiliares.addContent(eObj); }
			 */
			if (obj instanceof Variable) {
				Variable fobj = (Variable) obj;
				Element eObj = new Element(AUXILIARY);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
//				eObj.addContent(new Element(EXPRESSION).setText(fobj
//						.getExpression()));
				eObj.addContent(new Element(EXPRESSION).addContent(ExpressionToMathMLConverter
								.convertToMathml(fobj.getExpression())));
				eAuxiliares.addContent(eObj);
			}
			if (obj instanceof Rate) {
				Rate fobj = (Rate) obj;
				Element eObj = new Element(RATE);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
//				eObj.addContent(new Element(EXPRESSION).setText(fobj
//						.getExpression()));
				eObj.addContent(new Element(EXPRESSION).addContent(ExpressionToMathMLConverter
						.convertToMathml(fobj.getExpression())));

				eObj.addContent(new Element(FROM).setText(fobj.getFromLevel()
						.getName()));
				eObj.addContent(new Element(TO).setText(fobj.getToLevel()
						.getName()));
				eRates.addContent(eObj);
			}

			if (obj instanceof Information) {
				Information fobj = (Information) obj;
				Element eObj = new Element(INFORMATION);
				eObj.addContent(new Element(NAME).setText(fobj.getName()));
				eObj.addContent(new Element(SOURCE).setText(fobj.getSource()
						.getName()));
				eObj.addContent(new Element(CONSUMER).setText(fobj
						.getConsumer().getName()));
				eInformations.addContent(eObj);
			}
		}
		XMLOutputter outputter = new XMLOutputter();

		outputter.setFormat(Format.getPrettyFormat());

		FileOutputStream fo = new FileOutputStream(fileName);
		outputter.output(doc, fo);
		fo.close();
	}

	//@Override
	public void saveToFile(JynaModel model, File fileName) throws Exception {
		saveToFile((SystemDynamicsModel) model, fileName);
	}

	@Override
	public JynaModel load(URI modelURI) throws Exception {
		return loadFromFile(new File(modelURI));
	}

	@Override
	public void save(JynaModel model, URI modelURI) throws Exception {
		saveToFile(model,new File(modelURI));
	}
}
