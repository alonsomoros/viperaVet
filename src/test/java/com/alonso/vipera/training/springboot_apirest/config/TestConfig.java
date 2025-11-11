package com.alonso.vipera.training.springboot_apirest.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@TestConfiguration
public class TestConfig {

    // Mock del contexto JPA para evitar problemas en los tests
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;
}