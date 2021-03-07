package io.dolphin.activiti.bpmn20.servicetask;

import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-3-5 8:15
 */
public class MyJavaBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaBean.class);

    private String name;

    public String getName() {
        LOGGER.info("run getName name = [{}]", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sayHello() {
        LOGGER.info("say Hello");
    }
}
