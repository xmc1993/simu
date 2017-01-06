package cn.superid.jpa.util;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public class Pagination {
    private int page =1;
    private int size =10;
    private int total = 0;


    public int getPage() {
        return page;
    }
    public Pagination(){}

    public  Pagination(int page,int size){
        this.page =page;
        this.size =size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset(){
        return (page-1)*size;
    }
}
