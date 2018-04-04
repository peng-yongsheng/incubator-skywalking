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
import org.apache.skywalking.apm.collector.storage.dao.ui.IApplicationAlarmListUIDAO;
import org.apache.skywalking.apm.collector.storage.h2.base.dao.H2DAO;
import org.apache.skywalking.apm.collector.storage.table.alarm.ApplicationAlarmListTable;
import org.apache.skywalking.apm.collector.storage.table.service.ServiceMetricTable;
import org.apache.skywalking.apm.collector.storage.ui.common.Step;
import org.apache.skywalking.apm.collector.storage.utils.TimePyramidTableNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wen-gang.ji
 */
public class ApplicationAlarmListH2UIDAO extends H2DAO implements IApplicationAlarmListUIDAO {
    private final Logger logger = LoggerFactory.getLogger(ApplicationAlarmListH2UIDAO.class);

    public ApplicationAlarmListH2UIDAO(H2Client client) {
        super(client);
    }


    @Override public List<AlarmTrend> getAlarmedApplicationNum(Step step, long startTimeBucket, long endTimeBucket) {
        String tableName = TimePyramidTableNameBuilder.build(step, ServiceMetricTable.TABLE);

        H2Client client = getClient();
        String dynamicSql = "select {2} ,{1} from {0} where {1} >= ? and {1} <= ? group by {2},{1} limit 100";
        String sql = SqlBuilder.buildSql(dynamicSql, tableName, ApplicationAlarmListTable.COLUMN_TIME_BUCKET,ApplicationAlarmListTable.COLUMN_APPLICATION_ID);

        Object[] params = new Object[] {startTimeBucket, endTimeBucket};
        List<AlarmTrend> alarmTrends = new LinkedList<>();

        try (ResultSet rs = client.executeQuery(sql, params)) {
            while (rs.next()) {
                AlarmTrend alarmTrend = new AlarmTrend();
                alarmTrend.setNumberOfApplication(rs.getInt(ApplicationAlarmListTable.COLUMN_APPLICATION_ID));
                alarmTrend.setTimeBucket(rs.getLong(ApplicationAlarmListTable.COLUMN_TIME_BUCKET));
                alarmTrends.add(alarmTrend);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }

        return alarmTrends;



    }
}
