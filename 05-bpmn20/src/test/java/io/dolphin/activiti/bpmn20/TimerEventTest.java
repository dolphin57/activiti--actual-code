package io.dolphin.activiti.bpmn20;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-24 8:24
 */
public class TimerEventTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerEventTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = "my-process-timer-boundary.bpmn20.xml")
    public void testTimerBoundary() throws InterruptedException {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        List<Task> taskList = taskService.createTaskQuery().listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = [{}]", task.getName());
        }
        LOGGER.info("task.size = [{}]", taskList.size());

        Thread.sleep(15 * 1000);

        taskList = taskService.createTaskQuery().listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = [{}]", task.getName());
        }
        LOGGER.info("task.size = [{}]", taskList.size());
    }
}
