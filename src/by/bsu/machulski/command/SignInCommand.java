package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class SignInCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SignInCommand.class);
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private final String SIGN_IN_ERROR_ATTRIBUTE = "signInErrorMessage";
    private final String SIGN_IN_ERROR_PATH = "message.sign_in.error";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String email = content.getFirstParameterValue(EMAIL);
            String password = content.getFirstParameterValue(PASSWORD);
            Optional<User> user = new UserLogic().identify(email, password);
            if (user.isPresent()) {
                User identifiedUser = user.get();
                if (!identifiedUser.isBlocked()) {
                    router.setRoutingType(RoutingType.REDIRECT);
                    router.setPath(ConfigurationManager.getProperty(PageConfigConstant.INDEX));
                    content.putSessionAttribute(SessionAttributeConstant.USERNAME, identifiedUser.getName());
                    content.putSessionAttribute(SessionAttributeConstant.USER_ID, identifiedUser.getId());
                    content.putSessionAttribute(SessionAttributeConstant.ROLE, identifiedUser.getRole());
                } else {
                    router.setRoutingType(RoutingType.REDIRECT);
                    router.setPath(ConfigurationManager.getProperty(PageConfigConstant.ACCOUNT_BLOCKED));
                }
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                String message = MessageManager.getProperty(SIGN_IN_ERROR_PATH, locale);
                content.putRequestAttribute(SIGN_IN_ERROR_ATTRIBUTE, message);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getProperty(PageConfigConstant.INDEX));
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
