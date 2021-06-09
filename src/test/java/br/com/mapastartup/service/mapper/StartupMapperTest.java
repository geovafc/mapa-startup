package br.com.mapastartup.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StartupMapperTest {

    private StartupMapper startupMapper;

    @BeforeEach
    public void setUp() {
        startupMapper = new StartupMapperImpl();
    }
}
