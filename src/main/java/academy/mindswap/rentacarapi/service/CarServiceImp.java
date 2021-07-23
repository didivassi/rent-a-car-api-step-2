package academy.mindswap.rentacarapi.service;

import academy.mindswap.rentacarapi.command.car.CarDetailsDto;
import academy.mindswap.rentacarapi.command.car.CreateOrUpdateCarDto;
import academy.mindswap.rentacarapi.converter.CarDtoToCarEntityConverter;
import academy.mindswap.rentacarapi.converter.CarEntityToCarDtoConverter;
import academy.mindswap.rentacarapi.exceptions.NotFoundException;
import academy.mindswap.rentacarapi.persistence.entity.CarEntity;
import academy.mindswap.rentacarapi.persistence.entity.UserEntity;
import academy.mindswap.rentacarapi.persistence.repository.CarRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A {@link CarService} implementation
 */
@Service
public class CarServiceImp implements CarService {

    private final CarRepository carRepository;

    public CarServiceImp(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * @see CarService#addNewCar(CreateOrUpdateCarDto)
     */
    @Override
    public CarDetailsDto addNewCar(CreateOrUpdateCarDto carDetails) {
        CarEntity carEntity = CarDtoToCarEntityConverter.convert(carDetails);
        carEntity.setAvailable(true);
        carRepository.save(carEntity);
        return CarEntityToCarDtoConverter.convert(carEntity);
    }

    /**
     * @see CarService#getCarById(long)
     */
    @Override
    public CarDetailsDto getCarById(long carId) {
        CarEntity carEntity = getCarEntityById(carId);
        return  CarEntityToCarDtoConverter.convert(carEntity);
    }

    /**
     * @see CarService#getCarsList()
     */
    @Override
    public List<CarDetailsDto> getCarsList() {
        List<CarDetailsDto> carsListResponse = new ArrayList<>();

        for (CarEntity car : carRepository.findAll()) {
            carsListResponse.add(CarEntityToCarDtoConverter.convert(car));
        }

        return carsListResponse;
    }

    /**
     * @see CarService#deleteCar(long)
     */
    @Override
    public void deleteCar(long carId) {
        CarEntity carEntity  = getCarEntityById(carId);
        carRepository.delete(carEntity);
    }

    /**
     * @see CarService#updateCarDetails(long, CreateOrUpdateCarDto)
     */
    @Override
    public CarDetailsDto updateCarDetails(long carId, CreateOrUpdateCarDto carDetails) {
        CarEntity carEntity = getCarEntityById(carId);


        carEntity.setBrand(carDetails.getBrand());
        carEntity.setModelDescription(carDetails.getModelDescription());
        carEntity.setCarSegment(carDetails.getCarSegment());
        carEntity.setPlate(carDetails.getPlate());
        carEntity.setDateOfPurchase(carDetails.getDateOfPurchase());
        carRepository.save(carEntity);

        CarDetailsDto carDetailsDto = CarEntityToCarDtoConverter.convert(carEntity);

        return carDetailsDto;
    }

    protected CarEntity getCarEntityById(long carId) {
        Optional<CarEntity> carEntity = carRepository.findById(carId);
        return carEntity.orElseThrow(()-> new NotFoundException("Car") );
    }
}
