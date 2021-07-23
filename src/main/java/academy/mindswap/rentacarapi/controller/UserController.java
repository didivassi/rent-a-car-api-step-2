package academy.mindswap.rentacarapi.controller;

import academy.mindswap.rentacarapi.command.user.CreateOrUpdateUserDto;
import academy.mindswap.rentacarapi.command.user.UserDetailsDto;
import academy.mindswap.rentacarapi.persistence.entity.UserEntity;
import academy.mindswap.rentacarapi.service.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * REST controller responsible for {@link UserEntity} related CRUD operations
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    /**
     * Create new user
     *
     * @param createOrUpdateUserDto new user data
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<UserDetailsDto>
    registration(@Valid @RequestBody CreateOrUpdateUserDto createOrUpdateUserDto,
                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_GATEWAY);
        }

        UserDetailsDto userDetailsDto = userService.createUser(createOrUpdateUserDto);
        return new ResponseEntity<>(userDetailsDto, CREATED);
    }

    /**
     * Get user by id
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable long userId) {
        UserDetailsDto userDetailsDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDetailsDto, OK);
    }

    /**
     * Get all users
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        List<UserDetailsDto> usersList = userService.getUsersList();
        return new ResponseEntity<>(usersList, OK);
    }

    /**
     * Update user
     *
     * @param userId                the user id
     * @param createOrUpdateUserDto the data to update
     * @return the response entity
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDetailsDto>
    updateUser(@PathVariable long userId,
               @Valid @RequestBody CreateOrUpdateUserDto createOrUpdateUserDto,
               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        UserDetailsDto userDetailsDto = userService.updateUser(userId, createOrUpdateUserDto);
        return new ResponseEntity<>(userDetailsDto, OK);
    }

    /**
     * Delete user
     *
     * @param userId the user id
     * @return the response entity
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(OK);
    }
}
