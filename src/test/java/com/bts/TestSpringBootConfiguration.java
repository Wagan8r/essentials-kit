package com.bts;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by wagan8r on 8/18/18.
 */
@SpringBootConfiguration
@ComponentScan
@PropertySource("classpath:essentials.properties")
public class TestSpringBootConfiguration {
}
