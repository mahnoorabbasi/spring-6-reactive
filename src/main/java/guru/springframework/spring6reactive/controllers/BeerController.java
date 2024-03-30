package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import guru.springframework.spring6reactive.services.BeerServiceImpl;
import io.r2dbc.spi.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(BEER_PATH_ID)
    private Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer id){
        return beerService.getBeerById(id);
    }

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> getBeerList(){
        return beerService.listBeers();
    }
    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeerById(@PathVariable Integer beerId,
                                             @Validated @RequestBody BeerDTO beerDTO){

        return beerService.patchBeer(beerDTO,beerId)
                .map(savedDto-> ResponseEntity.ok().build());


    }
    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeerById(@PathVariable Integer beerId,
                                              @Validated @RequestBody BeerDTO beerDTO){

        return beerService.updateBeer(beerDTO,beerId)
                .map(savedDto-> ResponseEntity.ok().build());


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

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable Integer beerId){
        return beerService.deleteBeerById(beerId)
                .map(response->
                    ResponseEntity.noContent().build()
                );
    }


}
