package by.bsu.machulski.command.doctor;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.PrescriptionAttributeConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.message.InfoMessage;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.PrescriptionLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.PrescriptionOperationError;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class ChangeExpirationDateCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ChangeExpirationDateCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String prescriptionId = content.getFirstParameterValue(PrescriptionAttributeConstant.PRESCRIPTION_ID);
            String newExpirationDate = content.getFirstParameterValue(PrescriptionAttributeConstant.NEW_EXPIRATION_DATE);
            EnumSet<PrescriptionOperationError> errors = new PrescriptionLogic().changeExpirationDate(prescriptionId, newExpirationDate);
            String path;
            String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
            if (errors.isEmpty()) {
                path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.EXPIRATION_DATE_UPDATED);
                router.setRoutingType(RoutingType.REDIRECT);
            } else {
                for (PrescriptionOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(PrescriptionAttributeConstant.PRESCRIPTION_ID, prescriptionId);
                content.putRequestAttribute(PrescriptionAttributeConstant.NEW_EXPIRATION_DATE, newExpirationDate);
                router.setRoutingType(RoutingType.FORWARD);
                path = ConfigurationManager.getPath(PageConfigConstant.CONTROLLER);
                path = ConfigurationManager.addParameter(path, COMMAND_PARAMETER, PrescriptionAttributeConstant.SHOW_GIVEN_PRESCRIPTIONS_СOMMAND);
                router.setPath(path);
            }
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
