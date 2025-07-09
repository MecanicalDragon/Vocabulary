package net.medrag.vocabulary.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Stanislav Tretiakov
 * 09.07.2025
 */
@ConfigurationProperties("voc")
public class VocProps {
    private String dbUrl;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
}
