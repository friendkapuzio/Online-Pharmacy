package by.bsu.machulski.command.doctor;

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

public class ShowGivenPrescriptionsCommand extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowGivenPrescriptionsCommand.class);

    @Override
    public Router execute(SessionRequestContent content) {
        Router router = new Router(RoutingType.FORWARD, ConfigurationManager.getPath(PageConfigConstant.MANAGE_PRESCRIPTIONS));
        try {
            long id = (long) content.getSessionAttribute(SessionAttributeConstant.USER_ID);
            List<PrescriptionDTO> prescriptionDTOs = new PrescriptionLogic().findByDoctor(id);
            content.putRequestAttribute(PrescriptionAttributeConstant.PRESCRIPTIONS_ATTRIBUTE, prescriptionDTOs);
        } catch (LogicException e) {
            LOGGER.log(Level.ERROR, "Logic error", e);
            router.setRoutingType(RoutingType.REDIRECT);
            router.setPath(ConfigurationManager.getPath(PageConfigConstant.ERROR_LOGIC));
        }
        return router;
    }
}
