package by.bsu.machulski.dto;

import by.bsu.machulski.type.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderDTO extends AbstractDTO {
    private long userId;
    private String userEmail;
    private Date placementDate;
    private List<ProductDTO> products;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public OrderDTO() {
    }

    public OrderDTO(long id, long userId, String userEmail, Date placementDate, List<ProductDTO> products, BigDecimal totalPrice, OrderStatus status) {
        super(id);
        this.userId = userId;
        this.userEmail = userEmail;
        this.placementDate = placementDate;
        this.products = products;
        this.totalPrice = totalPrice;
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

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDTO orderDTO = (OrderDTO) o;

        if (userId != orderDTO.userId) return false;
        if (userEmail != null ? !userEmail.equals(orderDTO.userEmail) : orderDTO.userEmail != null) return false;
        if (placementDate != null ? !placementDate.equals(orderDTO.placementDate) : orderDTO.placementDate != null)
            return false;
        if (products != null ? !products.equals(orderDTO.products) : orderDTO.products != null) return false;
        if (totalPrice != null ? !totalPrice.equals(orderDTO.totalPrice) : orderDTO.totalPrice != null) return false;
        return status == orderDTO.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (placementDate != null ? placementDate.hashCode() : 0);
        result = 31 * result + (products != null ? products.hashCode() : 0);
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
