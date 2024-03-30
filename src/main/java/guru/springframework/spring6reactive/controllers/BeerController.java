package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import guru.springframework.spring6reactive.services.BeerServiceImpl;
import io.r2dbc.spi.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jt, Spring Framework Guru.
 */
@RestController
@AllArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH+"/{beerId}";

    private final BeerService beerService;

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable Integer beerId){
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerDto -> beerService.deleteBeerById(beerDto.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> getBeerList(){
        return beerService.listBeers();
    }
    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable Integer beerId,
                                              @Validated @RequestBody BeerDTO beerDTO){
        return beerService.patchBeer(beerDTO, beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(updatedDto -> ResponseEntity.ok().build());
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                               @Validated @RequestBody BeerDTO beerDTO){
        return beerService.updateBeer(beerDTO, beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> ResponseEntity.noContent().build());
    }



    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> saveNewBeer(@Validated
                                           @RequestBody BeerDTO beerDTO){
        AtomicInteger atomicInteger=new AtomicInteger();


        beerService.saveNewBeer(beerDTO).subscribe(
                savedBeer -> atomicInteger.set(savedBeer.getId())
        );

        return beerService.saveNewBeer(beerDTO)
                .map(savedBeer -> ResponseEntity.created(
                        UriComponentsBuilder
                                .fromHttpUrl(
                                        "http://localhost:8080/"+BEER_PATH+"/"+atomicInteger.get()
                                ).build().toUri()
                ).build());


    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId){
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }


}
