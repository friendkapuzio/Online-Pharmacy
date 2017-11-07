package by.bsu.machulski.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CartDTO {
    private Map<Long, Integer> products = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;

    public CartDTO() {
    }

    public CartDTO(Map<Long, Integer> products, BigDecimal total) {
        this.products = products;
        this.total = total;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
