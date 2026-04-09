package com.dentistarchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
// if few properties, separate
public class DentistArchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(DentistArchiveApplication.class, args);
    }

}
