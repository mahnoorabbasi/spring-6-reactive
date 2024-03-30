package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> getBeerById(Integer id);
    Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO);
    Mono<Void> deleteBeerById(Integer id);
    Mono<BeerDTO> updateBeer(BeerDTO beerDTO,Integer integer);
    Mono<BeerDTO> patchBeer(BeerDTO beerDTO,Integer integer);


}

