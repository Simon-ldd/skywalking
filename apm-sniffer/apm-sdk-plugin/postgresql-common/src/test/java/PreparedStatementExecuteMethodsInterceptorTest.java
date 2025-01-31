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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractTracingSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.context.trace.TraceSegment;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.test.helper.SegmentHelper;
import org.apache.skywalking.apm.agent.test.tools.AgentServiceRule;
import org.apache.skywalking.apm.agent.test.tools.SegmentStorage;
import org.apache.skywalking.apm.agent.test.tools.SegmentStoragePoint;
import org.apache.skywalking.apm.agent.test.tools.SpanAssert;
import org.apache.skywalking.apm.agent.test.tools.TracingSegmentRunner;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;
import org.apache.skywalking.apm.plugin.jdbc.define.StatementEnhanceInfos;
import org.apache.skywalking.apm.plugin.jdbc.postgresql.interceptor.PreparedStatementExecuteMethodsInterceptor;
import org.apache.skywalking.apm.plugin.jdbc.trace.ConnectionInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

/**
 * @author aderm
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(TracingSegmentRunner.class)
public class PreparedStatementExecuteMethodsInterceptorTest {

    @SegmentStoragePoint
    private SegmentStorage segmentStorage;

    @Rule
    public AgentServiceRule serviceRule = new AgentServiceRule();

    private PreparedStatementExecuteMethodsInterceptor interceptor;

    @Mock
    private ConnectionInfo connectionInfo;

    @Mock
    private EnhancedInstance objectInstance;

    @Mock
    private Method method;

    private StatementEnhanceInfos statementEnhanceInfos;

    @Before
    public void setUp() {
        interceptor = new PreparedStatementExecuteMethodsInterceptor();
        statementEnhanceInfos = new StatementEnhanceInfos(connectionInfo, "SELECT * FROM test WHERE item1=? and item2=?", "CallableStatement");
        statementEnhanceInfos.setParameter(1, "abc");
        statementEnhanceInfos.setParameter(2, "def");
        when(objectInstance.getSkyWalkingDynamicField()).thenReturn(statementEnhanceInfos);
        when(method.getName()).thenReturn("executeQuery");
        when(connectionInfo.getComponent()).thenReturn(ComponentsDefine.POSTGRESQL_DRIVER);
        when(connectionInfo.getDBType()).thenReturn("POSTGRESQL");
        when(connectionInfo.getDatabaseName()).thenReturn("test");
        when(connectionInfo.getDatabasePeer()).thenReturn("localhost:5432");
    }

    @Test
    public void testCreateDatabaseSpan() throws Throwable {
        interceptor.beforeMethod(objectInstance, method, new Object[]{"WHERE item1=? and item2=?"}, null, null);
        interceptor.afterMethod(objectInstance, method, new Object[]{"WHERE item1=? and item2=?"}, null, null);

        assertThat(segmentStorage.getTraceSegments().size(), is(1));
        TraceSegment segment = segmentStorage.getTraceSegments().get(0);
        assertThat(SegmentHelper.getSpans(segment).size(), is(1));
        AbstractTracingSpan span = SegmentHelper.getSpans(segment).get(0);
        SpanAssert.assertLayer(span, SpanLayer.DB);
        assertThat(span.getOperationName(), is("POSTGRESQL/JDBI/CallableStatement/"));
        SpanAssert.assertTag(span, 0, "sql");
        SpanAssert.assertTag(span, 1, "test");
        SpanAssert.assertTag(span, 2, "SELECT * FROM test WHERE item1=? and item2=?");
    }

}
