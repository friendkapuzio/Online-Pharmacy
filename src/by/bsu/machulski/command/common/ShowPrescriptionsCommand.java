package by.bsu.machulski.command.common;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.PrescriptionAttributeConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.dto.PrescriptionDTO;
import by.bsu.machulski.exception.LogicException;
import by.bsu.machulski.exception.NoSuchParameterException;
import by.bsu.machulski.logic.PrescriptionLogic;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowPrescriptionsCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowPrescriptionsCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.PRESCRIPTIONS));
        try {
            long id = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            String isActual = content.getFirstParameterValue(PrescriptionAttributeConstant.IS_ACTUAL);
            List<PrescriptionDTO> prescriptionDTOs = new PrescriptionLogic().findByUser(id, isActual);
            content.putRequestAttribute(PrescriptionAttributeConstant.PRESCRIPTIONS_ATTRIBUTE, prescriptionDTOs);
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
