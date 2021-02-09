package io.dolphin.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-9 14:33
 */
public class DolphinBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DolphinBean.class);

    public void sayDolphin() {
        LOGGER.info("say dolphin");
    }
}
