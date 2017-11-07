package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.entity.Product;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.logic.ProductLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class IndexCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(IndexCommand.class);
    private int TOP_SIZE = 10;
    private String TOP_CHEAPEST_ATTRIBUTE = "cheapestProducts";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.HOME));
        try {
            List<Product> products = new ProductLogic().takeCheapest(TOP_SIZE);
            content.putRequestAttribute(TOP_CHEAPEST_ATTRIBUTE, products);
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
