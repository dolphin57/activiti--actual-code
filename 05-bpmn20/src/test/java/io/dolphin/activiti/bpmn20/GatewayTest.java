package io.dolphin.activiti.bpmn20;

import com.google.common.collect.Maps;
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
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-6 16:21
 */
public class GatewayTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-exclusivegateway1.bpmn20.xml"})
    public void testExclusiveGateway1() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 70);
        runtimeService.startProcessInstanceByKey("my-process", variables);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-parallelgateway1.bpmn20.xml"})
    public void testParallelGateway1() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = [{}]", task.getName());
            taskService.complete(task.getId());
        }
        LOGGER.info("task.size = [{}]",taskList.size());

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());

    }
}
