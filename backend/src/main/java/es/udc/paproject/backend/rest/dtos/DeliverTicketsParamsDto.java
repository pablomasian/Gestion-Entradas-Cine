package es.udc.paproject.backend.rest.dtos;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class DeliverTicketsParamsDto {

    private String creditCardNumber;

    public DeliverTicketsParamsDto() {}

    @NotNull
    @Size(min=1, max=32)
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
