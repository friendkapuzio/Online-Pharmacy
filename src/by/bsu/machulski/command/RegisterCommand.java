package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RegisterStatus;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class RegisterCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private final String CONFIRM_PASSWORD = "confirmPassword";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String name = content.getFirstParameterValue(NAME);
            String email = content.getFirstParameterValue(EMAIL);
            String password = content.getFirstParameterValue(PASSWORD);
            String confirmPassword = content.getFirstParameterValue(CONFIRM_PASSWORD);
            EnumSet<RegisterStatus> registerStatus = new UserLogic().registerUser(name, email, password, confirmPassword);
            if (registerStatus.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                router.setPath(ConfigurationManager.getProperty(PageConfigConstant.REGISTRATION_COMPLETED));
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (RegisterStatus it:registerStatus) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(NAME, name);
                content.putRequestAttribute(EMAIL, email);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getProperty(PageConfigConstant.REGISTRATION));
            }
        } catch (NoSuchParameterException e) {
            LOGGER.log(Level.WARN, "Wrong request parameters.");
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getProperty(PageConfigConstant.WRONG_REQUEST));
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getProperty(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
