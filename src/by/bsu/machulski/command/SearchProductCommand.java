package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SearchProductCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SearchProductCommand.class);
    private final String SEARCH_TEXT = "searchText";
    private final String PRODUCTS_ATTRIBUTE = "products";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getProperty(PageConfigConstant.PRODUCTS));
        try {
            String searchText = content.getFirstParameterValue(SEARCH_TEXT);
            List<Product> products = new ProductLogic().findProducts(searchText);
            content.putRequestAttribute(PRODUCTS_ATTRIBUTE, products);
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
