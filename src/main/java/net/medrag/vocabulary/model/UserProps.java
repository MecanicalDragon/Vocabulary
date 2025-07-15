package net.medrag.vocabulary.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Stanislav Tretiakov
 * 09.07.2025
 */
@ConfigurationProperties("user")
public class UserProps {
    private int user;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
