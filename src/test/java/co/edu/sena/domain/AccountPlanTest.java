package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountPlan.class);
        AccountPlan accountPlan1 = new AccountPlan();
        accountPlan1.setId(1L);
        AccountPlan accountPlan2 = new AccountPlan();
        accountPlan2.setId(accountPlan1.getId());
        assertThat(accountPlan1).isEqualTo(accountPlan2);
        accountPlan2.setId(2L);
        assertThat(accountPlan1).isNotEqualTo(accountPlan2);
        accountPlan1.setId(null);
        assertThat(accountPlan1).isNotEqualTo(accountPlan2);
    }
}
