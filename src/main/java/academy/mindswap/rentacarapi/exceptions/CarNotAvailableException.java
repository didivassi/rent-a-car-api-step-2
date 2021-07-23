package academy.mindswap.rentacarapi.exceptions;

public class CarNotAvailableException extends RuntimeException  {
    public CarNotAvailableException() {
        super("Car not available");
    }
}
