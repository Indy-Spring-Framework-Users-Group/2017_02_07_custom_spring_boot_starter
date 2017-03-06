package com.indysfug.example;

import com.indysfug.pokemon.api.Pokemon;
import com.indysfug.pokemon.api.PokemonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author russell.scheerer
 */
@SpringBootApplication
@Slf4j
public class Application {

    private static final Integer PIKACHU_ID = 25;

    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }

    @Bean
    CommandLineRunner example(PokemonClient pokemonClient) {
        return args -> {
            int pokemonId = PIKACHU_ID;
            if (args.length > 0) {
                pokemonId = Integer.parseInt(args[0]);
            }

            Pokemon pokemon = pokemonClient.getPokemon(pokemonId);
            log.info("Pokemon name: {}", pokemon.getName());
            log.info("Pokemon default image: {}", pokemon.getSprites().getFrontDefault());
        };
    }
}
