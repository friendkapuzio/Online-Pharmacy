package by.bsu.machulski.command.admin;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.OrderAttributeConstant;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.OrderDTO;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.OrderLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FindOrdersCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(FindOrdersCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String ordersStatus = content.getFirstParameterValue(OrderAttributeConstant.ORDERS_STATUS);
            List<OrderDTO> orders = new OrderLogic().findOrders(ordersStatus);
            content.putRequestAttribute(OrderAttributeConstant.ORDERS, orders);
            router.setRoutingType(RoutingType.FORWARD);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.MANAGE_ORDERS));
        } catch (NoSuchParameterException | IllegalArgumentException e) {
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
