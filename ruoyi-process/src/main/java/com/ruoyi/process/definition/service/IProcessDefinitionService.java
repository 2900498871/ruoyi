package com.ruoyi.process.definition.service;




import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.domain.TaskAssign;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public HashMap startProcess(String id, String defKey, Map<String, Object> var);

    public void caim(String taskid,String user_id);

    public void completeTask(String taskid, Map<String,Object> var);

    public void completeTask(String taskid,String insid, String comment, Map<String,Object> var);

}
