package io.dolphin.activiti.services;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
 * @Since: 2021-2-17 16:53
 */
public class FormServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-form.bpmn20.xml"})
    public void testFormService() {
        FormService formService = activitiRule.getFormService();
        TaskService taskService = activitiRule.getTaskService();

        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        LOGGER.info("startFormKey = [{}]", startFormKey);

        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            LOGGER.info("formProperty = [{}]", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }

        // 启动流程
        Map<String, String> properties = Maps.newHashMap();
        properties.put("message", "my test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);

        Task task = taskService.createTaskQuery().singleResult();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> taskFormProperties = taskFormData.getFormProperties();
        for (FormProperty taskFormProperty : taskFormProperties) {
            LOGGER.info("taskFormProperty = [{}]", ToStringBuilder.reflectionToString(taskFormProperty, ToStringStyle.JSON_STYLE));
        }

        // 继续执行流程
        Map<String, String> yesOrNoProperties = Maps.newHashMap();
        yesOrNoProperties.put("yesOrNo", "yes");
        formService.submitTaskFormData(task.getId(), yesOrNoProperties);

        // 查询
        Task existTask = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("existTask = [{}]", existTask);
    }
}
