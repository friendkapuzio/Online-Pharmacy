package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO extends AbstractDAO<Product> {
    private static final Logger LOGGER = LogManager.getLogger(ProductDAO.class);
    private static final String ID = "product_id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String QUANTITY = "available_quantity";
    private static final String PRODUCTION_FORM = "production_form";
    private static final String FORM_DESCRIPTION = "form_description";
    private static final String IS_PRESCRIPTION_REQUIRED = "is_prescription_required";
    private static final String IS_DELETED = "is_deleted";
    private static final String PRODUCT_COLUMNS =
            "`product_id`, `name`, `price`, `available_quantity`, `production_form`, `form_description`, `is_prescription_required`, `is_deleted`";
    private static final String INSERT_PRODUCT =
            "INSERT INTO `pharmacy`.`products` (`name`, `price`, `available_quantity`, `production_form`, `form_description`, `is_prescription_required`) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_CHEAPEST =
            "SELECT " + PRODUCT_COLUMNS + " FROM `pharmacy`.`products` WHERE `is_deleted`='0' ORDER BY `price` ASC LIMIT ?;";
    private static final String SELECT_BY_ID = "SELECT " + PRODUCT_COLUMNS + " FROM `pharmacy`.`products` WHERE `product_id`=?;";
    private static final String SELECT_BY_ID_IS_DELETED = "SELECT " + PRODUCT_COLUMNS + " FROM `pharmacy`.`products` WHERE `product_id`=? AND `is_deleted`=?;";
    private static final String FIND_BY_NAME =
            "SELECT " + PRODUCT_COLUMNS + " FROM `pharmacy`.`products` WHERE `is_deleted`=? AND `name` LIKE ?;";
    private static final String ADD_QUANTITY_UPDATE_PRODUCT = "UPDATE `pharmacy`.`products` " +
            "SET `name`=?, `price`=?, `available_quantity`=`available_quantity`+?, `production_form`=?, `form_description`=?, `is_prescription_required`=?, `is_deleted`=? WHERE `product_id`=?;";
    private static final String UPDATE_IS_DELETED_BY_ID = "UPDATE `pharmacy`.`products` SET `is_deleted`=? WHERE `product_id`=?;";
    private static final String UPDATE_PRODUCT = "UPDATE `pharmacy`.`products` " +
            "SET `name`=?, `price`=?, `available_quantity`=?, `production_form`=?, `form_description`=?, `is_prescription_required`=?, `is_deleted`=? WHERE `product_id`=?;";
    private static final String UPDATE_QUANTITY = "UPDATE `pharmacy`.`products` SET `available_quantity`=`available_quantity` + ? WHERE `product_id`=?;";

    public ProductDAO(ProxyConnection connection) {
        super(connection);
    }

    public boolean addQuantityUpdateProduct(long id, String name, BigDecimal price, int addQuantity,
                                 String form, String formDescription, boolean isPrescriptionRequired, boolean isDeleted) throws DAOException {
        boolean isUpdated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_QUANTITY_UPDATE_PRODUCT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setInt(3, addQuantity);
            preparedStatement.setString(4, form);
            preparedStatement.setString(5, formDescription);
            preparedStatement.setBoolean(6, isPrescriptionRequired);
            preparedStatement.setBoolean(7, isDeleted);
            preparedStatement.setLong(8, id);
            if (preparedStatement.executeUpdate() == 1) {
                isUpdated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to add quantity and update product, id " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to add quantity and update product, id " + id, e);
        }
        return isUpdated;
    }

    public boolean create(String name, BigDecimal price, int quantity, String form, String formDescription, boolean isPrescriptionRequired)
            throws DAOException {
        boolean isAdded = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, form);
            if (formDescription.isEmpty()) {
                preparedStatement.setNull(5, Types.VARCHAR);
            } else {
                preparedStatement.setString(5, formDescription);
            }
            preparedStatement.setBoolean(6, isPrescriptionRequired);
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to create product");
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to create product", e);
        }
        return isAdded;
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public Optional<Product> findById(long id) throws DAOException {
        Product product = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                product = createProduct(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find product by id, id " + id, e);
        }
        return Optional.ofNullable(product);
    }

    public Optional<Product> findById(long id, boolean isDeleted) throws DAOException {
        Product product = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_IS_DELETED)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setBoolean(2, isDeleted);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                product = createProduct(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find product by id, id " + id, e);
        }
        return Optional.ofNullable(product);
    }

    public List<Product> findDeletedProductsByName(String searchText) throws DAOException {
        return findProductsByName(searchText, true);
    }

    public List<Product> findProductsByName(String searchText) throws DAOException {
        return findProductsByName(searchText, false);
    }

    public boolean modifyQuantity(long productId, int amount) throws DAOException {
        boolean isModified = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUANTITY)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setLong(2, productId);
            if (preparedStatement.executeUpdate() == 1) {
                isModified = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to modify product quantity, product id " + productId);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to modify product quantity, product id " + productId, e);
        }
        return isModified;
    }

    public List<Product> takeTopCheapest(int amount) throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CHEAPEST)) {
            preparedStatement.setInt(1, amount);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(createProduct(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to take top cheapest products", e);
        }
        return products;
    }

    public int updateIsDeletedById(boolean isDeleted, long... identifiers) throws DAOException {
        int updated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_IS_DELETED_BY_ID)){
            for (long id:identifiers) {
                preparedStatement.setBoolean(1, isDeleted);
                preparedStatement.setLong(2, id);
                if (preparedStatement.executeUpdate() == 1) {
                    ++updated;
                } else {
                    LOGGER.log(Level.WARN, "Failure to update isDeleted status of product, id " + id);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update isDeleted status of products", e);
        }
        return updated;
    }

    public boolean updateProduct(long id, String name, BigDecimal price, int quantity, String form,
                                 String formDescription, boolean isPrescriptionRequired, boolean isDeleted) throws DAOException {
        boolean isUpdated = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, form);
            preparedStatement.setString(5, formDescription);
            preparedStatement.setBoolean(6, isPrescriptionRequired);
            preparedStatement.setBoolean(7, isDeleted);
            preparedStatement.setLong(8, id);
            if (preparedStatement.executeUpdate() == 1) {
                isUpdated = true;
            } else {
                LOGGER.log(Level.WARN, "Failure to update product, id " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to update product, id " + id, e);
        }
        return isUpdated;
    }

    private Product createProduct(ResultSet rs) throws SQLException {
        long id = rs.getLong(ID);
        String name = rs.getString(NAME);
        BigDecimal price = rs.getBigDecimal(PRICE);
        int availableQuantity = rs.getInt(QUANTITY);
        String productionForm = rs.getString(PRODUCTION_FORM);
        String formDescription = rs.getString(FORM_DESCRIPTION);
        boolean isPrescriptionRequired = rs.getBoolean(IS_PRESCRIPTION_REQUIRED);
        boolean isDeleted = rs.getBoolean(IS_DELETED);
        return new Product(id, name, price, availableQuantity, productionForm, formDescription, isPrescriptionRequired, isDeleted);
    }

    private List<Product> findProductsByName(String searchText, boolean isDeleted) throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setBoolean(1, isDeleted);
            preparedStatement.setString(2, "%" + searchText + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(createProduct(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Failure to find products by name, name: " + searchText, e);
        }
        return products;
    }
}
