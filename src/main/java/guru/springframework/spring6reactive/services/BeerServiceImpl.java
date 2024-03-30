package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.mappers.BeerMapper;
import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer id) {

        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDTO))
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer id) {
        return beerRepository.deleteById(id);

    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDTO, Integer id) {
       return beerRepository.findById(id)
                .map(foundBeer->{
                            foundBeer.setBeerName(beerDTO.getBeerName());
                            foundBeer.setPrice(beerDTO.getPrice());
                            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                            foundBeer.setPrice(beerDTO.getPrice());
                            return foundBeer;
                        })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO beerDTO, Integer id) {
        return beerRepository.findById(id)
                .map(foundBeer->{
                    if(StringUtils.hasText(beerDTO.getBeerName()))//use this for string based props
                        foundBeer.setBeerName(beerDTO.getBeerName());
                    if(beerDTO.getPrice()!=null)
                        foundBeer.setPrice(beerDTO.getPrice());
                    if(beerDTO.getBeerStyle()!=null)
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    if(beerDTO.getPrice()!=null)
                        foundBeer.setPrice(beerDTO.getPrice());
                    return foundBeer;
                })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);    }
}
