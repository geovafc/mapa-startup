package br.com.mapastartup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.mapastartup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Membro.class);
        Membro membro1 = new Membro();
        membro1.setId(1L);
        Membro membro2 = new Membro();
        membro2.setId(membro1.getId());
        assertThat(membro1).isEqualTo(membro2);
        membro2.setId(2L);
        assertThat(membro1).isNotEqualTo(membro2);
        membro1.setId(null);
        assertThat(membro1).isNotEqualTo(membro2);
    }
}
