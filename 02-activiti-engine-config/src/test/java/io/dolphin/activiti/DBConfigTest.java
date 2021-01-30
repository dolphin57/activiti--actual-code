package io.dolphin.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-1-26 8:11
 */
public class DBConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfigTest.class);

    @Test
    public void testConfigDefault() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        LOGGER.info("默认流程引擎配置对象:[{}]", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 [{}]", processEngine.getName());
        processEngine.close();
    }

    @Test
    public void testConfigResource() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
        LOGGER.info("默认流程引擎配置对象:[{}]", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 [{}]", processEngine.getName());
        processEngine.close();
    }
}
