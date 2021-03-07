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
public class SubProcessTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubProcessTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        runtimeService.startProcessInstanceByKey("my-process");

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess2() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        Map<String, Object> variables = Maps.newHashMap();
        // 启动子流程的边界异常
        variables.put("errorFlag", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());

        Map<String, Object> existVariables = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("existVariables = [{}]", existVariables);
    }

    @Test
    @Deployment(resources = {"my-process-eventsubprocess1.bpmn20.xml"})
    public void testSubProcess3() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        Map<String, Object> variables = Maps.newHashMap();
        // 启动子流程的边界异常
        variables.put("errorFlag", true);
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());

        Map<String, Object> existVariables = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("existVariables = [{}]", existVariables);
    }

    @Test
    @Deployment(resources = {"my-process-callmainprocess.bpmn20.xml", "my-process-callsubprocess.bpmn20.xml"})
    public void testSubProcess4() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        Map<String, Object> variables = Maps.newHashMap();
        // 启动子流程的边界异常
        variables.put("errorFlag", false);
        variables.put("key0", "value0");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task.name = [{}]", task.getName());

        Map<String, Object> existVariables = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("existVariables = [{}]", existVariables);
    }
}
