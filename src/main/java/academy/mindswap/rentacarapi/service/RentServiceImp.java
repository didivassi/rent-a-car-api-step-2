package academy.mindswap.rentacarapi.service;

import academy.mindswap.rentacarapi.command.car.CarDetailsDto;
import academy.mindswap.rentacarapi.command.rent.CreateOrUpdateRentDto;
import academy.mindswap.rentacarapi.command.rent.RentDetailsDto;
import academy.mindswap.rentacarapi.command.user.UserDetailsDto;
import academy.mindswap.rentacarapi.converter.CarDtoToCarEntityConverter;
import academy.mindswap.rentacarapi.converter.RentDtoToRentEntityConverter;
import academy.mindswap.rentacarapi.converter.RentEntityToRentDtoConverter;
import academy.mindswap.rentacarapi.converter.UserDtoToUserEntityConverter;
import academy.mindswap.rentacarapi.exceptions.CarNotAvailableException;
import academy.mindswap.rentacarapi.exceptions.InvalidRentalStatusException;
import academy.mindswap.rentacarapi.exceptions.NotFoundException;
import academy.mindswap.rentacarapi.persistence.entity.CarEntity;
import academy.mindswap.rentacarapi.persistence.entity.RentEntity;
import academy.mindswap.rentacarapi.persistence.entity.UserEntity;
import academy.mindswap.rentacarapi.persistence.repository.CarRepository;
import academy.mindswap.rentacarapi.persistence.repository.RentRepository;
import academy.mindswap.rentacarapi.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * An {@link RentService} implementation
 */
@Service
public class RentServiceImp implements RentService {

    private final RentRepository rentRepository;

    private final CarService carService;
    private final UserService userService;

    public RentServiceImp(RentRepository rentRepository, CarService carService, UserService userService) {
        this.rentRepository = rentRepository;

        this.carService = carService;
        this.userService = userService;
    }

    /**
     * @see RentService#addNewRent(CreateOrUpdateRentDto)
     */
    @Override
    public RentDetailsDto addNewRent(CreateOrUpdateRentDto createOrUpdateRentDto) {
        boolean carAvailable = rentRepository.isCarAvailableBetweenDates(
                createOrUpdateRentDto.getCarId(),
                createOrUpdateRentDto.getExpectedBeginDate(),
                createOrUpdateRentDto.getExpectedEndDate(),
                null);

        if (!carAvailable) {
            throw new CarNotAvailableException();
        }

        CarDetailsDto carDetailsDto = carService.getCarById(createOrUpdateRentDto.getCarId());
        CarEntity carEntity = CarDtoToCarEntityConverter.convert(carDetailsDto);


        UserDetailsDto userDetailsDto = userService.getUserById(createOrUpdateRentDto.getUserId());
        UserEntity userEntity = UserDtoToUserEntityConverter.convert(userDetailsDto);


        RentEntity rentEntity = RentDtoToRentEntityConverter.convert(createOrUpdateRentDto);

        rentEntity.setCarEntity(carEntity);
        rentEntity.setUserEntity(userEntity);

        rentEntity.setExpectedPrice(calculatePrice(createOrUpdateRentDto.getExpectedBeginDate(),
                createOrUpdateRentDto.getExpectedEndDate(), carEntity.getCarSegment().getDailyPrice()));

        rentRepository.save(rentEntity);

        return RentEntityToRentDtoConverter.convert(rentEntity);
    }

    /**
     * @see RentService#getRentById(long, long)
     */
    @Override
    public RentDetailsDto getRentById(long rentId, long userId) {
        Optional<RentEntity> rentEntityOptional = rentRepository.findByRentIdAndUserId(rentId, userId);

        if(rentEntityOptional.isEmpty()){
            throw new InvalidRentalStatusException();
        }

        return RentEntityToRentDtoConverter.convert(rentEntityOptional.get());
    }

    /**
     * @see RentService#getRentsList()
     */
    @Override
    public List<RentDetailsDto> getRentsList() {
        List<RentDetailsDto> rentListDto = new ArrayList<>();

        for (RentEntity rent : rentRepository.findAll()) {
            rentListDto.add(RentEntityToRentDtoConverter.convert(rent));
        }

        return rentListDto;
    }

    /**
     * @see RentService#deliverCar(long)
     */
    @Override
    public RentDetailsDto deliverCar(long rentId) {

        RentEntity rentEntity = getRentEntityById(rentId);

        rentEntity.setBeginDate(new Date());
        rentEntity.getCarEntity().setAvailable(false);
        rentRepository.save(rentEntity);

        return RentEntityToRentDtoConverter.convert(rentEntity);

    }

    /**
     * @see RentService#returnCar(long)
     */
    @Override
    public RentDetailsDto returnCar(long rentId) {
        RentEntity rentEntity = getRentEntityById(rentId);

        rentEntity.setEndDate(new Date());
        rentEntity.setFinalPrice(calculatePrice(
                rentEntity.getBeginDate(),
                rentEntity.getEndDate(),
                rentEntity.getCarEntity().getCarSegment().getDailyPrice()));

        rentEntity.getCarEntity().setAvailable(true);
        rentRepository.save(rentEntity);
        return RentEntityToRentDtoConverter.convert(rentEntity);
    }

    /**
     * @see RentService#deleteRent(long)
     */
    @Override
    public void deleteRent(long rentId) {
        RentEntity rentEntity = getRentEntityById(rentId);
        rentRepository.delete(rentEntity);
    }

    /**
     * Helper method to calculate the price considering the begin date, end date and the daily price
     *
     * @param beingDate
     * @param endDate
     * @param dailyPrice
     * @return {@link BigDecimal} the total price
     */
    private BigDecimal calculatePrice(Date beingDate, Date endDate, BigDecimal dailyPrice) {
        LocalDate beginLocalDate = LocalDate.ofInstant(beingDate.toInstant(), ZoneId.systemDefault());
        LocalDate endLocalDate = LocalDate.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

        long numOfDaysBetween = ChronoUnit.DAYS.between(beginLocalDate, endLocalDate);

        return dailyPrice.multiply(BigDecimal.valueOf(numOfDaysBetween != 0L ? numOfDaysBetween : 1L));
    }


    protected RentEntity getRentEntityById(long rentId) {
        Optional<RentEntity> rentEntity = rentRepository.findById(rentId);
        return rentEntity.orElseThrow(()-> new NotFoundException("Rental"));
    }
}
