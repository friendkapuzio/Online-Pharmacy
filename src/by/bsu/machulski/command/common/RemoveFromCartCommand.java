package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.CartAttributeConstant;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.ProductAttributeConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
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

public class RemoveFromCartCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RemoveFromCartCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            CartDTO cart = (CartDTO) content.getSessionAttribute(SessionAttributeConstant.CART);
            String productId = content.getFirstParameterValue(ProductAttributeConstant.PRODUCT_ID);
            new CartLogic().removeProduct(cart, productId);
            router.setRoutingType(RoutingType.REDIRECT);
            String path = ConfigurationManager.getPath(PageConfigConstant.CONTROLLER);
            path = ConfigurationManager.addParameter(path, COMMAND_PARAMETER, CartAttributeConstant.CART_COMMAND);
            router.setPath(path);
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
