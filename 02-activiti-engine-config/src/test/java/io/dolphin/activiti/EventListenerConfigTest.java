package io.dolphin.activiti;

import io.dolphin.activiti.event.CustomEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
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
 * @Since: 2021-1-30 17:59
 */
public class EventListenerConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventListener.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();

        // 继续执行流程
        activitiRule.getTaskService().complete(task.getId());

        // 添加监听器
        activitiRule.getRuntimeService().addEventListener(new CustomEventListener());

        // 发布自定义事件
        activitiRule.getRuntimeService()
                .dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
    }
}
