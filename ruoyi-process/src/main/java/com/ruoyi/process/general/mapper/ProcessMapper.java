package com.ruoyi.process.general.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProcessMapper {

    @Select("select user_id_ from act_id_membership where group_id_ = (select group_id_ from act_ru_identitylink where task_id_ = #{taskId})")
    List<String> selectTodoUserListByTaskId(@Param(value = "taskId") String taskId);


    @Select("select md.ID_ ID from (\n" +
            "select def.*,dep.DEPLOY_TIME_ from ACT_RE_PROCDEF def ,ACT_RE_DEPLOYMENT dep \n" +
            "where def.DEPLOYMENT_ID_=dep.ID_ ORDER BY VERSION_ DESC) def left join act_re_model md on def.KEY_=md.KEY_ \n" +
            "where def.ID_=#{ID}")
    List<String> selectModelIdfromId(@Param(value = "ID") String ID);
}
