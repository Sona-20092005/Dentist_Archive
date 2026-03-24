package com.dentistarchive.utils;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class UUIDGenerator {

    public static UUID generateUUIDv7() {
        return Generators.timeBasedEpochGenerator().generate();
    }
}
