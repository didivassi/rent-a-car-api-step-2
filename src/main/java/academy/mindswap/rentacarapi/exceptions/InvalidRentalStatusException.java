package academy.mindswap.rentacarapi.exceptions;

public class InvalidRentalStatusException extends RuntimeException {

    public InvalidRentalStatusException() {
        super("Object already exists");
    }
}
