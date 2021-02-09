package io.dolphin.activiti;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-1-30 17:59
 */
public class MDCConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MDCConfigTest.class);

    @Rule
    //public ActivitiRule activitiRule = new ActivitiRule();
    public ActivitiRule activitiRule = new ActivitiRule("activiti_mdc.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        // 打开MDC开关
        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("Activiti is awesome!", task.getName());

        // 继续执行流程
        activitiRule.getTaskService().complete(task.getId());
    }
}
