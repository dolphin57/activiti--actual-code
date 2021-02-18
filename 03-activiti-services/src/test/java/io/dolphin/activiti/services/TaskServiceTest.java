package io.dolphin.activiti.services;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-17 10:59
 */
public class TaskServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService() {
        TaskService taskService = activitiRule.getTaskService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");

        // 根据key默认使用的是流程最新版本
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = [{}]", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task description = [{}]", task.getDescription());

        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        Map<String, Object> runtimeServiceVariables = runtimeService.getVariables(task.getExecutionId());
        LOGGER.info("taskServiceVariables = [{}]", taskServiceVariables);
        LOGGER.info("taskServiceVariablesLocal = [{}]", taskServiceVariablesLocal);
        LOGGER.info("runtimeServiceVariables = [{}]", runtimeServiceVariables);

        Map<String, Object> compeleVars = Maps.newHashMap();
        compeleVars.put("cKey1", "cValue1");
        taskService.complete(task.getId(), compeleVars);

        Task existTask = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("existTask = [{}]", existTask);
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        TaskService taskService = activitiRule.getTaskService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");

        // 根据key默认使用的是流程最新版本
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = [{}]", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task description = [{}]", task.getDescription());

        taskService.setOwner(task.getId(), "user1");
        //taskService.setAssignee(task.getId(), "dolphin");
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser("dolphin")
                .taskUnassigned()
                .listPage(0, 100);
        for (Task unassignedTask : taskList) {
            try {
                taskService.claim(unassignedTask.getId(), "dolphin");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinkList) {
            LOGGER.info("identityLink = [{}]", identityLink);
        }

        List<Task> dolphinTaskList = taskService.createTaskQuery()
                .taskAssignee("dolphin")
                .listPage(0, 100);
        for (Task dolphinTask : dolphinTaskList) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("ckey1", "cvalue1");
            taskService.complete(dolphinTask.getId(), vars);
        }

        dolphinTaskList = taskService.createTaskQuery()
                .taskAssignee("dolphin")
                .listPage(0, 100);
        LOGGER.info("是否存在 [{}]", CollectionUtils.isEmpty(dolphinTaskList));
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskAttachment() {
        TaskService taskService = activitiRule.getTaskService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");

        // 根据key默认使用的是流程最新版本
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = [{}]", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task description = [{}]", task.getDescription());

        taskService.createAttachment("url",
                task.getId(),
                task.getProcessInstanceId(),
                "name",
                "desc",
                "/url/test.png");

        List<Attachment> attachmentList = taskService.getTaskAttachments(task.getId());
        for (Attachment attachment : attachmentList) {
            LOGGER.info("attachment = [{}]", ToStringBuilder.reflectionToString(attachment, ToStringStyle.JSON_STYLE));
        }
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskComment() {
        TaskService taskService = activitiRule.getTaskService();
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");

        // 根据key默认使用的是流程最新版本
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = [{}]", processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = [{}]", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task description = [{}]", task.getDescription());

        taskService.setOwner(task.getId(), "user1");
        taskService.setAssignee(task.getId(), "dolphin");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 2");

        List<Comment> commentList = taskService.getTaskComments(task.getId());
        for (Comment comment : commentList) {
            LOGGER.info("comment = [{}]", ToStringBuilder.reflectionToString(comment, ToStringStyle.JSON_STYLE));
        }

        // 所有的事件记录都在这里
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            LOGGER.info("taskEvent = [{}]", ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE));
        }
    }
}
