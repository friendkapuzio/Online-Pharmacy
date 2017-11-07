package by.bsu.machulski.command.pharmacist;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SearchDeletedProductCommand  extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SearchDeletedProductCommand.class);
    private final String PRODUCTS_ATTRIBUTE = "products";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.PRODUCTS));
        try {
            String searchText = content.getFirstParameterValue(UserAttributeConstant.SEARCH_TEXT);
            List<Product> products = new ProductLogic().findDeletedProducts(searchText);
            content.putRequestAttribute(PRODUCTS_ATTRIBUTE, products);
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
