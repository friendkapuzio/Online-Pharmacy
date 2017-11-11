package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.dto.ProductDTO;
import by.bsu.machulski.entity.Order;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.type.OrderStatus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OrderDAO extends AbstractDAO<Order> {
    private static final Logger LOGGER = LogManager.getLogger(OrderDAO.class);
    private static final String ORDER_ID = "order_id";
    private static final String PLACEMENT_DATE = "placement_date";
    private static final String STATUS = "status";
    private static final String USER_ID = "user_id";
    private static final String ORDER_COLUMNS = "`order_id`, `user_id`, `placement_date`, `status`";
    private static final String ADD_PRODUCT_INFO = "INSERT INTO `pharmacy`.`products_orders` (`product_id`, `order_id`, `amount`, `price`) VALUES (?, ?, ?, ?);";
    private static final String FIND_BY_STATUS = "SELECT " + ORDER_COLUMNS + " FROM pharmacy.orders WHERE `status`=?;";
    private static final String FIND_BY_USER_AND_YEAR = "SELECT " + ORDER_COLUMNS + " FROM pharmacy.orders WHERE `user_id`=? AND year(`placement_date`)=?;";
    private static final String FIND_PRODUCTS_INFO = "SELECT `product_id`, `amount`, `products_orders`.`price`, `name`, `production_form`, `form_description`, `is_prescription_required`, `is_deleted` FROM pharmacy.products_orders JOIN pharmacy.products USING (`product_id`) WHERE `order_id`=?;";
    private static final String INSERT_ORDER = "INSERT INTO `pharmacy`.`orders` (`user_id`) VALUES (?);";
    private static final String SELECT_BY_ID = "SELECT " + ORDER_COLUMNS + " FROM `pharmacy`.`orders` WHERE `order_id`=?;";
    private static final String UPDATE_STATUS = "UPDATE `pharmacy`.`orders` SET `status`=? WHERE `order_id`=?;";

    public OrderDAO(ProxyConnection connection) {
        super(connection);
    }

    public boolean addProductInfo(long orderId, long productId, BigDecimal price, int amount) throws DAOException {
        boolean isAdded = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT_INFO)) {
            preparedStatement.setLong(1, productId);
            preparedStatement.setLong(2, orderId);
            preparedStatement.setInt(3, amount);
            preparedStatement.setBigDecimal(4, price);
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to add product info, order id " + orderId + ", product id " + productId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to add product info, order id " + orderId + ", product id " + productId, e);
        }
        return isAdded;
    }

    public boolean changeStatus(long id, String newStatus) throws DAOException {
        boolean isChanged = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setLong(2, id);
            if (preparedStatement.executeUpdate() == 1) {
                isChanged = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to change order's status, id: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to change order's status, id: " + id, e);
        }
        return isChanged;
    }

    public long create(long userId) throws DAOException {
        long orderId = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, userId);
            if (preparedStatement.executeUpdate() == 1) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getLong(1);
                }
            } else {
                LOGGER.log(Level.WARN, "Failure to create order, user id " + userId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to create order, user id: " + userId, e);
        }
        return orderId;
    }

    @Override
    public Optional<Order> findById(long id) throws DAOException {
        Order order = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                order = createOrder(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find order by id, id: " + id, e);
        }
        return Optional.ofNullable(order);
    }

    public List<Order> findByStatus(String status) throws DAOException {
        List<Order> orders = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_STATUS)) {
            preparedStatement.setString(1, status);
            ResultSet rs = preparedStatement.executeQuery();
            orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(createOrder(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find orders by status, status: " + status, e);
        }
        return orders;
    }

    public List<Order> findByUserAndYear(long userId, int year) throws DAOException {
        List<Order> orders = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_AND_YEAR)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, year);
            ResultSet rs = preparedStatement.executeQuery();
            orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(createOrder(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find orders by user and year, user id " + userId + ", year: " + year, e);
        }
        return orders;
    }

    public List<ProductDTO> findProductsInfo(long orderId) throws DAOException {
        List<ProductDTO> products = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCTS_INFO)) {
            preparedStatement.setLong(1, orderId);
            ResultSet rs = preparedStatement.executeQuery();
            products = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("product_id");
                String name = rs.getString("name");
                BigDecimal price = rs.getBigDecimal("price");
                int amount = rs.getInt("amount");
                String productionForm = rs.getString("production_form");
                String formDescription = rs.getString("form_description");
                boolean isPrescriptionRequired = rs.getBoolean("is_prescription_required");
                boolean isDeleted = rs.getBoolean("is_deleted");
                products.add(new ProductDTO(id, name, price, amount, productionForm, formDescription, isPrescriptionRequired, isDeleted));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find products info, order id: " + orderId, e);
        }
        return products;
    }

    private Order createOrder(ResultSet rs) throws SQLException {
        long orderId = rs.getLong(ORDER_ID);
        long userId = rs.getLong(USER_ID);
        Date placementDate = rs.getDate(PLACEMENT_DATE);
        OrderStatus status = OrderStatus.valueOf(rs.getString(STATUS).toUpperCase());
        return new Order(orderId, userId, placementDate, status);
    }
}
