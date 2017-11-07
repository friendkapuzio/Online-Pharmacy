package by.bsu.machulski.command.doctor;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.ProductAttributeConstant;
import by.bsu.machulski.constant.message.InfoMessage;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CreatePrescriptionFormCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(CreatePrescriptionFormCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.FIND_PATIENT));
        try {
            String productId = content.getFirstParameterValue(ProductAttributeConstant.PRODUCT_ID);
            Optional<Product> optionalProduct = new ProductLogic().findById(Long.parseLong(productId));
            if (optionalProduct.isPresent()) {
                content.putRequestAttribute(ProductAttributeConstant.PRODUCT, optionalProduct.get());
            } else {
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, InfoMessage.UNKNOWN_ERROR);
                router.setRoutingType(RoutingType.REDIRECT);
                router.setPath(path);
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
