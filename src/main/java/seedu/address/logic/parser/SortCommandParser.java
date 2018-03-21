package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SortCommandParser implements Parser<SortCommand> {

    private static final String[] ALLOWED_PARAMETERS = {"name", "tag"};
    private int ARRAY_COUNTER = 0;
    private boolean VALID_PARAMETER = false;

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        while (ARRAY_COUNTER < ALLOWED_PARAMETERS.length) {
            if (trimmedArgs.equals(ALLOWED_PARAMETERS[ARRAY_COUNTER++])) {
                VALID_PARAMETER = true;
                break;
            }
        }

        if (!VALID_PARAMETER) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(trimmedArgs);
    }
}
