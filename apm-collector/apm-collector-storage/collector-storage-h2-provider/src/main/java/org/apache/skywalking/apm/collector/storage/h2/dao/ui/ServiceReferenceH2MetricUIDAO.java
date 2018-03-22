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
import org.apache.skywalking.apm.collector.storage.dao.ui.IServiceReferenceMetricUIDAO;
import org.apache.skywalking.apm.collector.storage.h2.base.dao.H2DAO;
import org.apache.skywalking.apm.collector.storage.table.MetricSource;
import org.apache.skywalking.apm.collector.storage.table.service.ServiceReferenceMetricTable;
import org.apache.skywalking.apm.collector.storage.ui.common.Step;
import org.apache.skywalking.apm.collector.storage.utils.TimePyramidTableNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author peng-yongsheng
 */
public class ServiceReferenceH2MetricUIDAO extends H2DAO implements IServiceReferenceMetricUIDAO {
    private final Logger logger = LoggerFactory.getLogger(ApplicationMetricH2UIDAO.class);

    private static final String GET_SERVICEREFERENCEMETRIC_SQL=
            "select {0}, sum({1}) as {1},sum({2}) as {2},sum({3}) as {3}, sum({4}) as {4}" +
                    " from {5} where {6} >= ? and {6} <= ? " +
                    " and {7} = ? and {8} = ?  group by {0} limit 100";
    public ServiceReferenceH2MetricUIDAO(H2Client client) {
        super(client);
    }

    @Override public List<ServiceReferenceMetric> getFrontServices(Step step, long startTimeBucket, long endTimeBucket,
        MetricSource metricSource,
        int behindServiceId) {
        H2Client client = getClient();
        String tableName = TimePyramidTableNameBuilder.build(step, ServiceReferenceMetricTable.TABLE);
        String sql = SqlBuilder.buildSql(GET_SERVICEREFERENCEMETRIC_SQL, ServiceReferenceMetricTable.COLUMN_FRONT_SERVICE_ID,
                ServiceReferenceMetricTable.COLUMN_TRANSACTION_CALLS, ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_CALLS,
                ServiceReferenceMetricTable.COLUMN_TRANSACTION_DURATION_SUM,ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM,
                tableName, ServiceReferenceMetricTable.COLUMN_TIME_BUCKET, ServiceReferenceMetricTable.COLUMN_BEHIND_SERVICE_ID,
                ServiceReferenceMetricTable.COLUMN_SOURCE_VALUE);
        List<ServiceReferenceMetric> referenceMetrics = new LinkedList<>();
        Object[] params = new Object[] {startTimeBucket, endTimeBucket,behindServiceId,metricSource.getValue()};
        try (ResultSet rs = client.executeQuery(sql, params)) {
            while (rs.next()) {
                int frontServiceId = rs.getInt(ServiceReferenceMetricTable.COLUMN_FRONT_SERVICE_ID);
                long callsSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_CALLS);
                long errorCallsSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_CALLS);
                long durationSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_DURATION_SUM);
                long errorDurationSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM);

                ServiceReferenceMetric referenceMetric = new ServiceReferenceMetric();
                referenceMetric.setTarget(behindServiceId);
                referenceMetric.setSource(frontServiceId);
                referenceMetric.setCalls(callsSum);
                referenceMetric.setErrorCalls(errorCallsSum);
                referenceMetric.setDurations(durationSum);
                referenceMetric.setErrorDurations(errorDurationSum);
                referenceMetrics.add(referenceMetric);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return referenceMetrics;
    }

    @Override public List<ServiceReferenceMetric> getBehindServices(Step step, long startTimeBucket, long endTimeBucket,
        MetricSource metricSource,
        int frontServiceId) {

        H2Client client = getClient();
        String tableName = TimePyramidTableNameBuilder.build(step, ServiceReferenceMetricTable.TABLE);
        String sql = SqlBuilder.buildSql(GET_SERVICEREFERENCEMETRIC_SQL, ServiceReferenceMetricTable.COLUMN_BEHIND_SERVICE_ID,
                ServiceReferenceMetricTable.COLUMN_TRANSACTION_CALLS, ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_CALLS,
                ServiceReferenceMetricTable.COLUMN_TRANSACTION_DURATION_SUM,ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM,
                tableName, ServiceReferenceMetricTable.COLUMN_TIME_BUCKET, ServiceReferenceMetricTable.COLUMN_FRONT_SERVICE_ID,
                ServiceReferenceMetricTable.COLUMN_SOURCE_VALUE);
        List<ServiceReferenceMetric> referenceMetrics = new LinkedList<>();
        Object[] params = new Object[] {startTimeBucket, endTimeBucket,frontServiceId,metricSource.getValue()};
        try (ResultSet rs = client.executeQuery(sql, params)) {
            while (rs.next()) {
                int behindServiceId = rs.getInt(ServiceReferenceMetricTable.COLUMN_BEHIND_SERVICE_ID);
                long callsSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_CALLS);
                long errorCallsSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_CALLS);
                long durationSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_DURATION_SUM);
                long errorDurationSum = rs.getLong(ServiceReferenceMetricTable.COLUMN_TRANSACTION_ERROR_DURATION_SUM);

                ServiceReferenceMetric referenceMetric = new ServiceReferenceMetric();
                referenceMetric.setTarget(behindServiceId);
                referenceMetric.setSource(frontServiceId);
                referenceMetric.setCalls(callsSum);
                referenceMetric.setErrorCalls(errorCallsSum);
                referenceMetric.setDurations(durationSum);
                referenceMetric.setErrorDurations(errorDurationSum);
                referenceMetrics.add(referenceMetric);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return referenceMetrics;
    }
}
