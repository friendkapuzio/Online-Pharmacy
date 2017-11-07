package by.bsu.machulski.command.pharmacist;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.message.PharmacistActionMessage;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.ProductAttributeConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestoreProductsCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RestoreProductsCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String[] products = content.getParameterValues(ProductAttributeConstant.PRODUCTS_ATTRIBUTE).orElse(new String[]{});
            int updated = new ProductLogic().restoreDeletedById(products);
            String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
            if (updated == products.length) {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, PharmacistActionMessage.PRODUCTS_UPDATED);
            } else {
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, PharmacistActionMessage.PRODUCTS_UPDATE_FAILED);
            }
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(path);
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
