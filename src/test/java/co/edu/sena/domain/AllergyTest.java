package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllergyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Allergy.class); 
        /* se crea una alergia
        */
        
        Allergy allergy1 = new Allergy();
        allergy1.setId(1L);
        Allergy allergy2 = new Allergy();
        allergy2.setId(allergy1.getId());
        assertThat(allergy1).isEqualTo(allergy2);
        allergy2.setId(2L);
        assertThat(allergy1).isNotEqualTo(allergy2);
        allergy1.setId(null);
        assertThat(allergy1).isNotEqualTo(allergy2);
    }
}
