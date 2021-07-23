package academy.mindswap.rentacarapi.service;

import academy.mindswap.rentacarapi.command.user.CreateOrUpdateUserDto;
import academy.mindswap.rentacarapi.command.user.UserDetailsDto;
import academy.mindswap.rentacarapi.converter.UserDtoToUserEntityConverter;
import academy.mindswap.rentacarapi.converter.UserEntityToUserDtoConverter;
import academy.mindswap.rentacarapi.exceptions.NotFoundException;
import academy.mindswap.rentacarapi.persistence.entity.UserEntity;
import academy.mindswap.rentacarapi.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An {@link UserService} implementation
 */
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @see UserService#createUser(CreateOrUpdateUserDto)
     */
    @Override
    public UserDetailsDto createUser(CreateOrUpdateUserDto userRegistrationDto) {
        UserEntity userEntity = UserDtoToUserEntityConverter.convert(userRegistrationDto);
        UserEntity createdUser = userRepository.save(userEntity);
        return UserEntityToUserDtoConverter.convert(createdUser);
    }

    /**
     * @see UserService#getUserById(long)
     */
    @Override
    public UserDetailsDto getUserById(long userId) {
        UserEntity userEntity = getUserEntityById(userId);
        return UserEntityToUserDtoConverter.convert(userEntity);
    }

    /**
     * @see UserService#getUsersList()
     */
    @Override
    public List<UserDetailsDto> getUsersList() {
        List<UserDetailsDto> usersListResponse = new ArrayList<>();

        for (UserEntity user : userRepository.findAll()) {
            usersListResponse.add(UserEntityToUserDtoConverter.convert(user));
        }

        return usersListResponse;
    }

    /**
     * @see UserService#deleteUser(long)
     */
    @Override
    public void deleteUser(long userId) {
        UserEntity user = getUserEntityById(userId);
        userRepository.delete(user);
    }

    /**
     * @see UserService#updateUser(long, CreateOrUpdateUserDto)
     */
    @Override
    public UserDetailsDto updateUser(long userId, CreateOrUpdateUserDto createOrUpdateUserDto) {
        UserEntity userEntity = getUserEntityById(userId);

        userEntity.setFirstName(createOrUpdateUserDto.getFirstName());
        userEntity.setLastName(createOrUpdateUserDto.getLastName());
        userEntity.setEmail(createOrUpdateUserDto.getEmail());
        userEntity.setLicenseId(createOrUpdateUserDto.getLicenseId());

        userRepository.save(userEntity);

        return UserEntityToUserDtoConverter.convert(userEntity);
    }

    protected UserEntity getUserEntityById(long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity.orElseThrow(()-> new NotFoundException("User"));
    }
}
