package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by xiaofengxu on 16/12/30.
 */
@ApiModel
@Data
public class SearchAffairMemberConditions {
    @ApiModelProperty(notes = "成员用户名关键字、角色title关键字，接受缩写")
    private String key;

    @ApiModelProperty(notes = "列表显示页码，从1开始")
    private int page;

    @ApiModelProperty(notes = "每页显示数量，10-100之间")
    private int count;

    @ApiModelProperty(notes = "按哪一信息项进行排序，可选值为 name , gender , role , affair")
    private String sortColumn = "name";

    @ApiModelProperty(notes = "是否反序排序，不填默认为正序")
    private boolean isReverseSort;

    @ApiModelProperty(notes = "是否包含子事务，不填默认不包含")
    private boolean includeSubAffair;

}
