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

package org.apache.skywalking.apm.collector.storage.h2.dao.ui;

import org.apache.skywalking.apm.collector.client.h2.H2Client;
import org.apache.skywalking.apm.collector.client.h2.H2ClientException;
import org.apache.skywalking.apm.collector.storage.base.sql.SqlBuilder;
import org.apache.skywalking.apm.collector.storage.dao.ui.IApplicationMetricUIDAO;
import org.apache.skywalking.apm.collector.storage.h2.base.dao.H2DAO;
import org.apache.skywalking.apm.collector.storage.table.MetricSource;
import org.apache.skywalking.apm.collector.storage.table.application.ApplicationMetricTable;
import org.apache.skywalking.apm.collector.storage.table.instance.InstanceMetricTable;
import org.apache.skywalking.apm.collector.storage.ui.common.Step;
import org.apache.skywalking.apm.collector.storage.ui.overview.ApplicationTPS;
import org.apache.skywalking.apm.collector.storage.utils.TimePyramidTableNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author peng-yongsheng,wen-gang.ji
 */
public class ApplicationMetricH2UIDAO extends H2DAO implements IApplicationMetricUIDAO {

    private final Logger logger = LoggerFactory.getLogger(ApplicationMetricH2UIDAO.class);
    private static final String GET_APPLICATION_THROUGHPUT_SQL=
            "select {0}, sum({1}) as {1}  from {2} where {3} >= ? and {3} <= ? and {4} = ?  group by {0} limit ?";
    private static final String GET_APPLICATIONS_SQL=
            "select {0}, sum({1}) as {1},sum({2}) as {2},sum({3}) as {3},sum({4}) as {4}, sum({5}) as {5},sum({6}) as {6} " +
                    " sum({7}) as {7}  from {8} where {9} >= ? and {9} <= ?  and {10} = ?  group by {0} limit ? ";

    public ApplicationMetricH2UIDAO(H2Client client) {
        super(client);
    }

    @Override
    public List<ApplicationTPS> getTopNApplicationThroughput(Step step, long startTimeBucket, long endTimeBucket,
        int betweenSecond, int topN, MetricSource metricSource) {

        H2Client client = getClient();
        String tableName = TimePyramidTableNameBuilder.build(step, ApplicationMetricTable.TABLE);
        String sql = SqlBuilder.buildSql(GET_APPLICATION_THROUGHPUT_SQL, ApplicationMetricTable.COLUMN_APPLICATION_ID,
                ApplicationMetricTable.COLUMN_TRANSACTION_CALLS, tableName,
                ApplicationMetricTable.COLUMN_TIME_BUCKET, InstanceMetricTable.COLUMN_SOURCE_VALUE);
        List<ApplicationTPS> applicationTPSs = new LinkedList<>();
        Object[] params = new Object[] {startTimeBucket, endTimeBucket,metricSource.getValue(),2000};

        try (ResultSet rs = client.executeQuery(sql, params)) {
            while (rs.next()) {
                int applicationId = rs.getInt(ApplicationMetricTable.COLUMN_APPLICATION_ID);
                int transactionCallsSum = rs.getInt(ApplicationMetricTable.COLUMN_TRANSACTION_CALLS);
                long calls = (long)transactionCallsSum;
                int callsPerSec = (int)(betweenSecond == 0 ? 0 : calls / betweenSecond);

                ApplicationTPS applicationTPS = new ApplicationTPS();
                applicationTPS.setApplicationId(applicationId);
                applicationTPS.setCallsPerSec(callsPerSec);
                applicationTPSs.add(applicationTPS);
            }


            if (applicationTPSs.size() <= topN) {
                return applicationTPSs;
            } else {
                List<ApplicationTPS> newCollection = new LinkedList<>();
                for (int i = 0; i < topN; i++) {
                    newCollection.add(applicationTPSs.get(i));
                }
                return newCollection;
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return applicationTPSs;
    }

    @Override public List<ApplicationMetric> getApplications(Step step, long startTimeBucket,
        long endTimeBucket, MetricSource metricSource) {
        H2Client client = getClient();
        String tableName = TimePyramidTableNameBuilder.build(step, ApplicationMetricTable.TABLE);
        String sql = SqlBuilder.buildSql(GET_APPLICATIONS_SQL, ApplicationMetricTable.COLUMN_APPLICATION_ID,
                ApplicationMetricTable.COLUMN_TRANSACTION_CALLS,ApplicationMetricTable.COLUMN_TRANSACTION_ERROR_CALLS,
                ApplicationMetricTable.COLUMN_TRANSACTION_DURATION_SUM,ApplicationMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM,
                ApplicationMetricTable.COLUMN_SATISFIED_COUNT,ApplicationMetricTable.COLUMN_TOLERATING_COUNT,
                ApplicationMetricTable.COLUMN_FRUSTRATED_COUNT,tableName,
                ApplicationMetricTable.COLUMN_TIME_BUCKET, InstanceMetricTable.COLUMN_SOURCE_VALUE);

        List<ApplicationMetric> applicationMetrics = new LinkedList<>();
        Object[] params = new Object[] {startTimeBucket, endTimeBucket,metricSource.getValue(),100};

        try (ResultSet rs = client.executeQuery(sql, params)) {
            while (rs.next()) {
                int applicationId = rs.getInt(ApplicationMetricTable.COLUMN_APPLICATION_ID);
                long calls = rs.getLong(ApplicationMetricTable.COLUMN_TRANSACTION_CALLS);
                long errorCalls = rs.getLong(ApplicationMetricTable.COLUMN_TRANSACTION_ERROR_CALLS);
                long durations = rs.getLong(ApplicationMetricTable.COLUMN_TRANSACTION_DURATION_SUM);
                long errorDurations = rs.getLong(ApplicationMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM);
                long satisfiedCount = rs.getLong(ApplicationMetricTable.COLUMN_SATISFIED_COUNT);
                long toleratingCount = rs.getLong(ApplicationMetricTable.COLUMN_TOLERATING_COUNT);
                long frustratedCount = rs.getLong(ApplicationMetricTable.COLUMN_FRUSTRATED_COUNT);
                ApplicationMetric applicationMetric = new ApplicationMetric();
                applicationMetric.setId(applicationId);
                applicationMetric.setCalls(calls);
                applicationMetric.setErrorCalls(errorCalls);
                applicationMetric.setDurations(durations);
                applicationMetric.setErrorDurations(errorDurations);
                applicationMetric.setSatisfiedCount(satisfiedCount);
                applicationMetric.setToleratingCount(toleratingCount);
                applicationMetric.setToleratingCount(frustratedCount);
                applicationMetrics.add(applicationMetric);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return applicationMetrics;
    }
}
