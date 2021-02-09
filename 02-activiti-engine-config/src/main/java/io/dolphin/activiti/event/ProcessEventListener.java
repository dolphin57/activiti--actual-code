package io.dolphin.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-6 18:22
 */
public class ProcessEventListener implements ActivitiEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        if (ActivitiEventType.PROCESS_STARTED.equals(eventType)) {
            LOGGER.info("流程启动 [{}] \t [{}]", eventType, event.getProcessDefinitionId());
        } else if (ActivitiEventType.PROCESS_COMPLETED.equals(eventType)) {
            LOGGER.info("流程结束 [{}] \t [{}]", eventType, event.getProcessDefinitionId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
