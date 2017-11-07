package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.constant.message.AccountActionMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.type.UserOperationError;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class ChangeEmailCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ChangeEmailCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String newEmail = content.getFirstParameterValue(UserAttributeConstant.NEW_EMAIL);
            UserLogic userLogic = new UserLogic();
            EnumSet<UserOperationError> errors = userLogic.changeEmail(userId, newEmail);
            if (errors.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AccountActionMessage.EMAIL_CHANGED);
                router.setPath(path);
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (UserOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(USER, userLogic.findById(userId).get());
                content.putRequestAttribute(UserAttributeConstant.NEW_EMAIL, newEmail);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.ACCOUNT));
            }
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
