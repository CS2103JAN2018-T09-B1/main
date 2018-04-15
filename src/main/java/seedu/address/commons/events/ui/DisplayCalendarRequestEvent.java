package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to display Calendar
 */
public class DisplayCalendarRequestEvent extends BaseEvent {

    String parameter;

    public DisplayCalendarRequestEvent(String parameter) {
        this.parameter = parameter;
    }
    @Override
    public String toString() {
        return this.parameter;
    }
}
