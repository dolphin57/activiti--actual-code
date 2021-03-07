package io.dolphin.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
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
 * @Since: 2021-3-1 8:04
 */
public class ServiceTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-servicetask1.bpmn20.xml"})
    public void testServiceTask() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HistoryService historyService = activitiRule.getHistoryService();

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        // 查询执行过程
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            LOGGER.info("activity = [{}]", historicActivityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testServiceTask2() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HistoryService historyService = activitiRule.getHistoryService();
        ManagementService managementService = activitiRule.getManagementService();

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        // 查询执行过程
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            LOGGER.info("activity = [{}]", historicActivityInstance);
        }

        Execution execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = [{}]", execution);

        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution, false);
                return null;
            }
        });

        historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            LOGGER.info("activity = [{}]", historicActivityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc", "the Java Delegate");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

    }
}
