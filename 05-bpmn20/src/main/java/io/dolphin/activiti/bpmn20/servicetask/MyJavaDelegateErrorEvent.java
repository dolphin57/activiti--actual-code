package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Objects;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-5 8:23
 */
public class MyJavaDelegateErrorEvent implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        if (Objects.equals(execution.getVariable("result"), false)) {
            throw new BpmnError("bpmnError");
        }
    }
}
