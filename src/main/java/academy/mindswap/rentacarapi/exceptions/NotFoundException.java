package academy.mindswap.rentacarapi.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String entityType) {
        super(entityType + " not found.");
    }
}
