package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.util.Router;

public class SignOutCommand extends AbstractCommand {
    @Override
    public Router execute(SessionRequestContent content) {
        content.putSessionAttribute(SessionAttributeConstant.USERNAME, null);
        content.putSessionAttribute(SessionAttributeConstant.ROLE, null);
        content.putSessionAttribute(SessionAttributeConstant.USER_ID, null);
        //todo delete order
        Router router = new Router();
        router.setRoutingType(RoutingType.REDIRECT);
        router.setPath(ConfigurationManager.getProperty(PageConfigConstant.INDEX));
        return router;
    }
}
