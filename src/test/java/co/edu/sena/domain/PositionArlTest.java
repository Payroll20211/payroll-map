package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PositionArlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionArl.class);
        PositionArl positionArl1 = new PositionArl();
        positionArl1.setId(1L);
        PositionArl positionArl2 = new PositionArl();
        positionArl2.setId(positionArl1.getId());
        assertThat(positionArl1).isEqualTo(positionArl2);
        positionArl2.setId(2L);
        assertThat(positionArl1).isNotEqualTo(positionArl2);
        positionArl1.setId(null);
        assertThat(positionArl1).isNotEqualTo(positionArl2);
    }
}
