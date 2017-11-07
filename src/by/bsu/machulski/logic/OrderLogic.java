package by.bsu.machulski.logic;

import by.bsu.machulski.dao.OrderDAO;
import by.bsu.machulski.dao.PrescriptionDAO;
import by.bsu.machulski.dao.ProductDAO;
import by.bsu.machulski.dao.UserDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.dto.OrderDTO;
import by.bsu.machulski.dto.ProductDTO;
import by.bsu.machulski.entity.Order;
import by.bsu.machulski.entity.Prescription;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.OrderOperationStatus;
import by.bsu.machulski.type.OrderStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class OrderLogic {
    public boolean changeStatus(String orderId, String newStatus) throws LogicException, IllegalArgumentException {
        OrderStatus.valueOf(newStatus.toUpperCase());
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new OrderDAO(connection).changeStatus(Long.parseLong(orderId), newStatus);
        } catch (DAOException e) {
            throw new LogicException("Failure to change order's status, id: " + orderId, e);
        }
    }

    public OrderOperationStatus createOrder(long userId, CartDTO cart) throws LogicException {
        OrderOperationStatus result = OrderOperationStatus.ERROR;
        if (new CartLogic().checkProducts(userId, cart)) {
            ProxyConnection connection = ConnectionPool.getInstance().takeConnection();
            try {
                connection.setAutoCommit(false);
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.findById(userId).get().getBalance().compareTo(cart.getTotal()) >= 0 && userDAO.modifyBalance(userId, cart.getTotal().negate())) {
                    OrderDAO orderDAO = new OrderDAO(connection);
                    long orderId = orderDAO.create(userId);
                    ProductDAO productDAO = new ProductDAO(connection);
                    PrescriptionDAO prescriptionDAO = new PrescriptionDAO(connection);
                    boolean isCreated = true;
                    for (Map.Entry<Long, Integer> entry:cart.getProducts().entrySet()) {
                        Product product = productDAO.findById(entry.getKey()).get();
                        if (product.getIsPrescriptionRequired()) {
                            List<Prescription> prescriptions = prescriptionDAO.findActualByUserAndProduct(userId, entry.getKey());
                            int requiredAmount = entry.getValue();
                            for (Prescription prescription:prescriptions) {
                                if (requiredAmount > 0 && prescription.getAmount() > requiredAmount) {
                                    isCreated = isCreated & prescriptionDAO.updateAmount(prescription.getId(), requiredAmount);
                                    isCreated = isCreated & prescriptionDAO.updateIsUsed(prescription.getId(), true);
                                    isCreated = isCreated & prescriptionDAO.create(prescription.getPatientId(),
                                            prescription.getDoctorId(),
                                            prescription.getProductId(),
                                            prescription.getAmount() - requiredAmount,
                                            prescription.getExpirationDate());
                                    requiredAmount = 0;
                                } else if (requiredAmount > 0 && prescription.getAmount() == requiredAmount) {
                                    isCreated = isCreated & prescriptionDAO.updateIsUsed(prescription.getId(), true);
                                    requiredAmount = 0;
                                } else if (requiredAmount > 0) {
                                    isCreated = isCreated & prescriptionDAO.updateIsUsed(prescription.getId(), true);
                                    requiredAmount -= prescription.getAmount();
                                }
                            }
                            if (requiredAmount !=0) {
                                isCreated = false;
                            }
                        }
                        isCreated = isCreated & productDAO.modifyQuantity(product.getId(), -entry.getValue());
                        isCreated = isCreated & orderDAO.addProductInfo(orderId, product.getId(), product.getPrice(), entry.getValue());
                    }
                    if (isCreated) {
                        connection.commit();
                        result = OrderOperationStatus.ACCEPTED;
                    } else {
                        connection.rollback();
                    }
                } else {
                    result = OrderOperationStatus.INSUFFICIENT_FUNDS;
                }
                connection.setAutoCommit(true);
            } catch (SQLException | DAOException e) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    throw new LogicException("Failure to create order", e);
                } catch (SQLException e1) {
                    throw new LogicException("Failure to rollback order creation", e1);
                }
            } finally {
                connection.close();
            }
        }
        return result;
    }

    public List<OrderDTO> findOrders(String status) throws LogicException, IllegalArgumentException {
        OrderStatus.valueOf(status.toUpperCase());
        List<OrderDTO> orderDTOs = null;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            OrderDAO orderDAO = new OrderDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            List<Order> orders = orderDAO.findByStatus(status.toUpperCase());
            orderDTOs = new ArrayList<>();
            for (Order order:orders) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(order.getId());
                orderDTO.setPlacementDate(order.getPlacementDate());
                orderDTO.setUserId(order.getUserId());
                orderDTO.setStatus(order.getStatus());
                List<ProductDTO> productsInfo = orderDAO.findProductsInfo(order.getId());
                User user = userDAO.findById(order.getUserId()).get();
                orderDTO.setUserEmail(user.getEmail());
                orderDTO.setProducts(productsInfo);
                BigDecimal totalPrice = BigDecimal.ZERO;
                for (ProductDTO product:productsInfo) {
                    totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getAmount())));
                }
                orderDTO.setTotalPrice(totalPrice);
                orderDTOs.add(orderDTO);
                orderDTOs.sort(Comparator.comparing(OrderDTO::getPlacementDate).reversed());
            }
        } catch (DAOException e) {
            throw new LogicException("Failure to find orders, orders status: " + status, e);
        }
        return orderDTOs;
    }

    public List<OrderDTO> findOrders(long userId, String ordersYear) throws LogicException {
        List<OrderDTO> orderDTOs = null;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            OrderDAO orderDAO = new OrderDAO(connection);
            List<Order> orders = orderDAO.findByUserAndYear(userId, Integer.parseInt(ordersYear));
            orderDTOs = new ArrayList<>();
            for (Order order:orders) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(order.getId());
                orderDTO.setPlacementDate(order.getPlacementDate());
                orderDTO.setUserId(order.getUserId());
                orderDTO.setStatus(order.getStatus());
                List<ProductDTO> productsInfo = orderDAO.findProductsInfo(order.getId());
                orderDTO.setProducts(productsInfo);
                BigDecimal totalPrice = BigDecimal.ZERO;
                for (ProductDTO product:productsInfo) {
                    totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getAmount())));
                }
                orderDTO.setTotalPrice(totalPrice);
                orderDTOs.add(orderDTO);
                orderDTOs.sort(Comparator.comparing(OrderDTO::getPlacementDate).reversed());
            }
        } catch (DAOException e) {
            throw new LogicException("Failure to find orders, user id: " + userId + ", orders year: " + ordersYear, e);
        }
        return orderDTOs;
    }
}
