package by.bsu.machulski.command.pharmacist;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.message.PharmacistActionMessage;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.ProductOperationError;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

import static by.bsu.machulski.constant.ProductAttributeConstant.*;

public class EditDeletedProductCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditDeletedProductCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String id = content.getFirstParameterValue(PRODUCT_ID);
            String name = content.getFirstParameterValue(PRODUCT_NAME);
            String price = content.getFirstParameterValue(PRODUCT_PRICE);
            String newAmount = content.getFirstParameterValue(NEW_QUANTITY);
            String form = content.getFirstParameterValue(PRODUCT_FORM);
            String formDescription = content.getFirstParameterValue(FORM_DESCRIPTION);
            String isPrescriptionRequired = content.getFirstParameterValue(PRODUCT_PRESCRIPTION_REQUIRED);
            String isDeleted = content.getFirstParameterValue(IS_DELETED);
            EnumSet<ProductOperationError> errors =
                    new ProductLogic().update(id, name, price, newAmount, form, formDescription, isPrescriptionRequired, isDeleted);
            if (errors.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, PharmacistActionMessage.PRODUCT_EDITED);
                router.setPath(path);
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (ProductOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(PRODUCT_NAME, name);
                content.putRequestAttribute(PRODUCT_PRICE, price);
                content.putRequestAttribute(NEW_QUANTITY, newAmount);
                content.putRequestAttribute(PRODUCT_FORM, form);
                content.putRequestAttribute(FORM_DESCRIPTION, formDescription);
                content.putRequestAttribute(PRODUCT_PRESCRIPTION_REQUIRED, isPrescriptionRequired);
                content.putRequestAttribute(IS_DELETED, isDeleted);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.EDIT_PRODUCT));
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
