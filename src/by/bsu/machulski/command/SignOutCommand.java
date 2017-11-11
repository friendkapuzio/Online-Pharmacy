package by.bsu.machulski.command;

import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.RoutingType;
import by.bsu.machulski.controller.Router;
import by.bsu.machulski.type.UserRole;

public class SignOutCommand extends AbstractCommand {
    @Override
    public Router execute(SessionRequestContent content) {
        content.putSessionAttribute(SessionAttributeConstant.USERNAME, null);
        content.putSessionAttribute(SessionAttributeConstant.ROLE, UserRole.GUEST);
        content.putSessionAttribute(SessionAttributeConstant.USER_ID, null);
        content.putSessionAttribute(SessionAttributeConstant.REGISTRATION_DATE, null);
        content.putSessionAttribute(SessionAttributeConstant.CART, null);
        return new Router(RoutingType.REDIRECT, ConfigurationManager.getPath(PageConfigConstant.INDEX));
    }
}
