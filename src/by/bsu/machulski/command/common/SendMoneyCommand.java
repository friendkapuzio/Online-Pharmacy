package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.constant.UserAttributeConstant;
import by.bsu.machulski.constant.message.AccountActionMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.type.UserOperationError;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class SendMoneyCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(SendMoneyCommand.class);
    private static final String RECEIVER_EMAIL = "receiverEmail";

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String receiverEmail = content.getFirstParameterValue(RECEIVER_EMAIL);
            String amount = content.getFirstParameterValue(UserAttributeConstant.TRANSACTION_AMOUNT);
            String password = content.getFirstParameterValue(UserAttributeConstant.PASSWORD);
            EnumSet<UserOperationError> errors = new UserLogic().transferMoney(userId, receiverEmail, amount, password);
            if (errors.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AccountActionMessage.MONEY_TRANSFERRED);
                router.setPath(path);
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (UserOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(RECEIVER_EMAIL, receiverEmail);
                content.putRequestAttribute(UserAttributeConstant.TRANSACTION_AMOUNT, amount);
                String currentBalance = content.getFirstParameterValue(UserAttributeConstant.CURRENT_BALANCE);
                content.putRequestAttribute(UserAttributeConstant.CURRENT_BALANCE, currentBalance);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.SEND_MONEY));
            }
        } catch (NoSuchParameterException e ) {
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
