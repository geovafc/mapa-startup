package br.com.mapastartup.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembroMapperTest {

    private MembroMapper membroMapper;

    @BeforeEach
    public void setUp() {
        membroMapper = new MembroMapperImpl();
    }
}
