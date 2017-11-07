package by.bsu.machulski.command.admin;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.constant.message.AdminActionMessage;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnblockUserCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(UnblockUserCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String userId = content.getFirstParameterValue(UserAttributeConstant.USER_ID);
            UserLogic userLogic = new UserLogic();
            boolean isDeleted = userLogic.unblock(userId);
            String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
            if (isDeleted) {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AdminActionMessage.USER_UNBLOCK_SUCCEED);
            } else {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AdminActionMessage.USER_UNBLOCK_FAILED);
            }
            router.setRoutingType(RoutingType.REDIRECT);
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
