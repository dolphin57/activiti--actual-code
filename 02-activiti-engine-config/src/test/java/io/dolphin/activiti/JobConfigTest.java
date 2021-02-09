package io.dolphin.activiti;

import org.activiti.engine.runtime.Job;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-1-30 17:59
 */
public class JobConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        LOGGER.info("start");

        List<Job> jobList = activitiRule.getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobList) {
            LOGGER.info("定时任务 = [{}], 默认重试次数 = [{}]", job, job.getRetries());
        }
        LOGGER.info("jobList.size = [{}]", jobList.size());

        Thread.sleep(100 * 1000);

        LOGGER.info("end");
    }
}
