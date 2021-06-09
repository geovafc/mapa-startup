package br.com.mapastartup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.mapastartup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StartupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StartupDTO.class);
        StartupDTO startupDTO1 = new StartupDTO();
        startupDTO1.setId(1L);
        StartupDTO startupDTO2 = new StartupDTO();
        assertThat(startupDTO1).isNotEqualTo(startupDTO2);
        startupDTO2.setId(startupDTO1.getId());
        assertThat(startupDTO1).isEqualTo(startupDTO2);
        startupDTO2.setId(2L);
        assertThat(startupDTO1).isNotEqualTo(startupDTO2);
        startupDTO1.setId(null);
        assertThat(startupDTO1).isNotEqualTo(startupDTO2);
    }
}
