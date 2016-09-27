package cn.superid.webapp.service.impl;

import cn.superid.webapp.service.IMessageService;
import cn.superid.webapp.utils.AliOTSDao;
import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.ServiceException;
import com.aliyun.openservices.ots.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by njuTms on 16/9/26.
 */
@Service
public class MessageService implements IMessageService{
    private final static String TABLE_NAME = "message";

    private final static String TO_USER_ID = "to_user_id";
    private final static String RELATED_ID = "related_id";
    private final static String CREATE_TIME = "create_time";
    private OTSClient client = AliOTSDao.otsClient;

    @Override
    public void insertIntoTable(Long toUserId, Long relatedId, HashMap<String, Object> params) {
        insertIntoTable(toUserId,relatedId,System.currentTimeMillis(),params);
    }

    @Override
    public void insertIntoTable(Long toUserId, Long relatedId, Long create_time, HashMap<String, Object> params)
            throws ServiceException, ClientException {
        if(toUserId == null){
            throw new ServiceException("无通知用户");
        }
        RowPutChange rowChange = new RowPutChange(TABLE_NAME);
        RowPrimaryKey primaryKey = new RowPrimaryKey();
        primaryKey.addPrimaryKeyColumn(TO_USER_ID, PrimaryKeyValue.fromLong(toUserId));
        primaryKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
        primaryKey.addPrimaryKeyColumn(CREATE_TIME,PrimaryKeyValue.fromLong(create_time));
        rowChange.setPrimaryKey(primaryKey);
        for(String key : params.keySet()){
            Object o = params.get(key);
            if(o instanceof Integer){
                rowChange.addAttributeColumn(key, ColumnValue.fromLong((long) ((int)o)));
            }
            else if(o instanceof Long){
                rowChange.addAttributeColumn(key, ColumnValue.fromLong((long) o));
            }
            else if(o instanceof String){
                rowChange.addAttributeColumn(key,ColumnValue.fromString((String) o));
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
    public List<Row> getFromTable(Long toUserId, Long relatedId) {
        return getFromTable(toUserId,relatedId,null,null);
    }

    @Override
    public List<Row> getFromTable(Long toUserId) {
        return getFromTable(toUserId,null,null,null);
    }

    @Override
    public List<Row> getFromTable(Long toUserId, Long relatedId, Long startTime, Long endTime)
            throws ServiceException, ClientException {
        if(toUserId == null){
            throw new ServiceException("无通知用户");
        }
        RangeRowQueryCriteria criteria = new RangeRowQueryCriteria(TABLE_NAME);
        RowPrimaryKey inclusiveStartKey = new RowPrimaryKey();
        inclusiveStartKey.addPrimaryKeyColumn(TO_USER_ID,
                PrimaryKeyValue.fromLong(toUserId));
        //起始时间
        if(startTime == null){
            inclusiveStartKey.addPrimaryKeyColumn(CREATE_TIME,PrimaryKeyValue.INF_MIN);
        }
        else {
            inclusiveStartKey.addPrimaryKeyColumn(CREATE_TIME,PrimaryKeyValue.fromLong(startTime));
        }


        RowPrimaryKey exclusiveEndKey = new RowPrimaryKey();
        exclusiveEndKey.addPrimaryKeyColumn(TO_USER_ID,
                PrimaryKeyValue.fromLong(toUserId));
        //结束时间
        if(endTime == null){
            exclusiveEndKey.addPrimaryKeyColumn(CREATE_TIME,PrimaryKeyValue.INF_MAX);
        }
        else {
            exclusiveEndKey.addPrimaryKeyColumn(CREATE_TIME,PrimaryKeyValue.fromLong(endTime));
        }

        // 范围的边界需要提供完整的PK，若查询的范围不涉及到某一列值的范围，则需要将该列设置为无穷大或者无穷小
        if(relatedId == null){
            inclusiveStartKey.addPrimaryKeyColumn(RELATED_ID,PrimaryKeyValue.INF_MIN);
            exclusiveEndKey.addPrimaryKeyColumn(RELATED_ID,PrimaryKeyValue.INF_MAX);
        }
        else{
            inclusiveStartKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
            exclusiveEndKey.addPrimaryKeyColumn(RELATED_ID, PrimaryKeyValue.fromLong(relatedId));
        }


        criteria.setInclusiveStartPrimaryKey(inclusiveStartKey);
        criteria.setExclusiveEndPrimaryKey(exclusiveEndKey);
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
    public List<Row> getFromTableByType(Long toUserId, Long relatedId, Integer type) throws ServiceException, ClientException {
        return null;
    }

}
