package by.bsu.machulski.command.pharmacist;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.bsu.machulski.command.pharmacist.ProductAttributeConstant.PRODUCT_ID;

public class DeleteProductCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(DeleteProductCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            String id = content.getFirstParameterValue(PRODUCT_ID);
            new ProductLogic().deleteById(id);
        } catch (NoSuchParameterException e) {
            LOGGER.log(Level.WARN, "Wrong request parameters.");
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getProperty(PageConfigConstant.WRONG_REQUEST));
        }
        return router;
    }
}
