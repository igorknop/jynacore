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
package br.ufjf.mmc.jynacore.expression;



public enum NumberOperator implements Operator {
	ADD("+"), MINUS("-"), TIMES("*"), DIVIDE("/"), POWER("^"), ABS("ABS"), LOG(
			"LOG"), NEGATION("-"), SINE("SIN"), SQUARE("SQR"), SQUAREROOT(
			"SQRT"), CONSTANT(""), MINIMUM("MIN"), MAXIMUM("MAX"), GROUPSUM(
			"GROUPSUM"), IF("IF"), GROUPMAX("GROUPMAX"), COSINE("COSINE"), TAN(
			"TAN"), EXP("EXP"), MOD("MOD"), RAND("RAND"), RANDINT("RANDINT"), GROUPMIN(
			"GROUPMIN"), GROUPCOUNT("GROUPCOUNT"), LOOKUP("LOOKUP");
	public static NumberOperator[] GROUP_OPERATORS = {GROUPCOUNT,GROUPSUM,GROUPMIN,GROUPMAX};
	private final String symbol;
	NumberOperator(String newSymbol) {
		symbol = newSymbol;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

}
