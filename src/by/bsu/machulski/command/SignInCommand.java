package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.constant.message.InfoMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.CartDTO;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class SignInCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SignInCommand.class);
    private final String SIGN_IN_ERROR_ATTRIBUTE = "signInErrorMessage";
    private final String SIGN_IN_ERROR_PATH = "sign_in.error";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String email = content.getFirstParameterValue(UserAttributeConstant.EMAIL);
            String password = content.getFirstParameterValue(UserAttributeConstant.PASSWORD);
            Optional<User> optionalUser = new UserLogic().identify(email, password);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.getIsBlocked()) {
                    router.setRoutingType(RoutingType.REDIRECT);
                    router.setPath(ConfigurationManager.getPath(PageConfigConstant.INDEX));
                    content.putSessionAttribute(SessionAttributeConstant.USERNAME, user.getName());
                    content.putSessionAttribute(SessionAttributeConstant.USER_ID, user.getId());
                    content.putSessionAttribute(SessionAttributeConstant.ROLE, user.getRole());
                    content.putSessionAttribute(SessionAttributeConstant.REGISTRATION_DATE, user.getRegistrationDate());
                    content.putSessionAttribute(SessionAttributeConstant.CART, new CartDTO());
                } else {
                    router.setRoutingType(RoutingType.REDIRECT);
                    String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                    path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.ACCOUNT_BLOCKED);
                    router.setPath(path);
                }
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                String message = MessageManager.getProperty(SIGN_IN_ERROR_PATH, locale);
                content.putRequestAttribute(SIGN_IN_ERROR_ATTRIBUTE, message);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.INDEX));
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
