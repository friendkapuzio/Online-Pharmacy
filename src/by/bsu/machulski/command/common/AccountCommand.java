package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AccountCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(AccountCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.ACCOUNT));
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            Optional<User> optionalUser = new UserLogic().findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(null);
                content.putRequestAttribute(USER, user);
                content.putSessionAttribute(SessionAttributeConstant.ROLE, user.getRole());
            } else {
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.REGISTRATION));
            }
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
