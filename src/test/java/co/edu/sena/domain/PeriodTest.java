package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
        Period period1 = new Period();
        period1.setId(1L);
        Period period2 = new Period();
        period2.setId(period1.getId());
        assertThat(period1).isEqualTo(period2);
        period2.setId(2L);
        assertThat(period1).isNotEqualTo(period2);
        period1.setId(null);
        assertThat(period1).isNotEqualTo(period2);
    }
}
