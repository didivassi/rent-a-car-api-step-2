package academy.mindswap.rentacarapi.controller;

import academy.mindswap.rentacarapi.command.rent.CreateOrUpdateRentDto;
import academy.mindswap.rentacarapi.command.rent.RentDetailsDto;
import academy.mindswap.rentacarapi.persistence.entity.RentEntity;
import academy.mindswap.rentacarapi.service.RentServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST controller responsible for {@link RentEntity} related CRUD operations
 */
@RestController
@RequestMapping("/api/rents")
public class RentController {

    private RentServiceImp rentService;

    public RentController(RentServiceImp rentService) {
        this.rentService = rentService;
    }

    /**
     * Create rent
     *
     * @param createOrUpdateRentDto the new rent data
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<RentDetailsDto>
    createRent(@Valid @RequestBody CreateOrUpdateRentDto createOrUpdateRentDto,
               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        RentDetailsDto rentDetails = rentService.addNewRent(createOrUpdateRentDto);
        return new ResponseEntity<>(rentDetails, CREATED);
    }

    /**
     * Get rent by id
     *
     * @param rentId the rent id
     * @return the response entity
     */
    @GetMapping("/{rentId}/user/{userId}")
    public ResponseEntity<RentDetailsDto> getRentById(@PathVariable long userId, @PathVariable long rentId) {
        RentDetailsDto rentDetails = rentService.getRentById(rentId, userId);
        return new ResponseEntity<>(rentDetails, OK);
    }

    /**
     * Get rent list
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<RentDetailsDto>> getRentList() {
        return new ResponseEntity<>(rentService.getRentsList(), OK);
    }

    /**
     * Deliver car
     *
     * @param rentId the rent id
     * @return the response entity
     */
    @PutMapping("/{rentId}/deliver")
    public ResponseEntity<RentDetailsDto> deliverCar(@PathVariable long rentId) {
        RentDetailsDto rentDetails = rentService.deliverCar(rentId);
        return new ResponseEntity<>(rentDetails, OK);
    }

    /**
     * Return car
     *
     * @param rentId the rentId
     * @return the response entity
     */
    @PutMapping("/{rentId}/return")
    public ResponseEntity<RentDetailsDto> returnCar(@PathVariable long rentId) {
        RentDetailsDto rentDetailsDto = rentService.returnCar(rentId);
        return new ResponseEntity<>(rentDetailsDto, OK);
    }

    /**
     * Delete rent
     *
     * @param rentId the rent id
     * @return the response entity
     */
    @DeleteMapping("/{rentId}")
    public ResponseEntity<HttpStatus> deleteRent(@PathVariable long rentId) {
        rentService.deleteRent(rentId);
        return new ResponseEntity<>(OK);
    }
}
