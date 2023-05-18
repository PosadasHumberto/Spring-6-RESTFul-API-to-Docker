package kberestbrewery.web.controller;

import kberestbrewery.services.BeerService;
import kberestbrewery.web.model.BeerDTO;
import kberestbrewery.web.model.BeerPagedList;
import kberestbrewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class BeerController {


    private final BeerService beerService;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @GetMapping(path = "beer", produces = {"application/json"})
    public ResponseEntity<BeerPagedList> listBeers(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand
            ) {


        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        BeerPagedList beerPagedList = beerService.listBeers(
                beerName,
                beerStyle,
                PageRequest.of(pageNumber, pageSize),
                showInventoryOnHand
        );
        return new ResponseEntity<>(beerPagedList, HttpStatus.OK);
    }

    @GetMapping("beer/{beerId}")
    public ResponseEntity<BeerDTO> getBeerById(
            @PathVariable("beerId") UUID beerId,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand
    ) {
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        return new ResponseEntity<>(
                beerService.getById(beerId, showInventoryOnHand),
                HttpStatus.OK);
    }

    @PostMapping(path = "beer")
    public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDTO beerDTO) {

        BeerDTO savedBeer = beerService.saveNewBeer(beerDTO);

        return ResponseEntity
                .created(UriComponentsBuilder
                        .fromHttpUrl("http://org.hposadas.kberestbrewery/api/v1/beer/" + savedBeer.getId().toString())
                        .build().toUri())
                .build();
    }

    @PutMapping("beer/{beerId}")
    public ResponseEntity updateBeerById(
            @PathVariable("beerId") UUID beerId,
            @RequestBody @Validated BeerDTO beerDTO) {
        return new ResponseEntity(
                beerService.updateBeer(beerId, beerDTO),
                HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("beer/{beerId}")
    public ResponseEntity<Void> deleteBeerById (
            @PathVariable("beerId") UUID beerId) {
        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
