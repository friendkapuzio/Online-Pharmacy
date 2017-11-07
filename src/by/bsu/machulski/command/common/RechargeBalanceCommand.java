package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.*;
import by.bsu.machulski.constant.message.AccountActionMessage;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.UserLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.resource.MessageManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.type.UserOperationError;
import by.bsu.machulski.controller.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

public class RechargeBalanceCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(RechargeBalanceCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router();
        try {
            long userId = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String cardNumber = content.getFirstParameterValue(CardAttributeConstant.CARD_NUMBER);
            String validMonth = content.getFirstParameterValue(CardAttributeConstant.VALID_MONTH);
            String validYear = content.getFirstParameterValue(CardAttributeConstant.VALID_YEAR);
            String cardVerificationCode = content.getFirstParameterValue(CardAttributeConstant.VERIFICATION_CODE);
            String cardHolder = content.getFirstParameterValue(CardAttributeConstant.CARD_HOLDER);
            String amount = content.getFirstParameterValue(UserAttributeConstant.TRANSACTION_AMOUNT);
            EnumSet<UserOperationError> errors = new UserLogic().rechargeBalance(userId, cardNumber, validMonth, validYear,
                    cardVerificationCode, cardHolder, amount);
            if (errors.isEmpty()) {
                router.setRoutingType(RoutingType.REDIRECT);
                String path = ConfigurationManager.getPath(PageConfigConstant.MESSAGE);
                path = ConfigurationManager.addParameter(path, MESSAGE_PARAMETER, AccountActionMessage.BALANCE_RECHARGED);
                router.setPath(path);
            } else {
                String locale = (String) content.getSessionAttribute(SessionAttributeConstant.LOCALE);
                for (UserOperationError it:errors) {
                    content.putRequestAttribute(it.getMessageAttribute(), MessageManager.getProperty(it.getMessagePath(), locale));
                }
                content.putRequestAttribute(UserAttributeConstant.TRANSACTION_AMOUNT, amount);
                router.setRoutingType(RoutingType.FORWARD);
                router.setPath(ConfigurationManager.getPath(PageConfigConstant.RECHARGE_BALANCE));
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
