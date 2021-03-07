package io.dolphin.activiti.bpmn20;

import com.google.common.collect.Maps;
import io.dolphin.activiti.bpmn20.servicetask.MyJavaBean;
import io.dolphin.activiti.bpmn20.servicetask.MyJavaDelegate;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-1 8:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-context.xml")
public class ServiceTaskSpringTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Rule
    @Resource
    public ActivitiRule activitiRule;

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
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
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask2() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        LOGGER.info("myJavaDelegate = [{}]", myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
    }

    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask3() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean();
        LOGGER.info("myJavaBean = [{}]", myJavaBean);
        variables.put("myJavaBean", myJavaBean);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

    }
}
