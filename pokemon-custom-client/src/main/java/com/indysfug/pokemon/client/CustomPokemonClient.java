package com.indysfug.pokemon.client;

import com.indysfug.pokemon.api.Pokemon;
import com.indysfug.pokemon.api.PokemonClient;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author russell.scheerer
 */
@Slf4j
public class CustomPokemonClient implements PokemonClient {

    private final RestTemplate restTemplate;

    public CustomPokemonClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Pokemon getPokemon(Integer pokeId) {
        log.info("###### Custom PokeApi REST call ######");

        String url = "https://pokeapi.co/api/v2/pokemon/"+pokeId+"/";
        log.info("Url: {}", url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<Pokemon> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Pokemon.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return null;
            }
        } catch (Throwable t) {
            if (t instanceof HttpClientErrorException) {
                HttpClientErrorException e = (HttpClientErrorException) t;
                log.error(e.getResponseBodyAsString());
            }
            log.error(t.getMessage());
            return null;
        }
    }
}
