package by.bsu.machulski.command.doctor;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.*;
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

public class FindPatientCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(FindPatientCommand.class);
    private static final String PATIENTS_ATTRIBUTE = "users";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.FIND_PATIENT));
        try {
            String searchText = content.getFirstParameterValue(UserAttributeConstant.SEARCH_TEXT);
            List<User> users = new UserLogic().findUsersByEmail(searchText);
            content.putRequestAttribute(PATIENTS_ATTRIBUTE, users);
            content.putRequestAttribute(UserAttributeConstant.SEARCH_TEXT, searchText);
            router.setRoutingType(RoutingType.FORWARD);
            String path = ConfigurationManager.getPath(PageConfigConstant.CONTROLLER);
            path = ConfigurationManager.addParameter(path, COMMAND_PARAMETER, PrescriptionAttributeConstant.FORM_COMMAND);
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
