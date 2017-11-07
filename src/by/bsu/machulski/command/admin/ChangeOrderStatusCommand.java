package by.bsu.machulski.command.admin;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.OrderAttributeConstant;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.message.AdminActionMessage;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.OrderLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeOrderStatusCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ChangeOrderStatusCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String orderId = content.getFirstParameterValue(OrderAttributeConstant.ORDER_ID);
            String newStatus = content.getFirstParameterValue(OrderAttributeConstant.NEW_STATUS);
            OrderLogic orderLogic = new OrderLogic();
            boolean isChanged = orderLogic.changeStatus(orderId, newStatus);
            String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
            if (isChanged) {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AdminActionMessage.ORDER_STATUS_CHANGE_SUCCEED);
            } else {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AdminActionMessage.ORDER_STATUS_CHANGE_FAILED);
            }
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(path);
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
