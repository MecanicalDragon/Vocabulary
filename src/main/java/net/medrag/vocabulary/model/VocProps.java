package net.medrag.vocabulary.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Stanislav Tretiakov
 * 09.07.2025
 */
@Data
@ConfigurationProperties("voc")
public class VocProps {
    private String dbUrl;
}
