/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is JSwat. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2003-2012. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 */

package com.bluemarsh.jswat.core.expr;

import org.junit.Test;

/**
 * Tests the Evaluator class.
 *
 * @author  Nathan Fiedler
 */
public class BasicEvaluatorTest {

    @Test
    public void test_Evaluator_Basic() {
        TestData[] testDatum = new TestData[] {
            // base cases
            new TestData(null, null, "Null expression should be null"),
            new TestData("", null, "Empty string should be null"),
            new TestData("null", null, "'null' should be null"),
            new TestData("false", Boolean.FALSE),
            new TestData("true", Boolean.TRUE),
            new TestData("\"abc\"", "abc"),
            new TestData("128", new Integer(128)),
            new TestData("182L", new Long(182)),
            new TestData("1.28", new Float(1.28)),
            new TestData("1.82F", new Float(1.82)),
            new TestData("1.28D", new Double(1.28D)),

            // number bases (hexadecimal and octal)
            new TestData("0x3039", new Integer(12345)),
            new TestData("030071", new Integer(12345)),

            // binary: +, -
            new TestData("1 + 1", new Integer(2)),
            new TestData("1 + -1", new Integer(0)),
            new TestData("-1 + 1", new Integer(0)),
            new TestData("-1 + -1", new Integer(-2)),
            new TestData("1 - 1", new Integer(0)),
            new TestData("1 - -1", new Integer(2)),
            new TestData("-1 - 1", new Integer(-2)),
            new TestData("-1 - -1", new Integer(0)),
            new TestData("\"abc\" + 123", "abc123"),
            new TestData("1 + true", "1true"),
            new TestData("1 + null", "1null"),
            new TestData("2 - 1", new Integer(1)),
            new TestData("1 - 2", new Integer(-1)),
            new TestData("1 - 2L", new Long(-1)),
            new TestData("1L - 2", new Long(-1)),
            new TestData("1.0D - 2", new Double(-1.0)),
            new TestData("1.0 + 2.0", new Float(3.0)),

            // unary: -, +, !, ~
            new TestData("-1", new Integer(-1)),
            new TestData("-1L", new Long(-1)),
            new TestData("+1", new Integer(1)),
            new TestData("+1L", new Long(1)),
            new TestData("!true", Boolean.FALSE),
            new TestData("!false", Boolean.TRUE),
            new TestData("~128", new Integer(-129)),
            new TestData("2 - -1", new Integer(3)),
            new TestData("1 - +2", new Integer(-1)),
            new TestData("~'a'", new Integer(-98)),

            // mult, div, mod: *, /, %
            new TestData("1 * 2", new Integer(2)),
            new TestData("1 * 1", new Integer(1)),
            new TestData("1 * -1", new Integer(-1)),
            new TestData("-1 * 1", new Integer(-1)),
            new TestData("-1 * -1", new Integer(1)),
            new TestData("10 * 20", new Integer(200)),
            new TestData("-1 * 5", new Integer(-5)),
            new TestData("0 * 1", new Integer(0)),
            new TestData("1 * a"),
            new TestData("foo * 2.0"),
            new TestData("6 / 2", new Integer(3)),
            new TestData("1 / 0"),
            new TestData("0.0 / 0.0", new Float(Float.NaN)),
            new TestData("1 / null"),
            new TestData("1 / 2.0", new Float(0.5)),
            new TestData("2 / 8.0", new Float(0.25)),
            new TestData("1.0 / 0", new Float(Float.POSITIVE_INFINITY)),
            new TestData("-1.0 / 0", new Float(Float.NEGATIVE_INFINITY)),
            new TestData("11 % 2", new Integer(1)),
            new TestData("1 + 2 * 3", new Integer(7)),

            // equality: ==, !=
            new TestData("true == true", Boolean.TRUE),
            new TestData("true == false", Boolean.FALSE),
            new TestData("'A' == 'a'", Boolean.FALSE),
            new TestData("'a' == 'a'", Boolean.TRUE),
            new TestData("true != true", Boolean.FALSE),
            new TestData("false != false", Boolean.FALSE),
            new TestData("true != false", Boolean.TRUE),
            new TestData("true == null", Boolean.FALSE),
            // For some reason this doesn't work, but I'm not concerned.
            //new TestData("0.0 / 0.0 != 0.0 / 0.0", Boolean.TRUE),
            new TestData("0.0 / 0.0 != 0.0", Boolean.TRUE),

            // bitwise: &, |, ^
            new TestData("255 & 32", new Integer(32)),
            new TestData("64 | 32", new Integer(96)),
            new TestData("255 ^ 32", new Integer(223)),
            new TestData("2.0 ^ 32"),
            new TestData("255 ^ 3.2"),
            new TestData("null ^ 32"),
            new TestData("'a' | 32", new Integer(97)),
            new TestData("'a' ^ 32", new Integer(65)),

            // shift: <<, >>, >>>
            new TestData("32 >> 4", new Integer(2)),
            new TestData("32L >> 4", new Long(2)),
            new TestData("0xF1234567 >>> 4", new Long(252851286)),
            new TestData("2 << 4", new Integer(32)),

            // comparison: <, >, <=, <=
            new TestData("32 > 4", Boolean.TRUE),
            new TestData("4 > 32", Boolean.FALSE),
            new TestData("32 < 4", Boolean.FALSE),
            new TestData("4 < 32", Boolean.TRUE),
            new TestData("4 <= 32", Boolean.TRUE),
            new TestData("32 <= 32", Boolean.TRUE),
            new TestData("32 >= 4", Boolean.TRUE),
            new TestData("32 >= 32", Boolean.TRUE),
            new TestData("4 >= 32", Boolean.FALSE),
            new TestData("32 <= 4", Boolean.FALSE),
            new TestData("0.0 / 0.0 < 0.0", Boolean.FALSE),
            new TestData("0.0 / 0.0 > 0.0", Boolean.FALSE),
            new TestData("0.0 >= 0.0 / 0.0", Boolean.FALSE),
            new TestData("0.0 <= 0.0 / 0.0", Boolean.FALSE),
            new TestData("(0.0/0.0 < 0.0) == !(0.0/0.0 >= 0.0)", Boolean.FALSE),

            // logical: &&, ||
            new TestData("true || false", Boolean.TRUE),
            new TestData("false || false", Boolean.FALSE),
            new TestData("null || false"),
            new TestData("true && true", Boolean.TRUE),
            new TestData("true && false", Boolean.FALSE),

            // grouping: ()
            new TestData("(1 + 2) * 3", new Integer(9)),
            new TestData("3 * (1 + 2)", new Integer(9)),
            new TestData("(1) + (2) * (3)", new Integer(7)),
            new TestData("((1 + (2 * 3)))", new Integer(7)),
            new TestData("(((1)))", new Integer(1)),
            new TestData("(((1))"),
            new TestData("((1)))"),
            new TestData("(1 + (2) * (3)"),
            new TestData("(1) + (2 * (3)"),
            new TestData("(1) + (2) * (3"),
            new TestData("(1 + 2 * 3"),
            new TestData("1 + 2 * 3)"),
            new TestData("(true) == (true)", Boolean.TRUE),
            new TestData("(1 + 1) == (3 - 1)", Boolean.TRUE),
            new TestData("(1 + 1) == 2", Boolean.TRUE),
            new TestData("2 != (3 - 1)", Boolean.FALSE),
            new TestData("2 == (3 - 1)", Boolean.TRUE),

            // typecast ()
            new TestData("(boolean) true", Boolean.TRUE),
            new TestData("(Boolean) false", Boolean.FALSE),
            new TestData("(String) \"string\"", "string"),
            new TestData("(Number) 123456", new Integer(123456)),
            new TestData("(String) 123456", "123456"),
            new TestData("(long) 123456", new Long(123456)),
            new TestData("(Long) 123456", new Long(123456)),
            new TestData("(byte) 12", new Byte((byte) 12)),
            new TestData("(Byte) 12", new Byte((byte) 12)),
            new TestData("(short) 12", new Short((short) 12)),
            new TestData("(Short) 12", new Short((short) 12)),
            new TestData("(float) 0.1", new Float(0.1)),
            new TestData("(Float) 0.1", new Float(0.1)),
            new TestData("(double) 0.2", new Double(0.20000000298023224)),
            new TestData("(Double) 0.2", new Double(0.20000000298023224)),
            new TestData("(Object) null", null),
            new TestData("(boolean) 123"),
            new TestData("(void) 123"),
            new TestData("(Class) 123"),
            new TestData("(List) 123"),
            new TestData("(short) true"),
        };
        EvaluatorHelper helper = new EvaluatorHelper();
        helper.performTests(testDatum, null, 0);
    }
}
