package by.bsu.machulski.command;

import by.bsu.machulski.content.SessionRequestContent;
import by.bsu.machulski.util.Router;

public abstract class AbstractCommand {
    protected static final String DEFAULT_LOCALE = "en-US";

    public abstract Router execute(SessionRequestContent content);
}
