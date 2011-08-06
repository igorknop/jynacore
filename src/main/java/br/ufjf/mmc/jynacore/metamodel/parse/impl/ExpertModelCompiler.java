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
/* compilador.java
 * Created on 23 de Mar�o de 2007, 10:01
 * Changed to jynacore by Knop
 */

package br.ufjf.mmc.jynacore.metamodel.parse.impl;

import java.util.Vector;

import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.Operator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;

/**
 * @author ciro
 */

public class ExpertModelCompiler {

	public Vector<Token> reserva;
	public int prox_char;
	public int inic_lexema;
	public String lexema;
	public String sint_token;
	public boolean sint_avanco;
	private String code;

	// UI ui;

	/** Creates a new instance of compilador */

	public ExpertModelCompiler(/*  UI _ui */) {
		//  ui = _ui;
		reserva = new Vector<Token>(6);
		reserva.addElement(new Token("MODEL"));
		reserva.addElement(new Token("CLASS"));
		reserva.addElement(new Token("PUBLIC"));
		reserva.addElement(new Token("PRIVATE"));
		reserva.addElement(new Token("PROPERTY"));
		reserva.addElement(new Token("STOCK"));
		reserva.addElement(new Token("RATE"));
		reserva.addElement(new Token("PROC"));
		reserva.addElement(new Token("TABLE"));
		reserva.addElement(new Token("MULTIRELATION"));
		reserva.addElement(new Token("RELATION"));
		reserva.addElement(new Token("IF"));
		reserva.addElement(new Token("OR"));
		reserva.addElement(new Token("AND"));
		reserva.addElement(new Token("NOT"));
	}

	public void setCode(String newCode){
		code = newCode;
	}
	public String getCode(){
		return code;
	}
	public int princ() {
		prox_char = 0; // zerar o proxima caracter a ser lido do buffer
		int ret = model();
		if (ret == -1) {
			if (pega_token().equals("fim"))
				return -1;
		} else if (ret == -4)
			return ret;
		return -3;
	}

	public int model() {
		int ret;
		int estado = 0;
		sint_avanco = false;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("MODEL"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4; // erro lexico
				else
					estado = -3;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4; // erro lexico
				else
					estado = -3;
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("abre_chaves"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4; // erro lexico
				else
					estado = -3;
				break;
			case 3:
				ret = model_item();
				if (ret == -1)
					estado = 4; // fim
				else if (ret == -2 || ret == -3)
					estado = -3; // erro lexico
				else
					estado = ret;
				break;
			case 4:
				sint_token = pega_token();
				if (sint_token.equals("fecha_chaves"))
					estado = 5;
				else if (sint_token.equals("erro"))
					estado = -4; // erro lexico
				else
					estado = -3;
				break;
			case 5:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4; // erro lexico
				else
					estado = -3;
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int model_item() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = T1();
				if (ret == -1)
					estado = 1;
				else
					estado = ret;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			case 2:
				ret = model_item_1();
				if (ret == -1)
					estado = -1; // fim
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int T1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = classe();
				if (ret == -1)
					estado = -1;
				else if (ret == -4 || ret == -3)
					estado = ret;
				else {
					ret = relacao();
					if (ret == -1)
						estado = -1;
					else if (ret == -4 || ret == -3)
						estado = ret;
					else
						estado = ret;
				}
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int model_item_1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = T1();
				if (ret == -1)
					estado = 1;
				else {
					sint_avanco = true;
					estado = -1; // vazio
				}
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			case 2:
				ret = model_item_1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int classe() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("CLASS"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("abre_chaves"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			case 3:
				ret = classe_item();
				if (ret == -1)
					estado = 4; // fim
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatic
				else
					estado = ret; // erro lexico
				break;
			case 4:
				sint_token = pega_token();
				if (sint_token.equals("fecha_chaves"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int relacao() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = multi_relation();
				if (ret == -1)
					estado = -1;
				else if (ret == -4 || ret == -3)
					estado = ret;
				else {
					ret = single_relation();
					if (ret == -1)
						estado = -1;
					else if (ret == -4 || ret == -3)
						estado = -3;
					else
						estado = ret;
				}
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int multi_relation() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("MULTIRELATION"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 3:
				sint_token = pega_token();
				if (sint_token.equals("virgula"))
					estado = 4;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 4:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 5;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 5:
				ret = target_role();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret;
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int target_role() {
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("abre_parenteses"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -1; // erro sintatico
					sint_avanco = true;
				}
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 3:
				sint_token = pega_token();
				if (sint_token.equals("fecha_parenteses"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int single_relation() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("RELATION"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 3:
				sint_token = pega_token();
				if (sint_token.equals("virgula"))
					estado = 4;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 4:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 5;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 5:
				ret = target_role();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret;
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int classe_item() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = T2();
				if (ret == -1)
					estado = 1;
				else
					estado = ret;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			case 2:
				ret = class_item_1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret;
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int T2() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = property();
				if (ret == -1)
					estado = -1;
				else if (ret == -3 || ret == -4)
					estado = ret;
				else {
					ret = behaviour();
					if (ret == -1)
						estado = -1;
					else if (ret == -4 || ret == -3)
						estado = -ret;
					else {
						sint_token = pega_token();
						if (sint_token.equals("PUBLIC"))
							estado = -1;
						else if (sint_token.equals("erro"))
							estado = -4;
						else if (sint_token.equals("PRIVATE"))
							estado = -1;
						else if (sint_token.equals("erro"))
							estado = -4;
						else {
							estado = -2;
							sint_avanco = true;
						}
					}
				}
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int property() {
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("PROPERTY"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("numero"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int behaviour() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = stock();
				if (ret == -1)
					estado = -1;
				else if (ret == -4 || ret == -3)
					estado = ret;
				else {
					ret = rate();
					if (ret == -1)
						estado = -1;
					else if (ret == -4 || ret == -3)
						estado = -ret;
					else {
						ret = proc();
						if (ret == -1)
							estado = -1;
						else if (ret == -4 || ret == -3)
							estado = -ret;
						else {
							ret = table();
							if (ret == -1)
								estado = -1;
							else if (ret == -4 || ret == -3)
								estado = -ret;
							else
								estado = ret;
						}
					}
				}
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int table() {
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("TABLE"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			default:
				;
			}
		}
		return estado;

	}

	public int proc() {
		int ret;
		int estado = 0;
		Expression e = null;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("PROC"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				ret = Ea(e);
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int rate() {
		int ret;
		int estado = 0;
		Expression e = null;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("RATE"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("abre_parenteses"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				ret = IDENT();
				if (ret == -1)
					estado = 3;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			case 3:
				sint_token = pega_token();
				if (sint_token.equals("fecha_parenteses"))
					estado = 4;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 4:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 5;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 5:
				ret = Ea(e);
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int stock() {
		int ret;
		int estado = 0;
		Expression e = null;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("STOCK"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 2:
				ret = Ea(e);
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int class_item_1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = T2();
				if (ret == -1)
					estado = 1;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro sintatico ou lexico
				else
					estado = -1; // vazio
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			case 2:
				ret = class_item_1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	/*
	 * Ea -> F Ea1 Ea1 -> Op1 F Ea1 | vazio F -> N F1 F1 -> Op2 N F1 | vazio N -> -
	 * Ta | Ta Ta -> id | numero | ( Ea ) | if(El:Ea;Ea) Op1 -> + | - Op2 -> * | /
	 */
	public int Ea(Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
				// ret = F(e.operando1);
				ret = F(e.getLeftOperand());
				if (ret == -1)
					estado = 1;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 1:
				// ret = Ea1(e.operador, e.operando2);
				ret = Ea1(e.getOperator(), e.getRightOperand());
				if (ret == -1) {
					// if (e.operador == null) {
					if (e.getOperator() == null) {
						// e = e.operando1;
						e = e.getLeftOperand();
					}
					estado = -1;
				} else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	public int F(Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
//				ret = N(e.operando1);
				ret = N(e.getLeftOperand());
				if (ret == -1)
					estado = 2;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 2:
//				ret = F1(e.operador, e.operando2);
				ret = F1(e.getOperator(), e.getRightOperand());
				if (ret == -1) {
//					if (e.operador == null)
//						e = e.operando1;
					if (e.getOperator() == null)
						e = e.getLeftOperand();
					estado = -1;
				} else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	// public int Ea1(String o, Expression e) {
	public int Ea1(Operator o, Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_soma")) {
					// o = lexema;
					o = NumberOperator.ADD;
					estado = 1;
				} else if (sint_token.equals("operador_subtracao")) {
					// o = lexema;
					o = NumberOperator.MINUS;
					estado = 1;
				} else {
					estado = -1; // vazio
					o = null;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				// ret = F(e.operando1);
				ret = F(e.getLeftOperand());
				if (ret == -1)
					estado = 2;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 2:
				// ret = Ea1(e.operador, e.operando2);
				ret = Ea1(e.getOperator(), e.getLeftOperand());
				if (ret == -1) {
					// if (e.operador == null)
					if (e.getOperator() == null)
						// e = e.operando1;
						e = e.getLeftOperand();
					estado = -1;
				} else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	public int N(Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_subtracao")) {
					// e.tipo_operador = "unario";
					e.setOperator(NumberOperator.NEGATION);
					// e.setValueType(ExpressionValueType.NUMBER);
					// e.operador = lexema;
					estado = 1;
				} else if (sint_token.equals("erro"))
					estado = -4;
				else {
					sint_avanco = true;
					ret = Ta(e);
					if (ret == -1)
						estado = -1;
					else
						estado = ret;
				}
				break;
			case 1:
				// ret = Ta(e.operando1);
				ret = Ta(e.getLeftOperand());
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // outro lexico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	// public int F1(String o, Expression e) {
	public int F1(Operator o, Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_multiplicacao")) {
					// o = lexema;
					o = NumberOperator.TIMES;
					estado = 1;
				} else if (sint_token.equals("operador_divisao")) {
					// o = lexema;
					o = NumberOperator.DIVIDE;
					estado = 1;
				} else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -1; // vazio
					o = null;
					sint_avanco = true;
				}
				;
				break;
			case 1:
				// ret = N(e.operando1);
				ret = N(e.getLeftOperand());
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro lexico ou sintatico
				else
					estado = -4; // outro
				break;
			case 2:
				// ret = F1(e.operador, e.operando2);
				ret = F1(e.getOperator(), e.getRightOperand());
				if (ret == -1) {
					estado = -1;
					// if (e.operador == null)
					// e = e.operando1;
					if (e.getOperator() == null)
						e = e.getLeftOperand();
				} else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	public int Ta(Expression e) {
		int ret;
		int estado = 0;
		e = new DefaultExpression();
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("identificador")) {
					estado = -1;
					// e.tipo_expression = "folha";
					// e.tipo_folha = "identificador";
					// e.value = lexema;
					e = new DefaultNameExpression(lexema);
				} else if (sint_token.equals("numero")) {
					estado = -1;
					// e.tipo_expression = "folha";
					// e.tipo_folha = "numero";
					// e.value = lexema;
					e = new DefaultNumberConstantExpression(Double
							.valueOf(lexema));
				} else if (sint_token.equals("abre_parenteses"))
					estado = 1;
				else if (sint_token.equals("if"))
					estado = 3;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				break;
			case 1:
				ret = Ea(e);
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = ret; // erro sintatico
				else
					estado = -4; // erro lexico
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("fecha_parenteses"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 3:
				sint_token = pega_token();
				if (sint_token.equals("abre_parenteses"))
					estado = 4;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 4:
//				ret = El(e.operando1);
				ret = El(e.getLeftOperand());
				if (ret == -1)
					estado = 5;
				else if (ret == -2 || ret == -3)
					estado = ret; // erro sintatico
				else
					estado = -4; // erro lexico
				break;
			case 5:
				sint_token = pega_token();
				if (sint_token.equals("dois_pontos"))
					estado = 6;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 6:
//				ret = Ea(e.operando2);
				ret = Ea(e.getMiddleOperand());
				if (ret == -1)
					estado = 7;
				else if (ret == -2 || ret == -3)
					estado = ret; // erro sintatico
				else
					estado = -4; // erro lexico
				break;
			case 7:
				sint_token = pega_token();
				if (sint_token.equals("ponto_virgula"))
					estado = 8;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			case 8:
//				ret = Ea(e.operando3);
				ret = Ea(e.getRightOperand());
				if (ret == -1)
					estado = 9;
				else if (ret == -2 || ret == -3)
					estado = ret; // erro sintatico
				else
					estado = -4; // erro lexico
				break;
			case 9:
				sint_token = pega_token();
				if (sint_token.equals("fecha_parenteses"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // erro sintatico
				break;
			default:
				;
			}
		}
		if (estado != -1)
			e = null;
		return estado;
	}

	/*
	 * Er -> Ea Opr Ea Opr -> ==| <= | >= | < | > | !=
	 */
	public int Er() {
		int ret;
		int estado = 0;
		Expression e = null;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = Ea(e);
				if (ret == -1)
					estado = 1;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 1:
				ret = Opr();
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			case 2:
				ret = Ea(e);
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro lexico ou sintatico
				else
					estado = -4; // outro
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int Opr() {
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("igualdade"))
					estado = -1;
				else if (sint_token.equals("menor_igual"))
					estado = -1;
				else if (sint_token.equals("maior_igual"))
					estado = -1;
				else if (sint_token.equals("menor"))
					estado = -1;
				else if (sint_token.equals("maior"))
					estado = -1;
				else if (sint_token.equals("diferente"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -2;
					sint_avanco = true;
				}
				break;
			default:
				;
			}
		}
		return estado;
	}

	/*
	 * El -> Ee El1 El1 -> or Ee El1 | vazio Ee -> En Ee1 Ee1 -> and En Ee1 |
	 * vazio En -> not Tl | Tl Tl -> id | (El) | Er
	 */
	public int El(Expression e) {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = Ee();
				if (ret == -1)
					estado = 1;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 1:
				ret = El1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int Ee() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				ret = En();
				if (ret == -1)
					estado = 1;
				else if (ret == -4 || ret == -3)
					estado = ret; // erro lexico ou sintatico
				else
					estado = -2; // outro
				break;
			case 1:
				ret = Ee1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int El1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_ou"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -1; // vazio
					sint_avanco = true;
				}
				;
				break;
			case 1:
				ret = Ee();
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = -4; // outro
				break;
			case 2:
				ret = El1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int En() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_negacao"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					sint_avanco = true;
					ret = Tl();
					if (ret == -1)
						estado = -1;
					else
						estado = ret;
				}
				break;
			case 1:
				ret = Tl();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // outro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int Ee1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("operador_e"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -1; // vazio
					sint_avanco = true;
				}
				;
				break;
			case 1:
				ret = En();
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = -4; // outro
				break;
			case 2:
				ret = Ee1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int Tl() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else if (sint_token.equals("abre_parenteses"))
					estado = 1; // vazio
				else {
					sint_avanco = true;
					estado = Er();
				}
				;
				break;
			case 1:
				ret = Er();
				if (ret == -1)
					estado = 2;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = -4; // outro
				break;
			case 2:
				sint_token = pega_token();
				if (sint_token.equals("fecha_parenteses"))
					estado = -1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3;
				break;
			default:
				;
			}
		}
		return estado;
	}

	/*
	 * IDENTIFICADOR - considerando as referencias de classes '.' IDENT ->
	 * identificador IDENT1 IDENT1 -> . identificador IDENT1 | vazio
	 */
	public int IDENT() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					sint_avanco = true;
					estado = -2;
				}
				;
				break;
			case 1:
				ret = IDENT1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = -4; // outro
				break;
			default:
				;
			}
		}
		return estado;
	}

	public int IDENT1() {
		int ret;
		int estado = 0;
		while (estado >= 0) {
			switch (estado) {
			case 0:
				sint_token = pega_token();
				if (sint_token.equals("ponto"))
					estado = 1;
				else if (sint_token.equals("erro"))
					estado = -4;
				else {
					estado = -1; // vazio
					sint_avanco = true;
				}
				;
				break;
			case 1:
				sint_token = pega_token();
				if (sint_token.equals("identificador"))
					estado = 2;
				else if (sint_token.equals("erro"))
					estado = -4;
				else
					estado = -3; // vazio
				break;
			case 2:
				ret = IDENT1();
				if (ret == -1)
					estado = -1;
				else if (ret == -2 || ret == -3)
					estado = -3; // erro sintatico
				else
					estado = ret; // erro lexico
				break;
			default:
				;
			}
		}
		return estado;
	}

	public String pega_token() {
		if (sint_avanco)
			sint_avanco = false;
		else
			sint_token = proximo_token();
		return sint_token;
	}

	public String proximo_token() {
		int estado = 0;
		
		//String buffer = "";// ui.pegaConteudoAreaExpert();
		
		String token = "";
		char t = ' ';
		// enquanto nao chegar ao fim do token
		while (estado != -1) {
			switch (estado) {
			case 0:
				// enquanto nao achar um charctere valido
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					switch (t) {
					case '(':
						token = "abre_parenteses";
						estado = -1;
						break;
					case '{':
						token = "abre_chaves";
						estado = -1;
						break;
					case '[':
						token = "abre_couchetes";
						estado = -1;
						break;
					case ';':
						token = "ponto_virgula";
						estado = -1;
						break;
					case ',':
						token = "virgula";
						estado = -1;
						break;
					case ':':
						token = "dois_pontos";
						estado = -1;
						break;
					case '\n':
						;
						continue;
					case ' ':
						;
						continue;
					case '\t':
						;
						continue;
					case ')':
						token = "fecha_parenteses";
						estado = -1;
						break;
					case '}':
						token = "fecha_chaves";
						estado = -1;
						break;
					case ']':
						token = "fecha_colchetes";
						estado = -1;
						break;
					case '+':
						token = "operador_soma";
						estado = -1;
						break;
					case '-':
						token = "operador_subtracao";
						estado = -1;
						break;
					case '*':
						token = "operador_multiplicacao";
						estado = -1;
						break;
					case '/':
						token = "operador_divisao";
						estado = -1;
						break;
					case '.':
						token = "ponto";
						estado = -1;
						break;
					case '=':
						estado = 4;
						inic_lexema = prox_char - 1;
						break;
					case '<':
						estado = 5;
						inic_lexema = prox_char - 1;
						break;
					case '>':
						estado = 6;
						inic_lexema = prox_char - 1;
						break;
					case '!':
						estado = 7;
						inic_lexema = prox_char - 1;
						break;
					default:
						estado = 1;
					}
				} else {
					token = "fim";
					estado = -1;
				}
				;
				break;
			case 1:
				// se for letra vai para estado 2
				if (e_letra(t)) {
					inic_lexema = prox_char - 1;
					estado = 2;
					continue;
				} else if (e_numero(t)) {
					// se for numero vai para estado 3
					inic_lexema = prox_char - 1;
					estado = 3;
					continue;
				} else {
					// nao e nenhum caractere valido
					token = "erro";
					estado = -1;
				}
				;
				break;
			case 2:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (!e_letra(t)) {
						prox_char--;
						lexema = code.substring(inic_lexema, prox_char);
						token = palavra_reservada(new Token(lexema));
						estado = -1;
					}
				} else {
					lexema = code.substring(inic_lexema, prox_char);
					token = palavra_reservada(new Token(lexema));
					estado = -1;
				}
				break;
			case 3:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (!e_numero(t)) {
						prox_char--;
						lexema = code.substring(inic_lexema, prox_char);
						token = "numero";
						estado = -1;
					}
				} else {
					lexema = code.substring(inic_lexema, prox_char);
					token = "numero";
					estado = -1;
				}
				break;
			case 4:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (t != '=') {
						prox_char--;
						lexema = code.substring(inic_lexema, prox_char);
						token = "atribuicao";
						estado = -1;
					} else {
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_igualdade";
						estado = -1;
					}
				} else {
					lexema = code.substring(inic_lexema, prox_char);
					token = "atribuicao";
					estado = -1;
				}
				;
				break;
			case 5:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (t != '=') {
						prox_char--;
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_menor";
						estado = -1;
					} else {
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_menor_igual";
						estado = -1;
					}
				} else {
					lexema = code.substring(inic_lexema, prox_char);
					token = "operador_menor";
					estado = -1;
				}
				;
				break;
			case 6:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (t != '=') {
						prox_char--;
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_maior";
						estado = -1;
					} else {
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_maior_igual";
						estado = -1;
					}
				} else {
					lexema = code.substring(inic_lexema, prox_char);
					token = "operador_maior";
					estado = -1;
				}
				;
				break;
			case 7:
				if (prox_char < code.length()) {
					t = code.charAt(prox_char++);
					if (t != '=') {
						prox_char--;
						token = "erro";
						estado = -4;
					} else {
						lexema = code.substring(inic_lexema, prox_char);
						token = "operador_diferente";
						estado = -1;
					}
				} else {
					token = "erro";
					estado = -4;
				}
				;
				break;
			default:
				;
			}
		}
		return token;
	}

	public String palavra_reservada(Token s) {
		int i = 0;
		String aux2 = s.t;
		while (i < reserva.size()) {
			String aux1 = ((Token) reserva.get(i++)).t;
			if (aux1.equals(aux2))
				return aux1;
		}
		return "identificador";
	}

	public boolean e_letra(char l) {
		if (l == 'a' || l == 'b' || l == 'c' || l == 'd' || l == 'e'
				|| l == 'f' || l == 'g' || l == 'h' || l == 'i' || l == 'j'
				|| l == 'k' || l == 'l' || l == 'm' || l == 'n' || l == 'o'
				|| l == 'p' || l == 'q' || l == 'r' || l == 's' || l == 't'
				|| l == 'u' || l == 'v' || l == 'x' || l == 'y' || l == 'w'
				|| l == 'z' || l == 'A' || l == 'B' || l == 'C' || l == 'D'
				|| l == 'E' || l == 'F' || l == 'G' || l == 'H' || l == 'I'
				|| l == 'J' || l == 'K' || l == 'L' || l == 'M' || l == 'N'
				|| l == 'O' || l == 'P' || l == 'Q' || l == 'R' || l == 'S'
				|| l == 'T' || l == 'U' || l == 'V' || l == 'X' || l == 'Y'
				|| l == 'W' || l == 'Z' || l == '_')
			return true;
		return false;
	}

	public boolean e_numero(char l) {
		if (l == '0' || l == '1' || l == '2' || l == '3' || l == '4'
				|| l == '5' || l == '6' || l == '7' || l == '8' || l == '9'
				|| l == '.')
			return true;
		return false;
	}

}
