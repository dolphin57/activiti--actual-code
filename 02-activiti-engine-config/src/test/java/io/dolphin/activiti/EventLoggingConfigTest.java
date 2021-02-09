package io.dolphin.activiti;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-1-30 17:59
 */
public class EventLoggingConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggingConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlogging.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();

        // 继续执行流程
        activitiRule.getTaskService().complete(task.getId());

        // 取出event logging数据
        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService()
                .getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            LOGGER.info("eventLogEntry.type = [{}], eventLogEntry.data = [{}]", eventLogEntry.getType(), new String(eventLogEntry.getData()));
        }
        LOGGER.info("eventLogEntries.size = [{}]", eventLogEntries.size());
    }
}
