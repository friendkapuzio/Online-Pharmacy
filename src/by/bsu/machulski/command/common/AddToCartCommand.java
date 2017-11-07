package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.ProductAttributeConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.message.CartMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.CartLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddToCartCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddToCartCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            CartDTO cart = (CartDTO) content.getSessionAttribute(SessionAttributeConstant.CART);
            long productId = Long.parseLong(content.getFirstParameterValue(ProductAttributeConstant.PRODUCT_ID));
            String addedAmount = content.getFirstParameterValue(ProductAttributeConstant.ADDED_AMOUNT);
            Optional<String> message = new CartLogic().addAndCheckProduct(userId, cart, productId, addedAmount);
            String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
            if (!message.isPresent()) {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, CartMessage.ADD_TO_CART_SUCCEED);
            } else {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, message.get());
            }
            content.putSessionAttribute(SessionAttributeConstant.CART, cart);
            router.setPath(path);
            router.setRoutingType(RoutingType.REDIRECT);
        } catch (NoSuchParameterException e) {
            LOGGER.log(Level.WARN, "Wrong request parameters.", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.WRONG_REQUEST));
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
