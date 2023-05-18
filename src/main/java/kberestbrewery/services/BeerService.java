package kberestbrewery.services;

import kberestbrewery.web.model.BeerDTO;
import kberestbrewery.web.model.BeerPagedList;
import kberestbrewery.web.model.BeerStyleEnum;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {

    BeerPagedList listBeers(
            String beerName,
            BeerStyleEnum beerStyle,
            PageRequest pageRequest,
            Boolean showInventoryOnHand
            );

    BeerDTO getById(UUID id, Boolean showInventoryOnHand);
    BeerDTO saveNewBeer(BeerDTO beerDTO);
    BeerDTO updateBeer(UUID beerId, BeerDTO beerDTO);
    BeerDTO getByUpc(String upc);
    void deleteBeerById(UUID beerId);
}
