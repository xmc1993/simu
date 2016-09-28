package cn.superid.webapp.service;


import cn.superid.webapp.enums.MessageColumn;
import com.aliyun.openservices.ots.ClientException;
import com.aliyun.openservices.ots.ServiceException;
import com.aliyun.openservices.ots.model.Row;

import java.util.HashMap;
import java.util.List;

/**
 * Created by njuTms on 16/9/23.
 */
public interface IMessageService {

    public void insertIntoTable(Long toUserId,Long relatedId, HashMap<String,Object> params);
    public void insertIntoTable(Long toUserId,Long relatedId, Long create_time, HashMap<String,Object> params)
            throws ServiceException, ClientException;
    /**
     * 根据相关的通知id进行搜索,比如任务提醒,公告提醒
     * @param toUserId 被通知的人,一般是自己
     * @param relatedId 相关的id
     */
    public List<Row> getFromTable(Long toUserId, Long relatedId);

    public List<Row> getFromTable(Long toUserId, Long relatedId,Integer limit);

    /**
     * 查看所有通知
     * @param toUserId
     */
    public List<Row> getFromTable(Long toUserId);

    public List<Row> getFromTable(Long toUserId,Integer limit);

    /**
     * 以后可能会用到的根据时间段查看过往的通知
     * @param toUserId
     * @param relatedId
     * @param startTime
     * @param endTime
     * @param limit 如果为null或者-1,则不限制条目
     * @throws ServiceException
     * @throws ClientException
     */
    public List<Row> getFromTable(Long toUserId,Long relatedId,Long startTime,Long endTime,Integer limit)
            throws ServiceException, ClientException;


    /**
     *
     * @param toUserId
     * @param relatedId
     * @param messageColumn 列名
     * @param columnValue 列值
     * @return
     * @throws ServiceException
     * @throws ClientException
     */
    public List<Row> getFromTableByColumnName(Long toUserId, Long relatedId, MessageColumn messageColumn,Integer columnValue)
            throws ServiceException, ClientException;


}
