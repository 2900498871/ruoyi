package com.ruoyi.process.definition.service;




import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.domain.TaskAssign;


import java.util.List;

/**
 * ren
 */
public interface IProcessDefinitionService {

    public List<ProcessDefinition> selectProcessDefinitionList(ProcessDefinition processDefinition);

    public List<TaskAssign> selectRoleAssignList(TaskAssign taskAssign);

    public List<TaskAssign> selectUserAssignList(TaskAssign taskAssign);

    public void deleteAssign(TaskAssign taskAssign);

    public void insertAssign(TaskAssign taskAssign);

    public List<TaskAssign> selectListTaskRoleAssign(TaskAssign taskAssign);

    public List<TaskAssign> selectListTaskUserAssign(TaskAssign taskAssign);

}
