/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.sql.fun;

import org.apache.calcite.sql.SqlBinaryOperator;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlCallBinding;
import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperandCountRange;
import org.apache.calcite.sql.SqlOperatorBinding;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.type.InferTypes;
import org.apache.calcite.sql.validate.SqlMonotonicity;

/**
 * PostgreSQL's casting operator <code>::</code>
 *
 */
public class PostgreSQLCastOperator extends SqlBinaryOperator {
  /**
   * Most of logic required for PostgreSQLCastOperator is already implemented by
   * the {@link SqlCastFunction} but we can't inherit that class to override its
   * logic because it inherits from {@link SqlFunction} while
   * {@link PostgreSQLCastOperator} inherits from {@link SqlBinaryOperator}.
   *
   * So use an instance of the {@link SqlCastFunction} to utilize its logic when
   * applicable.
   */
  private static final SqlCastFunction FUN = new SqlCastFunction();

  PostgreSQLCastOperator() {
    super("POSTGRESQL_CAST", SqlKind.CAST, 92, true, null,
        InferTypes.FIRST_KNOWN, null);
  }

  @Override public SqlOperandCountRange getOperandCountRange() {
    return FUN.getOperandCountRange();
  }

  @Override public org.apache.calcite.rel.type.RelDataType inferReturnType(
      org.apache.calcite.sql.SqlOperatorBinding opBinding) {
    return FUN.inferReturnType(opBinding);
  }

  @Override public boolean checkOperandTypes(SqlCallBinding callBinding,
      boolean throwOnFailure) {
    return FUN.checkOperandTypes(callBinding, throwOnFailure);
  }

  @Override public String getSignatureTemplate(final int operandsCount) {
    assert operandsCount == 2;
    return "{1}::{2}";
  }

  @Override public void unparse(SqlWriter writer, SqlCall call, int leftPrec,
      int rightPrec) {
    assert call.operandCount() == 2;
    call.operand(0).unparse(writer, 0, 0);
    writer.sep("::");
    call.operand(1).unparse(writer, 0, 0);
  }

  @Override public SqlMonotonicity getMonotonicity(SqlOperatorBinding call) {
    return FUN.getMonotonicity(call);
  }

  @Override public SqlSyntax getSyntax() {
    return SqlSyntax.BINARY;
  }
}

// End PostgreSQLCastOperator.java
