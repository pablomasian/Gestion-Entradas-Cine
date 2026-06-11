package es.udc.paproject.backend.model.exceptions;

import java.time.LocalDate;

@SuppressWarnings("serial")
public class InvalidSearchDateException extends Exception {

    private final LocalDate date;

    public InvalidSearchDateException(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
