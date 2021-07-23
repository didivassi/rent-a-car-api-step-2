package academy.mindswap.rentacarapi.controller;

import academy.mindswap.rentacarapi.command.car.CarDetailsDto;
import academy.mindswap.rentacarapi.command.car.CreateOrUpdateCarDto;
import academy.mindswap.rentacarapi.persistence.entity.CarEntity;
import academy.mindswap.rentacarapi.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller responsible for {@link CarEntity} related CRUD operations
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Create car
     *
     * @param createOrUpdateCarDto the car data
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<CarDetailsDto> createCar(@Valid @RequestBody CreateOrUpdateCarDto createOrUpdateCarDto) {
        CarDetailsDto carDetails = carService.addNewCar(createOrUpdateCarDto);
        return new ResponseEntity<>(carDetails, HttpStatus.CREATED);
    }

    /**
     * Get car be id
     *
     * @param carId the car id
     * @return the response entity
     */
    @GetMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> getCarById(@PathVariable long carId) {
        CarDetailsDto carDetails = carService.getCarById(carId);
        return new ResponseEntity<>(carDetails, HttpStatus.OK);
    }

    /**
     * Get all cars
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<CarDetailsDto>> getCarsList() {
        List<CarDetailsDto> usersList = carService.getCarsList();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    /**
     * Update car
     *
     * @param carId   the car id
     * @param createOrUpdateCarDto the data to update
     * @return the response entity
     */
    @PutMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> updateCar(@PathVariable long carId,
                                                   @Valid @RequestBody CreateOrUpdateCarDto createOrUpdateCarDto) {
        CarDetailsDto carDetailsDto = carService.updateCarDetails(carId, createOrUpdateCarDto);
        return new ResponseEntity<>(carDetailsDto, HttpStatus.OK);
    }

    /**
     * Delete car
     *
     * @param carId the car id
     * @return the response entity
     */
    @DeleteMapping("/{carId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long carId) {
        carService.deleteCar(carId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
