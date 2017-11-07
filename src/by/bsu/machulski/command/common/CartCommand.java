package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.CartAttributeConstant;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.logic.CartLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CartCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(CartCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.CART));
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            CartDTO cart = (CartDTO) content.getSessionAttribute(SessionAttributeConstant.CART);
            Map<Product, String> cartProducts = new CartLogic().checkAndGetProductsInfo(userId, cart);
            String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
            for (Map.Entry<Product, String> entry:cartProducts.entrySet()) {
                if (entry.getValue() != null) {
                    entry.setValue(MessageManager.getProperty(entry.getValue(), locale));
                }
            }
            content.putSessionAttribute(SessionAttributeConstant.CART, cart);
            content.putRequestAttribute(CartAttributeConstant.CART_PRODUCTS_ATTRIBUTE, cartProducts);
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
