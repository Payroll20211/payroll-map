package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialSecurityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialSecurity.class);
        SocialSecurity socialSecurity1 = new SocialSecurity();
        socialSecurity1.setId(1L);
        SocialSecurity socialSecurity2 = new SocialSecurity();
        socialSecurity2.setId(socialSecurity1.getId());
        assertThat(socialSecurity1).isEqualTo(socialSecurity2);
        socialSecurity2.setId(2L);
        assertThat(socialSecurity1).isNotEqualTo(socialSecurity2);
        socialSecurity1.setId(null);
        assertThat(socialSecurity1).isNotEqualTo(socialSecurity2);
    }
}
