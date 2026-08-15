package br.ufjf.mmc.jynacore.metamodel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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
import br.ufjf.mmc.jynacore.metamodel.MetaModelItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelStorer;

public class YamlMetaModelStorer implements MetaModelStorer {

	@Override
	public MetaModel loadFromFile(File fileName) throws Exception {
		MetaModel model = new DefaultMetaModel();
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(fileName);
		Map<String, Object> root = yaml.load(input);
		input.close();

		if (root == null) {
			return model;
		}

		model.setName((String) root.get("name"));

		// Classes
		List<Map<String, Object>> classesList = (List<Map<String, Object>>) root.get("classes");
		if (classesList != null) {
			for (Map<String, Object> classMap : classesList) {
				MetaModelClass mmClass = new DefaultMetaModelClass();
				mmClass.setName((String) classMap.get("name"));
				model.put(mmClass.getName(), mmClass);

				List<Map<String, Object>> itemsList = (List<Map<String, Object>>) classMap.get("items");
				if (itemsList != null) {
					for (Map<String, Object> itemMap : itemsList) {
						String type = (String) itemMap.get("type");
						if ("finiteStock".equals(type)) {
							MetaModelClassStock stock = new DefaultMetaModelClassStock();
							stock.setName((String) itemMap.get("name"));
							String mathml = (String) itemMap.get("initialValue");
							if (mathml != null) {
								stock.setExpression(parseMathMLString(mathml));
							}
							mmClass.put(stock);
						} else if ("rate".equals(type)) {
							MetaModelClassRate rate = new DefaultMetaModelClassRate();
							rate.setName((String) itemMap.get("name"));
							String mathml = (String) itemMap.get("expression");
							if (mathml != null) {
								rate.setExpression(parseMathMLString(mathml));
							}
							String targetStr = (String) itemMap.get("target");
							if (targetStr != null) {
								if (targetStr.contains(".")) {
									String[] tokens = targetStr.split("\\.");
									MetaModelExternalClassItem affected = new DefaultMetaModelClassExternalItem();
									affected.SetRelationName(tokens[0]);
									affected.setName(tokens[1]);
									rate.setTarget(affected);
								} else {
									rate.setTarget(mmClass.get(targetStr));
								}
							}
							String sourceStr = (String) itemMap.get("source");
							if (sourceStr != null) {
								if (sourceStr.contains(".")) {
									String[] tokens = sourceStr.split("\\.");
									MetaModelExternalClassItem affected = new DefaultMetaModelClassExternalItem();
									affected.SetRelationName(tokens[0]);
									affected.setName(tokens[1]);
									rate.setSource(affected);
								} else {
									rate.setSource(mmClass.get(sourceStr));
								}
							}
							mmClass.put(rate.getName(), rate);
						} else if ("auxiliary".equals(type)) {
							MetaModelClassAuxiliary aux = new DefaultMetaModelClassAuxiliary();
							aux.setName((String) itemMap.get("name"));
							String mathml = (String) itemMap.get("expression");
							if (mathml != null) {
								aux.setExpression(parseMathMLString(mathml));
							}
							mmClass.put(aux.getName(), aux);
						} else if ("property".equals(type)) {
							MetaModelClassProperty prop = new DefaultMetaModelClassProperty();
							prop.setName((String) itemMap.get("name"));
							Object valObj = itemMap.get("value");
							if (valObj != null) {
								prop.setDefaultValue(Double.valueOf(valObj.toString()));
							}
							mmClass.put(prop.getName(), prop);
						} else if ("table".equals(type)) {
							MetaModelClassTable table = new DefaultMetaModelClassTable();
							table.setName((String) itemMap.get("name"));
							List<Object> values = (List<Object>) itemMap.get("values");
							if (values != null) {
								for (Object v : values) {
									table.getValues().add(Double.valueOf(v.toString()));
								}
							}
							mmClass.put(table.getName(), table);
						}
					}
				}
			}
		}

		// Relations
		List<Map<String, Object>> relationsList = (List<Map<String, Object>>) root.get("relations");
		if (relationsList != null) {
			for (Map<String, Object> relMap : relationsList) {
				String type = (String) relMap.get("type");
				String name = (String) relMap.get("name");
				String sourceName = (String) relMap.get("source");
				String targetName = (String) relMap.get("target");
				String targetRole = (String) relMap.get("targetRole");

				if ("singleRelation".equals(type)) {
					MetaModelSingleRelation sr = new DefaultMetaModelSingleRelation();
					sr.setName(name);
					sr.setSource((MetaModelClass) model.get(sourceName));
					sr.setTarget((MetaModelClass) model.get(targetName));
					sr.setTargetRole(targetRole);
					model.put(sr.getName(), sr);
				} else if ("multiRelation".equals(type)) {
					MetaModelMultiRelation mr = new DefaultMetaModelMultiRelation();
					mr.setName(name);
					mr.setSource((MetaModelClass) model.get(sourceName));
					mr.setTarget((MetaModelClass) model.get(targetName));
					mr.setTargetRole(targetRole);
					model.put(mr.getName(), mr);
				}
			}
		}

		return model;
	}

	@Override
	public void saveToFile(MetaModel model, File fileName) throws Exception {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", model.getName());

		List<Map<String, Object>> classesList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> relationsList = new ArrayList<Map<String, Object>>();

		for (String k : model.keySet()) {
			MetaModelItem mmi = model.get(k);
			if (mmi instanceof MetaModelClass) {
				classesList.add(getMetaModelClassMap((MetaModelClass) mmi));
			} else if (mmi instanceof MetaModelRelation) {
				relationsList.add(getMetaModelRelationMap((MetaModelRelation) mmi));
			}
		}

		root.put("classes", classesList);
		root.put("relations", relationsList);

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);

		Writer writer = new FileWriter(fileName);
		yaml.dump(root, writer);
		writer.close();
	}

	private Map<String, Object> getMetaModelRelationMap(MetaModelRelation mmr) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (mmr instanceof MetaModelSingleRelation) {
			map.put("type", "singleRelation");
		} else {
			map.put("type", "multiRelation");
		}
		map.put("name", mmr.getName());
		if (mmr.getSource() != null) {
			map.put("source", mmr.getSource().getName());
		}
		if (mmr.getTarget() != null) {
			map.put("target", mmr.getTarget().getName());
		}
		if (mmr.getTargetRole() != null && !mmr.getTargetRole().isEmpty()) {
			map.put("targetRole", mmr.getTargetRole());
		}
		return map;
	}

	private Map<String, Object> getMetaModelClassMap(MetaModelClass mmClass) throws Exception {
		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("name", mmClass.getName());

		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();

		for (Entry<String, MetaModelClassItem> entry : mmClass.entrySet()) {
			MetaModelClassItem mmci = entry.getValue();
			Map<String, Object> itemMap = new HashMap<String, Object>();

			if (mmci instanceof MetaModelClassStock) {
				MetaModelClassStock stock = (MetaModelClassStock) mmci;
				itemMap.put("type", "finiteStock");
				itemMap.put("name", stock.getName());
				if (stock.getExpression() != null) {
					itemMap.put("initialValue", convertExpressionToMathMLString(stock.getExpression()));
				}
				itemsList.add(itemMap);
			} else if (mmci instanceof MetaModelClassAuxiliary) {
				MetaModelClassAuxiliary aux = (MetaModelClassAuxiliary) mmci;
				itemMap.put("type", "auxiliary");
				itemMap.put("name", aux.getName());
				if (aux.getExpression() != null) {
					itemMap.put("expression", convertExpressionToMathMLString(aux.getExpression()));
				}
				itemsList.add(itemMap);
			} else if (mmci instanceof MetaModelClassProperty) {
				MetaModelClassProperty prop = (MetaModelClassProperty) mmci;
				itemMap.put("type", "property");
				itemMap.put("name", prop.getName());
				itemMap.put("value", prop.getDefaultValue());
				itemsList.add(itemMap);
			} else if (mmci instanceof MetaModelClassRate) {
				MetaModelClassRate rate = (MetaModelClassRate) mmci;
				itemMap.put("type", "rate");
				itemMap.put("name", rate.getName());
				if (rate.getExpression() != null) {
					itemMap.put("expression", convertExpressionToMathMLString(rate.getExpression()));
				}
				if (rate.getTarget() != null) {
					itemMap.put("target", rate.getTarget().getName());
				}
				if (rate.getSource() != null) {
					itemMap.put("source", rate.getSource().getName());
				}
				itemsList.add(itemMap);
			} else if (mmci instanceof MetaModelClassTable) {
				MetaModelClassTable table = (MetaModelClassTable) mmci;
				itemMap.put("type", "table");
				itemMap.put("name", table.getName());
				itemMap.put("values", new ArrayList<Double>(table.getValues()));
				itemsList.add(itemMap);
			}
		}

		classMap.put("items", itemsList);
		return classMap;
	}

	private String convertExpressionToMathMLString(br.ufjf.mmc.jynacore.expression.Expression expr) throws Exception {
		Element mathmlElement = ExpressionToMathMLConverter.convertToMathml(expr);
		XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
		return outputter.outputString(mathmlElement);
	}

	private br.ufjf.mmc.jynacore.expression.Expression parseMathMLString(String mathmlStr) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		java.io.StringReader reader = new java.io.StringReader(mathmlStr);
		org.jdom.Document doc = builder.build(reader);
		Element root = doc.getRootElement();
		return ExpressionToMathMLConverter.readFromMathML(root);
	}
}
