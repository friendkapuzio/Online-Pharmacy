package by.bsu.machulski.entity;

import by.bsu.machulski.type.OrderStatus;

import java.util.Date;

public class Order extends Entity {
    private long userId;
    private Date placementDate;
    private OrderStatus status;

    public Order() {
    }

    public Order(long id, long userId, Date placementDate, OrderStatus status) {
        super(id);
        this.userId = userId;
        this.placementDate = placementDate;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Order order = (Order) o;

        if (userId != order.userId) return false;
        if (placementDate != null ? !placementDate.equals(order.placementDate) : order.placementDate != null)
            return false;
        return status == order.status;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (placementDate != null ? placementDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
