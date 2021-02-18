package io.dolphin.activiti.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2021-2-18 7:37
 */
public interface MyCustomMapper {
    @Select("SELECT * FROM ACT_RU_TASK")
    public List<Map<String, Object>> findAll();
}
