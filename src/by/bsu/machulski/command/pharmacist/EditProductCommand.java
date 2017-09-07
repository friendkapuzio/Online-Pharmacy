package by.bsu.machulski.command.pharmacist;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.ProductOperationStatus;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

import static by.bsu.machulski.command.pharmacist.ProductAttributeConstant.*;

public class EditProductCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditProductCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String id = content.getFirstParameterValue(PRODUCT_ID);
            String name = content.getFirstParameterValue(PRODUCT_NAME);
            String price = content.getFirstParameterValue(PRODUCT_PRICE);
            String quantityModification = content.getFirstParameterValue(QUANTITY_MODIFICATION);
            String modificationAmount = content.getFirstParameterValue(MODIFICATION_AMOUNT);
            String form = content.getFirstParameterValue(PRODUCT_FORM);
            String formDescription = content.getFirstParameterValue(FORM_DESCRIPTION);
            String isRecipeRequired = content.getFirstParameterValue(PRODUCT_RECIPE_REQUIRED);
            EnumSet<ProductOperationStatus> addStatus =
                    new ProductLogic().updateProduct(id, name, price, quantityModification, modificationAmount, form, formDescription, isRecipeRequired);
            if (addStatus.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                router.setPath(ConfigurationManager.getProperty(PageConfigConstant.PRODUCT_EDITED));
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (ProductOperationStatus it:addStatus) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(PRODUCT_NAME, name);
                content.putRequestAttribute(PRODUCT_PRICE, price);
                content.putRequestAttribute(QUANTITY_MODIFICATION, quantityModification);
                content.putRequestAttribute(MODIFICATION_AMOUNT, modificationAmount);
                content.putRequestAttribute(PRODUCT_FORM, form);
                content.putRequestAttribute(FORM_DESCRIPTION, formDescription);
                content.putRequestAttribute(PRODUCT_RECIPE_REQUIRED, isRecipeRequired);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getProperty(PageConfigConstant.EDIT_PRODUCT));
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
