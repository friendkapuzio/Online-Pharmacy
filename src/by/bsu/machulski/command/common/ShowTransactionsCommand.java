package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.TransactionAttributeConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.TransactionDTO;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.TransactionLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowTransactionsCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowTransactionsCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String transactionsYear = content.getFirstParameterValue(TransactionAttributeConstant.TRANSACTIONS_YEAR);
            List<TransactionDTO> orders = new TransactionLogic().find(userId, transactionsYear);
            content.putRequestAttribute(TransactionAttributeConstant.TRANSACTIONS, orders);
            content.putRequestAttribute(TransactionAttributeConstant.TRANSACTIONS_YEAR, transactionsYear);
            router.setRoutingType(RoutingType.FORWARD);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.TRANSACTIONS));
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
