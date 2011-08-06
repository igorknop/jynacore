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
package br.ufjf.mmc.jynacore.metamodel.impl.examples.softwareproject;

import br.ufjf.mmc.jynacore.expression.BooleanOperator;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.OperatorEvaluator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultOperatorEvaluator;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelExternalClassItem;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.MetaModelSingleRelation;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassExternalItem;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassRate;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassStock;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelSingleRelation;

/**
 * @author Knop
 * 
 */
@SuppressWarnings("serial")
public class MediumSoftwareProject extends DefaultMetaModel {

	private MetaModelClass developer;
	private MetaModelClass artifact;
	private MetaModelClass activity;
	private MetaModelMultiRelation precedence;
	private MetaModelMultiRelation team;
	private MetaModelMultiRelation income;
	private MetaModelSingleRelation outcome;
	private OperatorEvaluator operatorEval;

	/**
	 * 
	 */
	public MediumSoftwareProject() {
		operatorEval = new DefaultOperatorEvaluator();
		setName("MyProject");
		MetaModelClassProperty property;// = new
		// DefaultMetaModelClassProperty();
		MetaModelClassAuxiliary proc;// = new DefaultMetaModelClassAuxiliary();
		DefaultMetaModelClassStock stock;// = new
		// DefaultMetaModelClassStock();
		DefaultMetaModelClassRate rate;// = new DefaultMetaModelClassStock();

		developer = new DefaultMetaModelClass();
		developer.setName("Developer");
		property = new DefaultMetaModelClassProperty();
		property.setName("experience");
		property.setDefaultValue(1.0);
		developer.put(property);
		proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("Productivity");
		proc.setExpression(new DefaultNameExpression("experience",operatorEval));
		developer.put(proc);
		developer.setMetaModel(this);
		put(developer);

		artifact = new DefaultMetaModelClass();
		artifact.setName("Artifact");
		property = new DefaultMetaModelClassProperty();
		property.setName("latent_errors");
		property.setDefaultValue(0.0);
		artifact.put(property);
		stock = new DefaultMetaModelClassStock();
		stock.setName("Errors");
		stock.setExpression(new DefaultNameExpression("latent_errors",operatorEval));
		artifact.put(stock);
		artifact.setMetaModel(this);
		put(artifact);

		activity = new DefaultMetaModelClass();

		outcome = new DefaultMetaModelSingleRelation();
		outcome.setName("Outcome");
		outcome.setSource(activity);
		outcome.setTarget(artifact);
		outcome.setMetaModel(this);
		put(outcome);

		activity.setName("Activity");
		property = new DefaultMetaModelClassProperty();
		property.setName("duration");
		property.setDefaultValue(0.0);
		activity.put(property);
		stock = new DefaultMetaModelClassStock();
		stock.setName("TimeToConclude");
		stock.setExpression(new DefaultNameExpression("duration",operatorEval));
		activity.put(stock);
		rate = new DefaultMetaModelClassRate();
		rate.setTarget(stock);// TimeToConclude above
		rate.setExpression(getActivityWorkExpression());
		rate.setName("Work");
		activity.put(rate);
		proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("DependOk");
		//"GROUPSUM (Precedence, TimeToConclude) < 0.001"
		proc.setExpression(getDependOkExpression());
		activity.put(proc);
		stock = new DefaultMetaModelClassStock();
		stock.setName("ExecutingOrDone");
		stock.setExpression(new DefaultNumberConstantExpression(0.0,operatorEval));
		activity.put(stock);
		rate = new DefaultMetaModelClassRate();
		rate.setTarget(stock);// ExecutingOrDone above
		rate.setExpression(getActivityRTExecutingExpression());
		rate.setName("RTExecuting");
		activity.put(rate);
		rate = new DefaultMetaModelClassRate();
		MetaModelExternalClassItem external = new DefaultMetaModelClassExternalItem();
		external.setName("Errors");
		external.SetRelationName("Outcome");
		rate.setTarget(external);
		rate.setExpression(getErrorsTransmiteExpression());
		rate.setName("ErrorsTransmit");
		activity.put(rate);
		proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("Prod");
		//"GROUPMAX (Team, Productivity)"
		proc.setExpression(getProdExpression());
		activity.put(proc);
		proc = new DefaultMetaModelClassAuxiliary();
		proc.setName("ErrorsPerDay");
		proc.setExpression(new DefaultNumberConstantExpression(5.0,operatorEval));	
		activity.put(proc);
		rate = new DefaultMetaModelClassRate();
		external = new DefaultMetaModelClassExternalItem();
		external.setName("Errors");
		external.SetRelationName("Outcome");
		rate.setTarget(external);
		rate.setExpression(getErrorsCommittedExpression());
		rate.setName("ErrorsCommitted");
		activity.put(rate);
		activity.setMetaModel(this);
		put(activity);

		precedence = new DefaultMetaModelMultiRelation();
		precedence.setName("Precedence");
		precedence.setSource(activity);
		precedence.setTarget(activity);
		precedence.setTargetRole("NextActivities");
		precedence.setMetaModel(this);
		put(precedence);

		team = new DefaultMetaModelMultiRelation();
		team.setName("Team");
		team.setSource(activity);
		team.setTarget(developer);
		team.setMetaModel(this);
		put(team);

		income = new DefaultMetaModelMultiRelation();
		income.setName("Income");
		income.setSource(activity);
		income.setTarget(artifact);
		income.setMetaModel(this);
		put(income);

	}

	private Expression getDependOkExpression() {
		// "GROUPSUM (Precedence, TimeToConclude) < 0.001"
		Expression eGro = new DefaultExpression(NumberOperator.GROUPSUM);
		eGro.setLeftOperand(new DefaultNameExpression("Precedence"));
		eGro.setRightOperand(new DefaultNameExpression("TimeToConclude"));
		Expression eLT = new DefaultExpression(BooleanOperator.LOWERTHAN);
		eLT.setLeftOperand(eGro);
		eLT.setRightOperand(new DefaultNumberConstantExpression(0.001));
		eLT.setOperatorEvaluator(operatorEval);
		return eLT;
	}

	private Expression getProdExpression() {
		// "GROUPMAX (Team, Productivity)"
		Expression eGro = new DefaultExpression(NumberOperator.GROUPMAX);
		eGro.setLeftOperand(new DefaultNameExpression("Team"));
		eGro.setRightOperand(new DefaultNameExpression("Productivity"));		
		eGro.setOperatorEvaluator(operatorEval);
		return eGro;
	}

	private Expression getErrorsCommittedExpression() {
		// "-ErrorsPerDay * Work / Prod"
		Expression eNeg = new DefaultExpression(NumberOperator.NEGATION);
		Expression eMul = new DefaultExpression(NumberOperator.TIMES);
		Expression eDiv = new DefaultExpression(NumberOperator.DIVIDE);
		eDiv.setLeftOperand(new DefaultNameExpression("Work"));
		eDiv.setRightOperand(new DefaultNameExpression("Prod"));
		eMul.setLeftOperand(new DefaultNameExpression("ErrorsPerDay"));
		eMul.setRightOperand(eDiv);
		eNeg.setMiddleOperand(eMul);
		eNeg.setOperatorEvaluator(operatorEval);
		return eNeg;
	}

	private Expression getErrorsTransmiteExpression() {
		// "if (RTExecuting>0.001, GROUPSUM(Income,Errors)/DT, 0)"
		Expression eIf = new DefaultExpression(NumberOperator.IF);
		Expression eGT = new DefaultExpression(BooleanOperator.GREATERTHAN);
		eGT.setLeftOperand(new DefaultNameExpression("RTExecuting"));
		eGT.setRightOperand(new DefaultNumberConstantExpression(0.001));
		Expression eGS = new DefaultExpression(NumberOperator.GROUPSUM);
		eGS.setLeftOperand(new DefaultNameExpression("Income"));
		eGS.setRightOperand(new DefaultNameExpression("Errors"));
		Expression eDiv = new DefaultExpression(NumberOperator.DIVIDE);
		eDiv.setLeftOperand(eGS);
		eDiv.setRightOperand(new DefaultNameExpression("_STEP_TIME_"));
		eIf.setLeftOperand(eGT);
		eIf.setMiddleOperand(eDiv);
		eIf.setRightOperand(new DefaultNumberConstantExpression(0.0));
		eIf.setOperatorEvaluator(operatorEval);
		return eIf;
	}

	private Expression getActivityRTExecutingExpression() {
		// "if(AND(ExecutingOrDone<0.001, DependOk),1,0)"
		Expression eIf = new DefaultExpression(NumberOperator.IF);
		eIf.setOperatorEvaluator(operatorEval);
		Expression eAnd = new DefaultExpression(BooleanOperator.AND);
		Expression eLT = new DefaultExpression(BooleanOperator.LOWERTHAN);
		eLT.setLeftOperand(new DefaultNameExpression("ExecutingOrDone"));
		eLT.setRightOperand(new DefaultNumberConstantExpression(0.001));
		eAnd.setLeftOperand(eLT);
		eAnd.setRightOperand(new DefaultNameExpression("DependOk"));
		eIf.setLeftOperand(eAnd);
		eIf.setMiddleOperand(new DefaultNumberConstantExpression(1.0));
		eIf.setRightOperand(new DefaultNumberConstantExpression(0.0));
		return eIf;
	}

	private Expression getActivityWorkExpression() {
		// "if(DependOk, -Min(Prod*TimeToConclude/DT, Prod),0)"


		Expression eDiv = new DefaultExpression(NumberOperator.DIVIDE);
		eDiv.setLeftOperand(new DefaultNameExpression("TimeToConclude"));
		eDiv.setRightOperand(new DefaultNameExpression("_STEP_TIME_"));
		Expression eMul = new DefaultExpression(NumberOperator.TIMES);
		eMul.setLeftOperand(new DefaultNameExpression("Prod"));
		eMul.setRightOperand(eDiv);

		Expression eMin = new DefaultExpression(NumberOperator.MINIMUM);
		eMin.setLeftOperand(eMul);
		eMin.setRightOperand(new DefaultNameExpression("Prod"));

		Expression eNeg = new DefaultExpression(NumberOperator.NEGATION);
		eNeg.setMiddleOperand(eMin);

		Expression eIf = new DefaultExpression();
		eIf.setOperator(NumberOperator.IF);
		eIf.setLeftOperand(new DefaultNameExpression("DependOk"));
		eIf.setMiddleOperand(eNeg);
		eIf.setRightOperand(new DefaultNumberConstantExpression(0.0));
		eIf.setOperatorEvaluator(operatorEval);
		return eIf;
	}
}
