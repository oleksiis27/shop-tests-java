package com.shop.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources("classpath:app.properties")
public interface AppConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("ui.url")
    String uiUrl();

    @Key("admin.email")
    String adminEmail();

    @Key("admin.password")
    String adminPassword();

    @Key("user.email")
    String userEmail();

    @Key("user.password")
    String userPassword();

    static AppConfig get() {
        return ConfigFactory.create(AppConfig.class, System.getProperties());
    }
}
