package io.dolphin.activiti.services;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-17 16:53
 */
public class IdentityServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testIdentityService() {
        IdentityService identityService = activitiRule.getIdentityService();

        User user1 = identityService.newUser("user1");
        user1.setEmail("user1@126.com");
        identityService.saveUser(user1);
        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@126.com");
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");
        identityService.saveGroup(group2);

        identityService.createMembership("user1", "group1");
        identityService.createMembership("user2", "group1");
        identityService.createMembership("user1", "group2");

        User user11 = identityService.createUserQuery()
                .userId("user1")
                .singleResult();
        user11.setLastName("dolphin");
        identityService.saveUser(user11);

        // 通过group1获取组下的用户
        List<User> userList = identityService.createUserQuery()
                .memberOfGroup("group1")
                .listPage(0, 100);
        for (User user : userList) {
            LOGGER.info("user = [{}]", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        // 通过user1获取所在的组
        List<Group> groupList = identityService.createGroupQuery()
                .groupMember("user1")
                .listPage(0, 100);
        for (Group group : groupList) {
            LOGGER.info("group = [{}]", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }
    }
}
