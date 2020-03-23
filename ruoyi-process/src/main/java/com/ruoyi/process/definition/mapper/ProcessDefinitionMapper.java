package com.ruoyi.process.definition.mapper;



import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.domain.TaskAssign;
import com.ruoyi.process.definition.domain.VTasklist;

import java.util.List;

/**
 * 流程用户组Mapper接口
 * 
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public interface ProcessDefinitionMapper
{

    /**
     * 查询流程列表
     * 
     * @param processDefinition
     * @return 流程用户组集合
     */
    public List<ProcessDefinition> selectProcessDefinitionList(ProcessDefinition processDefinition);

    /**
     * 获取配置角色数据的列表
     * @param taskAssign
     * @return
     */
    public List<TaskAssign> selectRoleAssignList(TaskAssign taskAssign);

    /**
     * 获取配置用户数据的列表
     * @param taskAssign
     * @return
     */
    public List<TaskAssign> selectUserAssignList(TaskAssign taskAssign);

    /**
     * 根据角色删除
     * @param taskAssign
     */
    public void deleteAssign(TaskAssign taskAssign);


    /**
     * 根据assign 插入值
     * @param taskAssign
     */
    public void insertAssign(TaskAssign taskAssign);

    /**
     * 根据角色查询
     * @return
     */
    public List<TaskAssign> selectListTaskRoleAssign(TaskAssign taskAssign);


    /**
     * 根据用户查询
     * @param taskAssign
     * @return
     */
    public List<TaskAssign> selectListTaskUserAssign(TaskAssign taskAssign);


    /**
     * 获取待办任务
     * @param vTasklist
     * @return
     */
    public List<VTasklist> selectTaskByUserTodo(VTasklist vTasklist);


    /**
     * 删除角色的同时删除assign
     * @param roleId
     */
    public void  deleteAssignByRoleId(Long roleId);


    /**
     * 删除角色的同时删除assign
     * @param roleId
     */
    public void  deleteAssignByRoles(Long[] roleId);

}
