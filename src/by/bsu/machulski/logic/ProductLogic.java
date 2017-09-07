package by.bsu.machulski.logic;

import by.bsu.machulski.dao.ProductDAO;
import by.bsu.machulski.database.ConnectionPool;
import by.bsu.machulski.database.ProxyConnection;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.DAOException;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.ProductOperationStatus;
import by.bsu.machulski.util.ProductValidator;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

public class ProductLogic {
    public EnumSet<ProductOperationStatus> addProduct(String name, String price, String quantity, String form,
                                                      String formDescription, String isRecipeRequired) throws LogicException {
        EnumSet<ProductOperationStatus> status = EnumSet.noneOf(ProductOperationStatus.class);
        ProductValidator validator = new ProductValidator();
        if (!validator.checkName(name)) {
            status.add(ProductOperationStatus.INCORRECT_NAME);
        }
        if (!validator.checkPrice(price)) {
            status.add(ProductOperationStatus.INCORRECT_PRICE);
        }
        if (!validator.checkQuantity(quantity)) {
            status.add(ProductOperationStatus.INCORRECT_QUANTITY);
        }
        if (!validator.checkForm(form)) {
            status.add(ProductOperationStatus.INCORRECT_FORM);
        }
        if (!validator.checkFormDescription(formDescription)) {
            status.add(ProductOperationStatus.INCORRECT_FORM_DESCRIPTION);
        }
        if (status.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                ProductDAO productDAO = new ProductDAO(connection);
                BigDecimal parsedPrice = new BigDecimal(price);
                int parsedQuantity = Integer.parseInt(quantity);
                boolean parsedIsRecipeRequired = Boolean.parseBoolean(isRecipeRequired);
                boolean isAdded = productDAO.add(name, parsedPrice, parsedQuantity, form, formDescription, parsedIsRecipeRequired);
                if (!isAdded) {
                    status.add(ProductOperationStatus.ERROR);
                }
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return status;
    }

    public void deleteById(String id) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            new ProductDAO(connection).deleteById(Long.parseLong(id));
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    public List<Product> findProducts(String searchText) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).findProductsByName(searchText);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    public List<Product> takeCheapest(int amount) throws LogicException {
        try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
            return new ProductDAO(connection).takeTopCheapest(amount);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    public EnumSet<ProductOperationStatus> updateProduct(String id, String name,
                                                         String price, String quantityModifier,
                                                         String modificationQuantity, String form,
                                                         String formDescription, String isRecipeRequired) throws LogicException {
        EnumSet<ProductOperationStatus> status = EnumSet.noneOf(ProductOperationStatus.class);
        ProductValidator validator = new ProductValidator();
        if (!validator.checkName(name)) {
            status.add(ProductOperationStatus.INCORRECT_NAME);
        }
        if (!validator.checkPrice(price)) {
            status.add(ProductOperationStatus.INCORRECT_PRICE);
        }
        if (!quantityModifier.isEmpty() && !validator.checkQuantity(modificationQuantity)) {
            status.add(ProductOperationStatus.INCORRECT_QUANTITY);
        }
        if (!validator.checkForm(form)) {
            status.add(ProductOperationStatus.INCORRECT_FORM);
        }
        if (!validator.checkFormDescription(formDescription)) {
            status.add(ProductOperationStatus.INCORRECT_FORM_DESCRIPTION);
        }
        if (status.isEmpty()) {
            try (ProxyConnection connection = ConnectionPool.getInstance().takeConnection()) {
                long parsedId = Long.parseLong(id);
                ProductDAO productDAO = new ProductDAO(connection);
                if (!productDAO.findById(parsedId).isPresent()) {
                    status.add(ProductOperationStatus.NONEXISTENT_PRODUCT);
                } else {
                    BigDecimal parsedPrice = new BigDecimal(price);
                    String addQuantity = quantityModifier;
                    if (!quantityModifier.isEmpty()) {
                        addQuantity += modificationQuantity;
                    }
                    boolean parsedIsRecipeRequired = Boolean.parseBoolean(isRecipeRequired);
                    productDAO.updateProduct(parsedId, name, parsedPrice, addQuantity, form, formDescription, parsedIsRecipeRequired);
                }
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return status;
    }
}
