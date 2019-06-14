package ledahu.springframework.recipies.services;

import ledahu.springframework.recipies.commands.UnitOfMeasureCommand;
import ledahu.springframework.recipies.converters.UnitOfMeasureToUnitOfMeasureCommand;
import ledahu.springframework.recipies.repository.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand converter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converter = converter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll()
                .spliterator(), false)
                .map(converter::convert)
                .collect(Collectors.toSet());
    }
}
