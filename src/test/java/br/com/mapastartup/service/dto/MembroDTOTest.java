package br.com.mapastartup.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.mapastartup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembroDTO.class);
        MembroDTO membroDTO1 = new MembroDTO();
        membroDTO1.setId(1L);
        MembroDTO membroDTO2 = new MembroDTO();
        assertThat(membroDTO1).isNotEqualTo(membroDTO2);
        membroDTO2.setId(membroDTO1.getId());
        assertThat(membroDTO1).isEqualTo(membroDTO2);
        membroDTO2.setId(2L);
        assertThat(membroDTO1).isNotEqualTo(membroDTO2);
        membroDTO1.setId(null);
        assertThat(membroDTO1).isNotEqualTo(membroDTO2);
    }
}
