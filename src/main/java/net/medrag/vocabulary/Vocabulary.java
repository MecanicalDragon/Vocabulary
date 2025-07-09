package net.medrag.vocabulary;

import net.medrag.vocabulary.model.VocProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(VocProps.class)
public class Vocabulary {

    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Vocabulary.class, args);
    }

    public static void shutDown(){
        ctx.close();
    }
}
