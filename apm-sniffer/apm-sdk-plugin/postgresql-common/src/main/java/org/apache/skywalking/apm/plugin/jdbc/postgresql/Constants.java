/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.skywalking.apm.plugin.jdbc.postgresql;

/**
 * @author aderm
 */
public class Constants {
    public static final String CREATE_STATEMENT_INTERCEPTOR_CLASS = "org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.CreateStatementInterceptor";
    public static final String CREATE_PREPARED_STATEMENT_INTERCEPTOR_CLASS = "org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.CreatePreparedStatementInterceptor";
    public static final String CREATE_CALLABLE_STATEMENT_INTERCEPTOR_CLASS = "org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.CreateCallableStatementInterceptor";
    public static final String PG_PREPARED_STATEMENT_EXECUTE_METHOD_INTERCEPTOR = "org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.PreparedStatementExecuteMethodsInterceptor";
    public static final String PG_STATEMENT_EXECUTE_METHOD_INTERCEPTOR = "org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.StatementExecuteMethodsInterceptor";
}
