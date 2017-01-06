package cn.superid.webapp.controller.VO;

import com.taobao.api.internal.mapping.ApiField;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by jessiechen on 06/01/17.
 */
@ApiModel
public class ListVO<T> {
    @ApiModelProperty(notes = "分页列表")
    private List<T> list;
    @ApiModelProperty(notes = "当前页")
    private int page;
    @ApiModelProperty(notes = "当前页大小")
    private int count;
    @ApiModelProperty(notes = "全部搜索结果大小，如果请求时未要求获取则返回为小于0的值")
    private int total = -1;

    public ListVO() {
    }

    public ListVO(List<T> list, int page, int count, int total) {
        this.list = list;
        this.page = page;
        this.count = count;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
