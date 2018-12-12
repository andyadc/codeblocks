package com.andyadc.codeblocks.framework.mybatis.pagination;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class PageBounds extends RowBounds implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static int NO_PAGE = 1;
    /**
     * 页号
     */
    protected int page = NO_PAGE;
    /**
     * 分页大小
     */
    protected int limit = NO_ROW_LIMIT;

    /**
     * 结果集是否包含TotalCount
     */
    protected boolean containsTotalCount;

    /**
     * 是否异步统计
     */
    protected Boolean asyncTotalCount;

    /**
     * 分页排序信息
     */
    protected List<Order> orders = new ArrayList<>();

    public PageBounds() {
        containsTotalCount = false;
    }

    public PageBounds(List<Order> orders) {
        this(NO_PAGE, NO_ROW_LIMIT, orders, false);
    }

    public PageBounds(int page, int limit) {
        this(page, limit, new ArrayList<>(), false);
    }

    public PageBounds(int page, int limit, boolean containsTotalCount) {
        this(page, limit, new ArrayList<>(), containsTotalCount);
    }

    public PageBounds(int page, int limit, List<Order> orders, boolean containsTotalCount) {
        this.page = page;
        this.limit = limit;
        this.orders = orders;
        this.containsTotalCount = containsTotalCount;
    }

    public PageBounds(RowBounds rowBounds) {
        if (rowBounds instanceof PageBounds) {
            PageBounds pageBounds = (PageBounds) rowBounds;
            this.page = pageBounds.page;
            this.limit = pageBounds.limit;
            this.orders = pageBounds.orders;
            this.containsTotalCount = pageBounds.containsTotalCount;
            this.asyncTotalCount = pageBounds.asyncTotalCount;
        } else {
            this.page = (rowBounds.getOffset() / rowBounds.getLimit()) + 1;
            this.limit = rowBounds.getLimit();
        }
    }

    public int startIndex() {
        if (page >= 1) {
            return (page - 1) * limit;
        }
        return 0;
    }

//    @Override
//    public int getOffset() {
//        if (page >= 1) {
//            return (page - 1) * limit;
//        }
//        return 0;
//    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isContainsTotalCount() {
        return containsTotalCount;
    }

    public void setContainsTotalCount(boolean containsTotalCount) {
        this.containsTotalCount = containsTotalCount;
    }

    public Boolean getAsyncTotalCount() {
        return asyncTotalCount;
    }

    public void setAsyncTotalCount(Boolean asyncTotalCount) {
        this.asyncTotalCount = asyncTotalCount;
    }
}
