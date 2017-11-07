package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.message.CartMessage;
import by.bsu.machulski.constant.message.InfoMessage;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.logic.OrderLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.OrderOperationStatus;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;

public class CheckoutCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(CheckoutCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            CartDTO cart = (CartDTO) content.getSessionAttribute(SessionAttributeConstant.CART);
            OrderOperationStatus status = new OrderLogic().createOrder(userId, cart);
            String path;
            switch (status) {
                case ACCEPTED:
                    cart.setTotal(BigDecimal.ZERO);
                    cart.setProducts(new HashMap<>());
                    content.putSessionAttribute(SessionAttributeConstant.CART, cart);
                    path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                    path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.ORDER_ACCEPTED);
                    break;
                case INSUFFICIENT_FUNDS:
                    path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                    path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, CartMessage.INSUFFICIENT_FUNDS);
                    break;
                case ERROR:
                default:
                    path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                    path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.UNKNOWN_ERROR);
            }
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(path);
        }  catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
