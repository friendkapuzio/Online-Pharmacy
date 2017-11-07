package by.bsu.machulski.command.doctor;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.*;
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

public class CreatePrescriptionCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(CreatePrescriptionCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.FIND_PATIENT));
        try {
            long doctorId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String patientId = content.getFirstParameterValue(PrescriptionAttributeConstant.PATIENT_ID);
            String productId = content.getFirstParameterValue(ProductAttributeConstant.PRODUCT_ID);
            String productAmount = content.getFirstParameterValue(PrescriptionAttributeConstant.PRODUCT_AMOUNT);
            String endDate = content.getFirstParameterValue(PrescriptionAttributeConstant.EXPIRATION_DATE);
            EnumSet<PrescriptionOperationError> errors =
                    new PrescriptionLogic().create(doctorId, productId, productAmount, endDate, patientId);
            String path;
            String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
            if (errors.isEmpty()) {
                path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.PRESCRIPTION_CREATE_SUCCEED);
                router.setRoutingType(RoutingType.REDIRECT);
            } else {
                for (PrescriptionOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(PrescriptionAttributeConstant.PRODUCT_AMOUNT, productAmount);
                content.putRequestAttribute(PrescriptionAttributeConstant.EXPIRATION_DATE, endDate);
                router.setRoutingType(RoutingType.FORWARD);
                path = ConfigurationManager.getPath(PageConfigConstant.CONTROLLER);
                path = ConfigurationManager.addParameter(path, COMMAND_PARAMETER, PrescriptionAttributeConstant.FORM_COMMAND);
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
