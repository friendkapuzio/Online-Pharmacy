package by.bsu.machulski.command.admin;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.entity.User;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SearchBlockedUserCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SearchBlockedUserCommand.class);
    private static final String USERS_ATTRIBUTE = "users";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.USERS));
        try {
            String searchText = content.getFirstParameterValue(UserAttributeConstant.SEARCH_TEXT);
            List<User> users = new UserLogic().findBlockedUsersByEmail(searchText);
            content.putRequestAttribute(USERS_ATTRIBUTE, users);
            content.putRequestAttribute(UserAttributeConstant.SEARCH_TEXT, searchText);
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
