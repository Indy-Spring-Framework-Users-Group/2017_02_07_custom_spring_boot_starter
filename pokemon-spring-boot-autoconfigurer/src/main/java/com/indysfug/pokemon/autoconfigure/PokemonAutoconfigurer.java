package com.indysfug.pokemon.autoconfigure;

import com.indysfug.pokemon.api.PokemonClient;
import com.indysfug.pokemon.autoconfigure.hackaroundssl.PokeApiSslHackUtil;
import com.indysfug.pokemon.autoconfigure.hackaroundssl.RestTemplateSslHackUtil;
import com.indysfug.pokemon.client.CustomPokemonClient;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author russell.scheerer
 */
@Configuration
@ConditionalOnProperty(name = "pokemon.enabled", matchIfMissing = true)
public class PokemonAutoconfigurer {

    @Configuration
    @ConditionalOnClass({ PokemonClient.class, PokeApi.class })
    public static class KotlinV2PokemonConfiguration {

        @Bean
        PokemonClient pokemonClient() {
            final PokeApi pokeApi = new PokeApiClient(PokeApiSslHackUtil.clientConfig());
            return new PokiApiWrappedPokemonClient(pokeApi);
        }
    }

    // Fallback attempt if a better client isn't available
    @Configuration
    @ConditionalOnMissingBean(PokemonClient.class)
    @ConditionalOnClass({ PokemonClient.class, CustomPokemonClient.class })
    public static class DefaultPokemonConfiguration {

        @Bean
        PokemonClient pokemonClient() throws Exception {
            return new CustomPokemonClient(RestTemplateSslHackUtil.restTemplate());
        }
    }
}
