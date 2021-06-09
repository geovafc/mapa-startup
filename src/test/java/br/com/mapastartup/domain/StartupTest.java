package br.com.mapastartup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.mapastartup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StartupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Startup.class);
        Startup startup1 = new Startup();
        startup1.setId(1L);
        Startup startup2 = new Startup();
        startup2.setId(startup1.getId());
        assertThat(startup1).isEqualTo(startup2);
        startup2.setId(2L);
        assertThat(startup1).isNotEqualTo(startup2);
        startup1.setId(null);
        assertThat(startup1).isNotEqualTo(startup2);
    }
}
