package io.dolphin.activiti.dbentity;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-19 7:55
 */
public class DbConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigTest.class);

    @Test
    public void testDbConfig() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        // 获取每个表的数据量
        Map<String, Long> tableCount = managementService.getTableCount();
        // 取出对应的key
        List<String> tableNameList = Lists.newArrayList(tableCount.keySet());
        Collections.sort(tableNameList);
        for (String tableName : tableNameList) {
            LOGGER.info("tableName = [{}]", tableName);
        }
        LOGGER.info("tableName size = [{}]", tableNameList.size());
    }

    /**
     * 清理表结构
     */
    @Test
    public void testDropTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                LOGGER.info("删除表结构");
                return null;
            }
        });
    }
}
