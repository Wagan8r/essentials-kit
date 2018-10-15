package com.bts.essentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

/**
 * Created by wagan8r on 9/14/18.
 */
@AutoConfigureMockMvc
public abstract class BaseMvcTest extends BaseIntegrationTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired()
    protected Filter springSecurityFilterChain;
}
