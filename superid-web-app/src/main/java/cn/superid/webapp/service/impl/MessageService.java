package cn.superid.webapp.service.impl;

import cn.superid.webapp.enums.MessageColumn;
import cn.superid.webapp.service.IMessageService;
import cn.superid.webapp.service.IRedisMessageService;
import cn.superid.webapp.utils.AliOTSDao;
import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.ServiceException;
import com.aliyun.openservices.ots.model.*;
import com.aliyun.openservices.ots.model.condition.RelationalCondition;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by njuTms on 16/9/26.
 */
@Service
public class MessageService implements IMessageService {
    @Autowired
    private IRedisMessageService redisMessageService;

    private final static String TABLE_NAME = "message";

    private final static String TO_USER_ID = "to_user_id";
    private final static String RELATED_ID = "related_id";
    private final static String CREATE_TIME = "create_time";
    private final static String AFFAIR_ID = "affair_id";


    private final static Direction direction = Direction.BACKWARD;
    private OTSClient client = AliOTSDao.otsClient;

    @Override
    public void insertIntoTable(Long toUserId, Long relatedId, HashMap<String, Object> params) {
        insertIntoTable(toUserId, relatedId, System.currentTimeMillis(), params);
    }

    public void insertIntoTable(Long toUserId, Long affairId, Long relatedId, HashMap<String, Object> params) {
        insertIntoTable(toUserId, affairId, relatedId, System.currentTimeMillis(), params);

    }

    @Override
    public void insertIntoTable(Long toUserId, Long affairId, Long relatedId, Long create_time, HashMap<String, Object> params)
            throws ServiceException, ClientException {
        if (toUserId == null) {
            throw new ServiceException("无通知用户");
        }
        if (affairId == null) {
            throw new ServiceException("无归属事务");
        }
        RowPutChange rowChange = new RowPutChange(TABLE_NAME);
        RowPrimaryKey primaryKey = new RowPrimaryKey();
        primaryKey.addPrimaryKeyColumn(TO_USER_ID, PrimaryKeyValue.fromLong(toUserId));
        primaryKey.addPrimaryKeyColumn(AFFAIR_ID, PrimaryKeyValue.fromLong(affairId));
        primaryKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
        primaryKey.addPrimaryKeyColumn(CREATE_TIME, PrimaryKeyValue.fromLong(create_time));
        rowChange.setPrimaryKey(primaryKey);
        for (String key : params.keySet()) {
            Object o = params.get(key);
            if (o instanceof Integer) {
                rowChange.addAttributeColumn(key, ColumnValue.fromLong((long) ((int) o)));
            } else if (o instanceof Long) {
                rowChange.addAttributeColumn(key, ColumnValue.fromLong((long) o));
            } else if (o instanceof String) {
                rowChange.addAttributeColumn(key, ColumnValue.fromString((String) o));
            }
        }
        rowChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_NOT_EXIST));

        PutRowRequest request = new PutRowRequest();
        request.setRowChange(rowChange);

        PutRowResult result = client.putRow(request);
        int consumedWriteCU = result.getConsumedCapacity().getCapacityUnit().getWriteCapacityUnit();

        System.out.println("成功插入数据, 消耗的写CapacityUnit为：" + consumedWriteCU);

    }

    @Override
    public List<Row> getFromTable(Long toUserId, Long affairId, Long relatedId) {
        return getFromTable(toUserId, affairId, relatedId, null, null, null);
    }

    @Override
    public List<Row> getFromTable(Long toUserId, Long affairId, Long relatedId, Integer limit) {
        return getFromTable(toUserId, affairId, relatedId, null, null, limit);
    }

    @Override
    public List<Row> getFromTable(Long toUserId, Long affairId) {
        return getFromTable(toUserId, affairId, null, null, null, null);
    }

    @Override
    public List<Row> getFromTable(Long toUserId) {
        return getFromTable(toUserId, null, null, null, null, null);
    }

    @Override
    public List<Row> getFromTable(Long toUserId, Integer limit) {
        return getFromTable(toUserId, null, null, null, null, limit);
    }

    @Override
    public List<Row> getFromTable(Long toUserId, Long affairId, Long relatedId, Long startTime, Long endTime) throws ServiceException, ClientException {
        return getFromTable(toUserId, affairId, relatedId, startTime, endTime, null);
    }


    @Override
    public List<Row> getFromTable(Long toUserId, Long affairId, Long relatedId, Long startTime, Long endTime, Integer limit)
            throws ServiceException, ClientException {
        if (toUserId == null) {
            throw new ServiceException("无通知用户");
        }


        //因为要倒序查看,所以需要startKey>endKey
        RangeRowQueryCriteria criteria = new RangeRowQueryCriteria(TABLE_NAME);
        //endTime作为开始
        RowPrimaryKey inclusiveStartKey = setStartKey(toUserId, affairId, relatedId, endTime);

        RowPrimaryKey exclusiveEndKey = setEndKey(toUserId, affairId, relatedId, startTime);


        criteria.setInclusiveStartPrimaryKey(inclusiveStartKey);
        criteria.setExclusiveEndPrimaryKey(exclusiveEndKey);
        if ((!(limit == null)) && (limit.intValue() > 0)) {
            criteria.setLimit(limit);
        }
        criteria.setDirection(direction);
        GetRangeRequest request = new GetRangeRequest();
        request.setRangeRowQueryCriteria(criteria);
        GetRangeResult result = client.getRange(request);
        List<Row> rows = result.getRows();


        int consumedReadCU = result.getConsumedCapacity().getCapacityUnit()
                .getReadCapacityUnit();
        System.out.println("本次读操作消耗的读CapacityUnit为：" + consumedReadCU);


        return rows;
    }

    @Override
    public List<Row> getFromTableByColumnName(Long toUserId, Long affairId, Long relatedId, MessageColumn messageColumn, Integer columnValue)
            throws ServiceException, ClientException {
        if (toUserId == null) {
            throw new ServiceException("无通知用户");
        }
        RangeIteratorParameter param = new RangeIteratorParameter(TABLE_NAME);
        RowPrimaryKey startPk = setStartKey(toUserId, affairId, relatedId, null);

        RowPrimaryKey endPk = setEndKey(toUserId, affairId, relatedId, null);

        param.setInclusiveStartPrimaryKey(startPk);
        param.setExclusiveEndPrimaryKey(endPk);
        param.setDirection(direction);
        RelationalCondition filter = new RelationalCondition(messageColumn.toString(), RelationalCondition.CompareOperator.EQUAL, ColumnValue.fromLong(columnValue));
        param.setFilter(filter);

        Iterator<Row> rowIter = client.createRangeIterator(param);
        List<Row> rows = new ArrayList<>();

        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            rows.add(row);
        }

        return rows;
    }

    private RowPrimaryKey setStartKey(Long toUserId, Long affairId, Long relatedId, Long endTime) {
        RowPrimaryKey inclusiveStartKey = new RowPrimaryKey();
        inclusiveStartKey.addPrimaryKeyColumn(TO_USER_ID,
                PrimaryKeyValue.fromLong(toUserId));

        if (affairId == null) {
            inclusiveStartKey.addPrimaryKeyColumn(AFFAIR_ID, PrimaryKeyValue.INF_MAX);
        } else {
            inclusiveStartKey.addPrimaryKeyColumn(AFFAIR_ID, PrimaryKeyValue.fromLong(affairId));
        }

        //因为要倒序查看,所以需要startKey>endKey
        //起始时间
        if (endTime == null) {
            inclusiveStartKey.addPrimaryKeyColumn(CREATE_TIME, PrimaryKeyValue.INF_MAX);
        } else {
            inclusiveStartKey.addPrimaryKeyColumn(CREATE_TIME, PrimaryKeyValue.fromLong(endTime));
        }

        if (relatedId == null) {
            inclusiveStartKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.INF_MAX);
        } else {
            inclusiveStartKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
        }
        return inclusiveStartKey;
    }

    private RowPrimaryKey setEndKey(Long toUserId, Long affairId, Long relatedId, Long startTime) {
        RowPrimaryKey exclusiveEndKey = new RowPrimaryKey();
        exclusiveEndKey.addPrimaryKeyColumn(TO_USER_ID,
                PrimaryKeyValue.fromLong(toUserId));

        if (affairId == null) {
            exclusiveEndKey.addPrimaryKeyColumn(AFFAIR_ID, PrimaryKeyValue.INF_MIN);
        } else {
            exclusiveEndKey.addPrimaryKeyColumn(AFFAIR_ID, PrimaryKeyValue.fromLong(affairId));
        }

        //结束时间


        if (startTime == null) {
            exclusiveEndKey.addPrimaryKeyColumn(CREATE_TIME, PrimaryKeyValue.INF_MIN);
        } else {
            exclusiveEndKey.addPrimaryKeyColumn(CREATE_TIME, PrimaryKeyValue.fromLong(startTime));
        }

        // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小
        if (relatedId == null) {
            exclusiveEndKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.INF_MIN);
        } else {
            exclusiveEndKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
        }
        return exclusiveEndKey;

    }

    public void referToSomebody(Long toUserId, Long affairId, Long relatedId, String content) {
        HashMap<String, Object> map = new HashMap<>();
        //TODO 这里需要哪些列,type,subtype,etc
        map.put("type", 1);
        map.put("content", content);
        insertIntoTable(toUserId, affairId, relatedId, map);
        //TODO 调用redis进行推送,不太清楚
        //redisMessageService.se
    }


}
