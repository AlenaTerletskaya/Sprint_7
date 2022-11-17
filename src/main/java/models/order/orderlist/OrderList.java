package models.order.orderlist;

public class OrderList {

    private OrderForList[] orders;
    private PageInfo pageInfo;
    private AvailableStation[] availableStations;

    public OrderList(OrderForList[] orders, PageInfo pageInfo, AvailableStation[] availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }

    public OrderList() {}


    public OrderForList[] getOrders() {
        return orders;
    }

    public void setOrders(OrderForList[] orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public AvailableStation[] getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(AvailableStation[] availableStations) {
        this.availableStations = availableStations;
    }
}
