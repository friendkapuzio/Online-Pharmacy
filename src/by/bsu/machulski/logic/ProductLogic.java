package by.bsu.machulski.logic;

import by.bsu.machulski.dao.ProductDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.ProductOperationError;
import by.bsu.machulski.validator.ProductValidator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ProductLogic {
    public EnumSet<ProductOperationError> addQuantityUpdate(String id, String name, String price,
                                                            String isAdd, String addQuantity,
                                                            String form, String formDescription,
                                                            String isPrescriptionRequired, String isDeleted) throws LogicException {
        EnumSet<ProductOperationError> errors = checkProductFields(name, price, addQuantity, form, formDescription);
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                long parsedId = Long.parseLong(id);
                ProductDAO productDAO = new ProductDAO(connection);
                BigDecimal parsedPrice = new BigDecimal(price);
                int parsedQuantity = 0;
                if (Boolean.parseBoolean(isAdd)) {
                    parsedQuantity = Integer.parseInt(addQuantity);
                }
                boolean parsedIsPrescriptionRequired = Boolean.parseBoolean(isPrescriptionRequired);
                boolean parsedIsDeleted = Boolean.parseBoolean(isDeleted);
                boolean isUpdated = productDAO.addQuantityUpdateProduct(parsedId, name, parsedPrice, parsedQuantity, form, formDescription, parsedIsPrescriptionRequired, parsedIsDeleted);
                if (!isUpdated) {
                    errors.add(ProductOperationError.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to add quantity and update product, id: " + id, e);
            }
        }
        return errors;
    }

    public EnumSet<ProductOperationError> create(String name, String price, String quantity, String form,
                                                 String formDescription, String isPrescriptionRequired) throws LogicException {
        EnumSet<ProductOperationError> errors = checkProductFields(name, price, quantity, form, formDescription);
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                ProductDAO productDAO = new ProductDAO(connection);
                BigDecimal parsedPrice = new BigDecimal(price);
                int parsedQuantity = Integer.parseInt(quantity);
                boolean parsedIsPrescriptionRequired = Boolean.parseBoolean(isPrescriptionRequired);
                boolean isAdded = productDAO.create(name, parsedPrice, parsedQuantity, form, formDescription, parsedIsPrescriptionRequired);
                if (!isAdded) {
                    errors.add(ProductOperationError.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to create product", e);
            }
        }
        return errors;
    }

    public Optional<Product> findById(long id) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).findById(id);
        } catch (DAOException e) {
            throw new LogicException("Failure to find product by id, id: " + id, e);
        }
    }

    public Optional<Product> findById(long id, boolean isDeleted) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).findById(id, isDeleted);
        } catch (DAOException e) {
            throw new LogicException("Failure to find product by id, id: " + id, e);
        }
    }

    public List<Product> findDeletedProducts(String searchText) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).findDeletedProductsByName(searchText);
        } catch (DAOException e) {
            throw new LogicException("Failure to find deleted products, request text: " + searchText, e);
        }
    }

    public List<Product> findProducts(String searchText) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).findProductsByName(searchText);
        } catch (DAOException e) {
            throw new LogicException("Failure to find products, request text: " + searchText, e);
        }
    }

    public int markDeletedById(String... identifiers) throws LogicException {
        int updated = 0;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            ProductDAO productDAO = new ProductDAO(connection);
            updated = productDAO.updateIsDeletedById(true, Arrays.stream(identifiers).mapToLong(Long::parseLong).toArray());
        } catch (DAOException e) {
            throw new LogicException("Failure to mark products as deleted, " + updated + " of " + identifiers.length + " was updated", e);
        }
        return updated;
    }

    public int restoreDeletedById(String... identifiers) throws LogicException {
        int updated = 0;
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            ProductDAO productDAO = new ProductDAO(connection);
            updated = productDAO.updateIsDeletedById(false, Arrays.stream(identifiers).mapToLong(Long::parseLong).toArray());
        } catch (DAOException e) {
            throw new LogicException("Failure to restore deleted products, " + updated + " of " + identifiers.length + " was updated", e);
        }
        return updated;
    }

    public List<Product> takeCheapest(int amount) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).takeTopCheapest(amount);
        } catch (DAOException e) {
            throw new LogicException("Failure to take cheapest products", e);
        }
    }

    public EnumSet<ProductOperationError> update(String id, String name, String price,
                                                 String quantity, String form, String formDescription,
                                                 String isPrescriptionRequired, String isDeleted) throws LogicException {
        EnumSet<ProductOperationError> errors = checkProductFields(name, price, quantity, form, formDescription);
        if (errors.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                long parsedId = Long.parseLong(id);
                ProductDAO productDAO = new ProductDAO(connection);
                BigDecimal parsedPrice = new BigDecimal(price);
                int parsedQuantity = Integer.parseInt(quantity);
                boolean parsedIsPrescriptionRequired = Boolean.parseBoolean(isPrescriptionRequired);
                boolean parsedIsDeleted = Boolean.parseBoolean(isDeleted);
                boolean isUpdated = productDAO.updateProduct(parsedId, name, parsedPrice, parsedQuantity, form, formDescription, parsedIsPrescriptionRequired, parsedIsDeleted);
                if (!isUpdated) {
                    errors.add(ProductOperationError.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException("Failure to update product, id: " + id, e);
            }
        }
        return errors;
    }

    private EnumSet<ProductOperationError> checkProductFields(String name, String price, String quantity, String form, String formDescription) {
        EnumSet<ProductOperationError> errors = EnumSet.noneOf(ProductOperationError.class);
        ProductValidator productValidator = new ProductValidator();
        if (!productValidator.checkName(name)) {
            errors.add(ProductOperationError.INCORRECT_NAME);
        }
        if (!productValidator.checkPrice(price)) {
            errors.add(ProductOperationError.INCORRECT_PRICE);
        }
        if (!productValidator.checkAmount(quantity)) {
            errors.add(ProductOperationError.INCORRECT_QUANTITY);
        }
        if (!productValidator.checkForm(form)) {
            errors.add(ProductOperationError.INCORRECT_FORM);
        }
        if (!productValidator.checkFormDescription(formDescription)) {
            errors.add(ProductOperationError.INCORRECT_FORM_DESCRIPTION);
        }
        return errors;
    }
}
