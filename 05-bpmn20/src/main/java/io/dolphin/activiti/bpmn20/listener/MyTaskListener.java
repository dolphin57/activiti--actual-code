package io.dolphin.activiti.bpmn20.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-1 7:58
 */
public class MyTaskListener implements TaskListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (StringUtils.equals("create", eventName)) {
            LOGGER.info("configure by listener");
            delegateTask.addCandidateGroup("group1");
            delegateTask.addCandidateUsers(Arrays.asList("user1", "user2"));

            //delegateTask.setAssignee("kermit");
            delegateTask.setVariable("key1", "value1");
            // ... setXxx 过期时间
            delegateTask.setDueDate(DateTime.now().plusDays(3).toDate());
        } else if (StringUtils.equals("complete", eventName)) {
            LOGGER.info("task complete");
        }
    }
}
