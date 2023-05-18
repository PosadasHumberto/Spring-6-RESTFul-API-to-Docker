package kberestbrewery.services;

import kberestbrewery.domain.Beer;
import kberestbrewery.repositories.BeerRepository;
import kberestbrewery.web.controller.NotFoundException;
import kberestbrewery.web.mappers.BeerMapper;
import kberestbrewery.web.model.BeerDTO;
import kberestbrewery.web.model.BeerPagedList;
import kberestbrewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    /*Cuando se aplica la anotación @Cacheable a un método, Spring busca en la
    caché si el método ha sido invocado previamente con los mismos argumentos.
    Si se encuentra en caché el resultado correspondiente, se devuelve directamente
    desde la caché sin ejecutar el método nuevamente. Si no se encuentra en caché,
    se ejecuta el método y el resultado se almacena en la caché para futuras
    invocaciones con los mismos argumentos.
    cacheNames = "beerListCache", lo que significa que los resultados se almacenarán
    en una caché llamada "beerListCache"
    condition: Este atributo permite especificar una condición que debe cumplirse para
    que se realice el almacenamiento en caché*/
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(
            String beerName,
            BeerStyleEnum beerStyle,
            PageRequest pageRequest,
            Boolean showInventoryOnHand) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && StringUtils.hasText(String.valueOf(beerStyle))){
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(
                    beerName,
                    beerStyle,
                    pageRequest);
        } else if (StringUtils.hasText(beerName) && !StringUtils.hasText(String.valueOf(beerStyle))) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && StringUtils.hasText(String.valueOf(beerStyle))) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beer -> beerMapper.beerToBeerDtoWithInventory(beer))
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beer -> beerMapper.beerToBeerDto(beer))
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }
        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false ")
    @Override
    public BeerDTO getById(UUID beerId, Boolean showInventoryOnHand){
        if (showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        } else {
            return beerMapper.beerToBeerDto(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        }
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }

    @Override
    public BeerDTO updateBeer(UUID beerId, BeerDTO beerDTO) {
        Beer foundBeer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        foundBeer.setBeerName(beerDTO.getBeerName());
        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
        foundBeer.setPrice(beerDTO.getPrice());
        foundBeer.setUpc(beerDTO.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(foundBeer));
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public BeerDTO getByUpc(String upc) {
        return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc));
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }
}
