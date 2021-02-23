package io.dolphin.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-19 8:18
 */
public class DbRuntimTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbRuntimTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testRuntime() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        repositoryService.createDeployment()
                .name("二次审批流程")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("second_approve", variables);
    }

    @Test
    public void testSetOwner() {
        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("second_approve")
                .singleResult();
        taskService.setOwner(task.getId(), "user1");
    }

    /**
     * 基于流程的没有实例等信息
     */
    @Test
    public void testMessage() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        repositoryService.createDeployment()
                .addClasspathResource("my-process-message.bpmn20.xml")
                .deploy();
    }

    /**
     * 基于任务的监听是有流程实例等信息的
     */
    @Test
    public void testMessageReceived() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        repositoryService.createDeployment()
                .addClasspathResource("my-process-message-received.bpmn20.xml")
                .deploy();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
    }

    @Test
    public void testJob() throws InterruptedException {
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        repositoryService.createDeployment()
                .addClasspathResource("my-process-job.bpmn20.xml")
                .deploy();

        Thread.sleep(30*1000L);
    }
}
