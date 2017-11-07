package by.bsu.machulski.command;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    private static final String COMMAND_PARAMETER = "command";

    public AbstractCommand defineCommand(HttpServletRequest request) {
        String action = request.getParameter(COMMAND_PARAMETER);
        CommandType type = CommandType.valueOf(action.toUpperCase());
        return type.getCurrentCommand();
    }
}
