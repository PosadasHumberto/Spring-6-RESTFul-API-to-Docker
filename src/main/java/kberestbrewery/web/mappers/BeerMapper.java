package kberestbrewery.web.mappers;

import kberestbrewery.domain.Beer;
import kberestbrewery.web.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);
    BeerDTO beerToBeerDtoWithInventory(Beer beer);
    Beer beerDtoToBeer(BeerDTO beerDTO);
}
