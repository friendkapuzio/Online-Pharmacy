package by.bsu.machulski.logic;

import by.bsu.machulski.constant.message.CartMessage;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.entity.Prescription;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.type.ProductOperationError;
import by.bsu.machulski.validator.ProductValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

public class CartLogic {
    private static final Logger LOGGER = LogManager.getLogger(CartLogic.class);

    public Optional<String> addAndCheckProduct(long userId, CartDTO cart, long productId, String addedAmount)
            throws LogicException {
        String message = null;
        if (!new ProductValidator().checkAmount(addedAmount)) {
            message = ProductOperationError.INCORRECT_QUANTITY.getMessagePath();
        } else {
            Optional<Product> productOptional = new ProductLogic().findById(productId);
            BigDecimal cartTotal = cart.getTotal();
            if (productOptional.isPresent() && !productOptional.get().getIsDeleted()) {
                Product product = productOptional.get();
                int currentAmount = cart.getProducts().getOrDefault(productId, 0);
                int newAmount = currentAmount + Integer.parseInt(addedAmount);
                if (newAmount > product.getAvailableQuantity()) {
                    newAmount = product.getAvailableQuantity();
                    message = CartMessage.NOT_ENOUGH_PRODUCT;
                }
                cart.getProducts().put(productId, newAmount);
                if (product.getIsPrescriptionRequired()) {
                    List<Prescription> prescriptions = new PrescriptionLogic().findActualByUserAndProduct(userId, product.getId());
                    if (!prescriptions.isEmpty()) {
                        int prescriptionAmount = prescriptions.stream().mapToInt(Prescription::getAmount).sum();
                        if (prescriptionAmount < newAmount) {
                            newAmount = prescriptionAmount;
                            cart.getProducts().put(productId, newAmount);
                            message = CartMessage.NOT_ENOUGH_PRESCRIPTION;
                        }
                    } else {
                        newAmount = 0;
                        cart.getProducts().remove(productId);
                        message = CartMessage.NO_PRESCRIPTION;
                    }
                }
                cartTotal = cartTotal.add(productOptional.get().getPrice().multiply(new BigDecimal(newAmount - currentAmount)));
                cart.setTotal(cartTotal);
            } else {
                if (!productOptional.isPresent()) {
                    LOGGER.log(Level.WARN, "Nonexistent product in cart, product id " + productId);
                    message = CartMessage.ADD_TO_CART_FAILED;
                } else {
                    message = CartMessage.PRODUCT_UNAVAILABLE;
                }
            }
        }
        return Optional.ofNullable(message);
    }

    public Map<Product, String> checkAndGetProductsInfo(long userId, CartDTO cart)
            throws LogicException {
        Map<Long, Integer> products = cart.getProducts();
        BigDecimal cartTotal = BigDecimal.ZERO;
        ArrayList<Long> unavailableProducts = new ArrayList<>();
        HashMap<Product, String> productsInfo = new HashMap<>();
        ProductLogic productLogic = new ProductLogic();
        PrescriptionLogic prescriptionLogic = new PrescriptionLogic();
        for (Map.Entry<Long, Integer> entry:products.entrySet()) {
            Optional<Product> productOptional = productLogic.findById(entry.getKey());
            if (productOptional.isPresent() && !productOptional.get().getIsDeleted()) {
                Product product = productOptional.get();
                if (product.getAvailableQuantity() < entry.getValue()) {
                    entry.setValue(product.getAvailableQuantity());
                    productsInfo.put(product, CartMessage.NOT_ENOUGH_PRODUCT);
                } else {
                    productsInfo.put(product, null);
                }
                if (product.getIsPrescriptionRequired()) {
                    List<Prescription> prescriptions = prescriptionLogic.findActualByUserAndProduct(userId, product.getId());
                    if (!prescriptions.isEmpty()) {
                        int prescriptionAmount = prescriptions.stream().mapToInt(Prescription::getAmount).sum();
                        if (prescriptionAmount < entry.getValue()) {
                            entry.setValue(prescriptionAmount);
                            productsInfo.put(product, CartMessage.NOT_ENOUGH_PRESCRIPTION);
                        }
                    } else {
                        entry.setValue(0);
                        unavailableProducts.add(entry.getKey());
                        productsInfo.put(product, CartMessage.NO_PRESCRIPTION);
                    }
                }
                cartTotal = cartTotal.add(product.getPrice().multiply(new BigDecimal(entry.getValue())));
            } else {
                unavailableProducts.add(entry.getKey());
                if (!productOptional.isPresent()) {
                    LOGGER.log(Level.WARN, "Nonexistent product in cart, product id " + entry.getKey());
                } else {
                    productsInfo.put(productOptional.get(), CartMessage.PRODUCT_UNAVAILABLE);
                }
            }
        }
        for (Long productId:unavailableProducts) {
            products.remove(productId);
        }
        cart.setProducts(products);
        cart.setTotal(cartTotal);
        return productsInfo;
    }

    public boolean checkProducts(long userId, CartDTO cart) throws LogicException {
//        boolean isRight = false;
        Collection<String> messages = checkAndGetProductsInfo(userId, cart).values();
        return messages.size() == 1 && messages.contains(null);
//        return isRight;
    }

    public void removeProduct(CartDTO cart, String productId) throws LogicException {
        Optional<Product> productOptional = new ProductLogic().findById(Long.parseLong(productId));
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            int productAmount = cart.getProducts().getOrDefault(product.getId(), 0);
            BigDecimal newTotal = cart.getTotal().add(product.getPrice().multiply(new BigDecimal(productAmount)).negate());
            cart.setTotal(newTotal);
        }
        cart.getProducts().remove(Long.parseLong(productId));
    }

    public Optional<String> updateProduct(CartDTO cart, String productId, String newAmount) throws LogicException {
        String message = null;
        if (!new ProductValidator().checkAmount(newAmount)) {
            message = ProductOperationError.INCORRECT_QUANTITY.getMessagePath();
        } else {
            int parsedNewAmount = Integer.parseInt(newAmount);
            Optional<Product> productOptional = new ProductLogic().findById(Long.parseLong(productId));
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                int currentAmount = cart.getProducts().get(product.getId());
                BigDecimal newTotal = cart.getTotal().add(product.getPrice().multiply(new BigDecimal(parsedNewAmount - currentAmount)));
                cart.setTotal(newTotal);
            }
            cart.getProducts().replace(Long.parseLong(productId), parsedNewAmount);
        }
        return Optional.ofNullable(message);
    }
}
