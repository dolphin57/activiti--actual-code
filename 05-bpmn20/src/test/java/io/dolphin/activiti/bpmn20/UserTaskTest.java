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

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-1 8:04
 */
public class UserTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        // 查询指定候选人的任务
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = [{}]", task);

        task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        LOGGER.info("find by user2 task = [{}]", task);

        task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        LOGGER.info("find by group1 task = [{}]", task);

        // 设置代理人
        taskService.claim(task.getId(), "user2");
        LOGGER.info("claim task.id = [{}] by user2", task.getId());
        // 这种只知道属性修改了，并不会做校验
        //taskService.setAssignee(task.getId(), "user2");

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        LOGGER.info("find by user1 task = [{}]", task);

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult();
        LOGGER.info("find by user2 task = [{}]", task);
    }

    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        // 查询指定候选人的任务
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = [{}]", task);

        taskService.complete(task.getId());
    }
}
