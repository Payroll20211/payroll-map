package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialPaymentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialPayments.class);
        SocialPayments socialPayments1 = new SocialPayments();
        socialPayments1.setId(1L);
        SocialPayments socialPayments2 = new SocialPayments();
        socialPayments2.setId(socialPayments1.getId());
        assertThat(socialPayments1).isEqualTo(socialPayments2);
        socialPayments2.setId(2L);
        assertThat(socialPayments1).isNotEqualTo(socialPayments2);
        socialPayments1.setId(null);
        assertThat(socialPayments1).isNotEqualTo(socialPayments2);
    }
}
