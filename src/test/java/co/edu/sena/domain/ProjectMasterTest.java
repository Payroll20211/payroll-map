package co.edu.sena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.sena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectMaster.class);
        ProjectMaster projectMaster1 = new ProjectMaster();
        projectMaster1.setId(1L);
        ProjectMaster projectMaster2 = new ProjectMaster();
        projectMaster2.setId(projectMaster1.getId());
        assertThat(projectMaster1).isEqualTo(projectMaster2);
        projectMaster2.setId(2L);
        assertThat(projectMaster1).isNotEqualTo(projectMaster2);
        projectMaster1.setId(null);
        assertThat(projectMaster1).isNotEqualTo(projectMaster2);
    }
}
