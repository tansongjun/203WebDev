package net.csd.website.response;

import net.csd.website.model.DateTimeSlot;
import net.csd.website.model.QTicket;

public class QueueResponse {
    private QTicket qTicket;
    private DateTimeSlot dateTimeSlot;

    public QueueResponse(QTicket qTicket, DateTimeSlot dateTimeSlot) {
        this.qTicket = qTicket;
        this.dateTimeSlot = dateTimeSlot;
    }

    public QTicket getqTicket() {
        return qTicket;
    }

    public void setqTicket(QTicket qTicket) {
        this.qTicket = qTicket;
    }

    public DateTimeSlot getDateTimeSlot() {
        return dateTimeSlot;
    }

    public void setDateTimeSlot(DateTimeSlot dateTimeSlot) {
        this.dateTimeSlot = dateTimeSlot;
    }
}
