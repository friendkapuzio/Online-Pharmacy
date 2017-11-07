package by.bsu.machulski.command;

import by.bsu.machulski.controller.SessionRequestContent;
import by.bsu.machulski.controller.Router;

public abstract class AbstractCommand {
    protected static final String MESSAGE_PARAMETER = "message";
    protected static final String COMMAND_PARAMETER = "command";
    protected static final String USER = "user";

    public abstract Router execute(SessionRequestContent content);
}
