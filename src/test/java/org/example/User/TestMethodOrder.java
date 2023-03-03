package org.example.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

public @interface TestMethodOrder {

    Class<OrderAnnotation> value();

}
