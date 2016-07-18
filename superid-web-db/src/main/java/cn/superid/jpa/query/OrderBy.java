package cn.superid.jpa.query;
public class OrderBy {
    private String sort = null;
    private boolean asc = false;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public OrderBy(String sort, boolean asc) {
        this.sort = sort;
        this.asc = asc;
    }

    public String toOrderByString(cn.superid.jpa.query.Query query) {
        return sort + " " + (asc ? "asc" : "desc");
    }
}
