package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperatorTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperatorType.class);
        OperatorType operatorType1 = new OperatorType();
        operatorType1.setId(1L);
        OperatorType operatorType2 = new OperatorType();
        operatorType2.setId(operatorType1.getId());
        assertThat(operatorType1).isEqualTo(operatorType2);
        operatorType2.setId(2L);
        assertThat(operatorType1).isNotEqualTo(operatorType2);
        operatorType1.setId(null);
        assertThat(operatorType1).isNotEqualTo(operatorType2);
    }
}
