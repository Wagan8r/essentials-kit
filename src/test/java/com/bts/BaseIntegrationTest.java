package com.bts;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by wagan8r on 2/17/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public abstract class BaseIntegrationTest {

    @Autowired
    protected EntityManager entityManager;

    protected void flush() {
        entityManager.flush();
    }
}
