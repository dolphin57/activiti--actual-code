package io.dolphin.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-1-24 18:48
 */
public class ConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    /**
     * 依赖于spring
     */
    @Test
    public void testConfigDefault() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        LOGGER.info("默认流程引擎配置对象:[{}]", configuration);
    }

    @Test
    public void testConfigStandalone() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        LOGGER.info("Standalone流程引擎配置对象:[{}]", configuration);
    }
}
