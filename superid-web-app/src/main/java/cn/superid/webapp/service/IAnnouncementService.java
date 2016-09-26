package cn.superid.webapp.service;

import cn.superid.webapp.service.forms.Block;
import cn.superid.webapp.service.forms.Operation;
import cn.superid.webapp.service.forms.OperationListForm;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public interface IAnnouncementService {

    public OperationListForm compareTwoPapers(List<Block> present , List<Block> history );

    public List<Block> paperToBlockList(String content);
}
