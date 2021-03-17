package io.dolphin.activiti.springboot;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootApplicationTests.class);

    @Autowired
    private RuntimeService runtimeService;

    @Test
    void contextLoads() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance = [{}]", processInstance);
    }
}
