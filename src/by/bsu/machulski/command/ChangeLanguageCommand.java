package by.bsu.machulski.command;

import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangeLanguageCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ChangeLanguageCommand.class);
    private static final String LANGUAGE_PARAMETER = "language";

    @Override
    public Router execute(SessionRequestContent content) {
        String path = (String) content.getSessionAttribute(SessionAttributeConstant.PATH);
        Router router = new Router(RoutingType.FORWARD, path);
        try {
            String language = content.getFirstParameterValue(LANGUAGE_PARAMETER);
            content.putSessionAttribute(SessionAttributeConstant.LOCALE, language);
        } catch (NoSuchParameterException e) {
            LOGGER.log(Level.WARN, "Wrong request parameters.", e);
        }
        return router;
    }
}
