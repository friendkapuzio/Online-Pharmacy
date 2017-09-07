package by.bsu.machulski.factory;

import by.bsu.machulski.command.AbstractCommand;
import by.bsu.machulski.command.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    private final static Logger LOGGER = LogManager.getLogger(CommandFactory.class);
    private static final String COMMAND_PARAMETER = "command";

    public AbstractCommand defineCommand(HttpServletRequest request) {
        String action = request.getParameter(COMMAND_PARAMETER);
        CommandType type = CommandType.valueOf(action.toUpperCase());
        return type.getCurrentCommand();
    }
}
