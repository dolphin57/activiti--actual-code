package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-4 8:11
 */
public class MyJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {
        if (name != null) {
            Object value = name.getValue(execution);
            LOGGER.info("name = [{}]", value);
        }

        if (desc != null) {
            Object value = desc.getValue(execution);
            LOGGER.info("desc = [{}]", value);
        }
        LOGGER.info("my java delegate [{}]", this);
    }
}
