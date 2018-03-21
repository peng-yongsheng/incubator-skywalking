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
import org.apache.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.apache.skywalking.apm.collector.storage.base.sql.SqlBuilder;
import org.apache.skywalking.apm.collector.storage.dao.ui.IInstanceAlarmUIDAO;
import org.apache.skywalking.apm.collector.storage.h2.base.dao.H2DAO;
import org.apache.skywalking.apm.collector.storage.table.alarm.InstanceAlarmTable;
import org.apache.skywalking.apm.collector.storage.ui.alarm.Alarm;
import org.apache.skywalking.apm.collector.storage.ui.alarm.AlarmItem;
import org.apache.skywalking.apm.collector.storage.ui.alarm.AlarmType;
import org.apache.skywalking.apm.collector.storage.ui.alarm.CauseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author peng-yongsheng
 */
public class InstanceAlarmH2UIDAO extends H2DAO implements IInstanceAlarmUIDAO {
    private final Logger logger = LoggerFactory.getLogger(InstanceAlarmH2UIDAO.class);

    public InstanceAlarmH2UIDAO(H2Client client) {
        super(client);
    }
    /**
     * @author wen-gang.ji
     */
    @Override public Alarm loadAlarmList(String keyword, long startTimeBucket, long endTimeBucket, int limit, int from)throws ParseException {
        String tableName = InstanceAlarmTable.TABLE;

        H2Client client = getClient();
        String dynamicSql = "select * from {0} where {1} >= ?  and {1} <= ? and {2} like ? limit ? , ? ";
        String sql = SqlBuilder.buildSql(dynamicSql, tableName, InstanceAlarmTable.COLUMN_LAST_TIME_BUCKET, InstanceAlarmTable.COLUMN_ALARM_CONTENT);
        Object[] params = new Object[] {startTimeBucket, endTimeBucket, keyword, from,limit};
        Alarm alarm = new Alarm();

        try (ResultSet rs = client.executeQuery(sql, params)) {
            alarm.setTotal(rs.getRow());
            while (rs.next()) {
                AlarmItem alarmItem = new AlarmItem();
                alarmItem.setId(rs.getInt(InstanceAlarmTable.COLUMN_INSTANCE_ID));
                alarmItem.setTitle(rs.getString(InstanceAlarmTable.COLUMN_ALARM_CONTENT));
                alarmItem.setContent(rs.getString(InstanceAlarmTable.COLUMN_ALARM_CONTENT));
                alarmItem.setStartTime(TimeBucketUtils.INSTANCE.formatMinuteTimeBucket(rs.getLong(InstanceAlarmTable.COLUMN_LAST_TIME_BUCKET)));
                alarmItem.setAlarmType(AlarmType.SERVER);

                int alarmType = rs.getInt(InstanceAlarmTable.COLUMN_ALARM_TYPE);
                if (org.apache.skywalking.apm.collector.storage.table.alarm.AlarmType.SLOW_RTT.getValue() == alarmType) {
                    alarmItem.setCauseType(CauseType.SLOW_RESPONSE);
                } else if (org.apache.skywalking.apm.collector.storage.table.alarm.AlarmType.ERROR_RATE.getValue() == alarmType) {
                    alarmItem.setCauseType(CauseType.LOW_SUCCESS_RATE);
                }

                alarm.getItems().add(alarmItem);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }

        return alarm;
    }
}
