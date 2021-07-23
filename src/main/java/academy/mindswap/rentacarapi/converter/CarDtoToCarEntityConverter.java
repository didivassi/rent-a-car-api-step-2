package academy.mindswap.rentacarapi.converter;

import academy.mindswap.rentacarapi.command.car.CarDetailsDto;
import academy.mindswap.rentacarapi.command.car.CreateOrUpdateCarDto;
import academy.mindswap.rentacarapi.persistence.entity.CarEntity;

/**
 * Converter helper for car converting between DTO and entity
 */
public class CarDtoToCarEntityConverter {

    /**
     * Convert from {@link CreateOrUpdateCarDto} to {@link CarEntity}
     * @param createOrUpdateCarDto
     * @return {@link CarEntity}
     */
    public static CarEntity convert(CreateOrUpdateCarDto createOrUpdateCarDto) {
        return CarEntity.builder()
                .brand(createOrUpdateCarDto.getBrand())
                .modelDescription(createOrUpdateCarDto.getModelDescription())
                .dateOfPurchase(createOrUpdateCarDto.getDateOfPurchase())
                .carSegment(createOrUpdateCarDto.getCarSegment())
                .plate(createOrUpdateCarDto.getPlate())
                .build();
    }

    public static CarEntity convert(CarDetailsDto carDetailsDto) {
        return CarEntity.builder()
                .carId(carDetailsDto.getCarId())
                .brand(carDetailsDto.getBrand())
                .modelDescription(carDetailsDto.getModelDescription())
                .dateOfPurchase(carDetailsDto.getDateOfPurchase())
                .carSegment(carDetailsDto.getCarSegment())
                .plate(carDetailsDto.getPlate())
                .build();
    }
}
