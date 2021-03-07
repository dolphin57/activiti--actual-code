package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-4 8:11
 */
public class MyPayJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPayJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("variables = [{}]", execution.getVariables());
        LOGGER.info("my java delegate [{}]", this);

        execution.getParent().setVariableLocal("key2", "value2");
        execution.setVariable("key1", "value1");
        execution.setVariable("key3", "value3");

        Object errorFlag = execution.getVariable("errorFlag");
        if (Objects.equals(errorFlag, true)) {
            throw new BpmnError("bpmnError");
        }
    }
}
