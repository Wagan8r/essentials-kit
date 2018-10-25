package com.bts.essentials;

import com.bts.essentials.model.BasicUser;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wagan8r on 2/17/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseIntegrationTest {
    protected BasicUser createBasicUserObject() {
        BasicUser basicUser = new BasicUser();
        basicUser.setEmail("nerfherder@bts.com");
        basicUser.setFirstName("Han");
        basicUser.setLastName("Solo");
        return basicUser;
    }
}
