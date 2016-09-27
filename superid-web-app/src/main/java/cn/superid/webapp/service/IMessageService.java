package cn.superid.webapp.service;


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

    /**
     * 查看所有通知
     * @param toUserId
     */
    public List<Row> getFromTable(Long toUserId);

    /**
     * 以后可能会用到的根据时间段查看过往的通知
     * @param toUserId
     * @param relatedId
     * @param startTime
     * @param endTime
     * @throws ServiceException
     * @throws ClientException
     */
    public List<Row> getFromTable(Long toUserId,Long relatedId,Long startTime,Long endTime)
            throws ServiceException, ClientException;


    public List<Row> getFromTableByType(Long toUserId,Long relatedId,Integer type)
            throws ServiceException, ClientException;


}
