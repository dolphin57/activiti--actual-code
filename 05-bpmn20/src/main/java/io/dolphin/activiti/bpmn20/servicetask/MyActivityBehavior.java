package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-4 8:13
 */
public class MyActivityBehavior implements ActivityBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyActivityBehavior.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("my activiti brhavior");
    }
}
