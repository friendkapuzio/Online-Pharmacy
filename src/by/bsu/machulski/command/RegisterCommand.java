package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.constant.message.InfoMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.UserOperationError;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class RegisterCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String name = content.getFirstParameterValue(UserAttributeConstant.NAME);
            String email = content.getFirstParameterValue(UserAttributeConstant.EMAIL);
            String password = content.getFirstParameterValue(UserAttributeConstant.PASSWORD);
            String confirmPassword = content.getFirstParameterValue(UserAttributeConstant.CONFIRM_PASSWORD);
            EnumSet<UserOperationError> errors = new UserLogic().register(name, email, password, confirmPassword);
            if (errors.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.REGISTRATION_COMPLETED);
                router.setPath(path);
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (UserOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(UserAttributeConstant.NAME, name);
                content.putRequestAttribute(UserAttributeConstant.EMAIL, email);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.REGISTRATION));
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
