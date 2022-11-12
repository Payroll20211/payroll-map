package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperatorMatrizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperatorMatriz.class);
        OperatorMatriz operatorMatriz1 = new OperatorMatriz();
        operatorMatriz1.setId(1L);
        OperatorMatriz operatorMatriz2 = new OperatorMatriz();
        operatorMatriz2.setId(operatorMatriz1.getId());
        assertThat(operatorMatriz1).isEqualTo(operatorMatriz2);
        operatorMatriz2.setId(2L);
        assertThat(operatorMatriz1).isNotEqualTo(operatorMatriz2);
        operatorMatriz1.setId(null);
        assertThat(operatorMatriz1).isNotEqualTo(operatorMatriz2);
    }
}
