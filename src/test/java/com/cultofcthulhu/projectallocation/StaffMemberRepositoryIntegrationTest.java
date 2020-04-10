package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.data.StaffMemberDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StaffMemberRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StaffMemberDAO staffMemberDAO;

    @Test
    public void test_findByName() {
        StaffMember staffMember = new StaffMember("Jack Price", null, null);
        entityManager.persist(staffMember);
        entityManager.flush();

        StaffMember found = staffMemberDAO.findByName("Jack Price");

        assertThat(found.getName())
                .isEqualTo(staffMember.getName());
    }
}
