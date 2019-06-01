<#--
// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to you under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
-->

JoinType LeftSemiJoin() :
{
}
{
    <LEFT> <SEMI> <JOIN> { return JoinType.LEFT_SEMI_JOIN; }
}

SqlBinaryOperator SpecialBinaryOperator() :
{
}
{
    <PG_CAST> { return SqlStdOperatorTable.PG_CAST; }
}

SqlIdentifier PostgreSQLTypes(Span s) :
{
}
{
    "REGPROC" { return new SqlIdentifier("REGPROC", s.end(this)); }
    |
    <TEXT> { return new SqlIdentifier(SqlTypeName.TEXT.name(), s.end(this)); }
}

SqlBinaryOperator PostgreSQLCasting(List<Object> list, ExprContext exprContext) :
{
    SqlBinaryOperator op;
}
{
    op = SpecialBinaryOperator() {
        checkNonQueryExpression(exprContext);
        list.add(new SqlParserUtil.ToTreeListItem(op, getPos()));
    }
    { final SqlDataTypeSpec dt; }
    (
      dt = DataType() {
        list.add(dt);
      }
    )
    { return op ;}
}
// End parserImpls.ftl
