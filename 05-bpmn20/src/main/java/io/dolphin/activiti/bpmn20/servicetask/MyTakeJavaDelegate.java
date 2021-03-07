package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-4 8:11
 */
public class MyTakeJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTakeJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("my java delegate [{}]", this);
    }
}
