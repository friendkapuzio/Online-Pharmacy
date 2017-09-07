package by.bsu.machulski.dao;

import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Entity;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.DAOException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO extends AbstractDAO<Product> {
    private static final int ID_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int PRICE_INDEX = 3;
    private static final int QUANTITY_INDEX = 4;
    private static final int PRODUCTION_FORM_INDEX = 5;
    private static final int DESCRIPTION_INDEX = 6;
    private static final int IS_RECIPE_INDEX = 7;
    private static final int IS_DELETED_INDEX = 8;
    private static final String PRODUCT_COLOMNS =
            "`product_id`, `name`, `price`, `available_quantity`, `production_form`, `form_description`, `is_recipe_required`, `is_deleted`";
    private static final String INSERT_PRODUCT =
            "INSERT INTO `pharmacy`.`products` (`name`, `price`, `available_quantity`, `production_form`, `form_description`, `is_recipe_required`) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_CHEAPEST =
            "SELECT " + PRODUCT_COLOMNS + " FROM `pharmacy`.`products` WHERE `is_deleted`='0' ORDER BY `price` ASC LIMIT ?;";
    private static final String SELECT_BY_ID = "SELECT " + PRODUCT_COLOMNS + " FROM `pharmacy`.`products` WHERE `product_id`=?;";
    private static final String SELECT_BY_NAME =
            "SELECT " + PRODUCT_COLOMNS + " FROM `pharmacy`.`products` WHERE `is_deleted`='0' AND `name` LIKE ?;";
    private static final String UPDATE_PRODUCT = "UPDATE `pharmacy`.`products` " +
            "SET `name`=?, `price`=?, `available_quantity`=GREATEST(`available_quantity`+?, '0'), `production_form`=?, `form_description`=?, `is_recipe_required`=? WHERE `product_id`=?;";

    public ProductDAO(ProxyConnection connection) {
        super(connection);
    }


    public void deleteById(long id) throws DAOException {
        
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
            throw new DAOException(e);
        }
        return Optional.ofNullable(product);
    }

    public boolean add(String name, BigDecimal price, int quantity, String form, String formDescription, boolean isRecipeRequired)
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
            preparedStatement.setBoolean(6, isRecipeRequired);
            if (preparedStatement.executeUpdate() == 1) {
                isAdded = true;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return isAdded;
    }

    public List<Product> findProductsByName(String searchText) throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {
            preparedStatement.setString(1, "%" + searchText + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(createProduct(rs));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return products;
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
            throw new DAOException(e);
        }
        return products;
    }

    public void updateProduct(long id, String name,
                              BigDecimal price, String addQuantity,
                              String form, String formDescription, boolean isRecipeRequired) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setBigDecimal(2, price);
            preparedStatement.setString(3, addQuantity);
            preparedStatement.setString(4, form);
            preparedStatement.setString(5, formDescription);
            preparedStatement.setBoolean(6, isRecipeRequired);
            preparedStatement.setLong(7, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Product createProduct(ResultSet rs) throws SQLException {
        long id = rs.getLong(ID_INDEX);
        String name = rs.getString(NAME_INDEX);
        BigDecimal price = rs.getBigDecimal(PRICE_INDEX);
        int availableQuantity = rs.getInt(QUANTITY_INDEX);
        String productionForm = rs.getString(PRODUCTION_FORM_INDEX);
        String formDescription = rs.getString(DESCRIPTION_INDEX);
        boolean isRecipeRequired = rs.getBoolean(IS_RECIPE_INDEX);
        boolean isDeleted = rs.getBoolean(IS_DELETED_INDEX);
        return new Product(id, name, price, availableQuantity, productionForm, formDescription, isRecipeRequired, isDeleted);
    }
}
