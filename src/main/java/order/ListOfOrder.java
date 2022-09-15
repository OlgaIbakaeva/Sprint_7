package order;

import java.util.List;

public class ListOfOrder {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;

    public ListOfOrder(List<Order> orders, PageInfo pageInfo, List<AvailableStation> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }
    public ListOfOrder() {
    }
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStation> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStation> availableStations) {
        this.availableStations = availableStations;
    }
}
