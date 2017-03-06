package com.indysfug.pokemon.autoconfigure;

import com.indysfug.pokemon.api.Pokemon;
import com.indysfug.pokemon.api.PokemonClient;
import com.indysfug.pokemon.api.PokemonSprites;
import lombok.extern.slf4j.Slf4j;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;

/**
 * @author russell.scheerer
 */
@Slf4j
class PokiApiWrappedPokemonClient implements PokemonClient {

    private final PokeApi pokeApi;

    PokiApiWrappedPokemonClient(PokeApi pokeApi) {
        this.pokeApi = pokeApi;
    }

    @Override
    public Pokemon getPokemon(Integer pokeId) {
        log.info("###### Official PokeApi Library call ######");
        me.sargunvohra.lib.pokekotlin.model.Pokemon otherApiPokemon = pokeApi.getPokemon(pokeId);
        String defaultImageUrl = otherApiPokemon.getSprites().getFrontDefault();

        return Pokemon.builder()
                .name(otherApiPokemon.getName())
                .sprites(PokemonSprites.builder().frontDefault(defaultImageUrl).build())
                .build();
    }
}
