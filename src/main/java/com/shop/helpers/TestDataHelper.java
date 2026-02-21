package com.shop.helpers;

import net.datafaker.Faker;

import java.util.Map;

public class TestDataHelper {

    private static final Faker faker = new Faker();

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomPassword() {
        return faker.internet().password(8, 20);
    }

    public static String randomName() {
        return faker.name().fullName();
    }

    public static Map<String, Object> randomProduct(int categoryId) {
        return Map.of(
                "name", faker.commerce().productName(),
                "description", faker.lorem().sentence(10),
                "price", Double.parseDouble(faker.commerce().price(10, 1000)),
                "stock", faker.number().numberBetween(1, 100),
                "category_id", categoryId,
                "image_url", "https://example.com/images/" + faker.internet().slug() + ".jpg"
        );
    }
}
