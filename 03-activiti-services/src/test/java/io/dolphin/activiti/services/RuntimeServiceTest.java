package io.dolphin.activiti.services;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-9 15:58
 */
public class RuntimeServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 流程启动控制key测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessByKey() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        // 根据key默认使用的是流程最新版本
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);
    }

    /**
     * 流程启动控制id测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessById() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
        LOGGER.info("processInstance = [{}]", processInstance);
    }

    /**
     * 流程启动processInstanceBuilder测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceBuilder() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");

        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("my-process")
                .variables(variables)
                .start();

        LOGGER.info("processInstance = [{}]", processInstance);
    }

    /**
     * 流程变量测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testVariables() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        runtimeService.setVariable(processInstance.getId(), "key3", "value3");
        runtimeService.setVariable(processInstance.getId(), "key2", "value2_1");

        // 此处需要executionId,与流程实例id ProcessInstanceId是一致的
        Map<String, Object> existVariables = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("existVariables = [{}]", existVariables);
    }

    /**
     * 流程实例查询测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        ProcessInstance existProcessInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
    }

    /**
     * 流程对象查询测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        List<Execution> executionList = runtimeService.createExecutionQuery()
                .listPage(0, 100);
        for (Execution execution : executionList) {
            LOGGER.info("execution = [{}]", execution);
        }
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-trigger.bpmn20.xml"})
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);

        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
    public void testSingleEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);

        runtimeService.signalEventReceived("my-signal");

        execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message-received.bpmn20.xml"})
    public void testMessageEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);

        runtimeService.messageEventReceived("my-message", execution.getId());

        execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message.bpmn20.xml"})
    public void testMessageStart() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                //.startProcessInstanceByKey("my-process");
            .startProcessInstanceByMessage("my-message");
        LOGGER.info("processInstance = [{}]", processInstance);
    }
}
