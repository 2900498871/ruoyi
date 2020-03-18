package com.ruoyi.process.definition.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.domain.TaskAssign;
import com.ruoyi.process.definition.mapper.ProcessDefinitionMapper;
import com.ruoyi.process.definition.service.IProcessDefinitionService;
import com.ruoyi.process.general.mapper.ProcessMapper;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class ProcessDefinitionService implements IProcessDefinitionService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

	@Transactional
    public void startProcess(String assignee) {
        // 从系统用户表中查询用户信息
        SysUser sysUser = userMapper.selectUserByLoginName(assignee);
        Map<String, Object> variables = new HashMap<>();
        variables.put("sysUser", sysUser);
        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);
//        runtimeService.startProcessInstanceByKey("oneTaskProcess");
    }

    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    /**
     * 根据流程id 获取ModelerId
     * @param id
     * @return
     */
    public String getModelerById(String id){
         List<String> list= processMapper.selectModelIdfromId(id);
         if(list!=null){
             return list.get(0);
         }else{
             return "";
         }

    }

    /**
     * 分页查询流程定义文件
     * @return
     */
    public List<ProcessDefinition> selectProcessDefinitionList(com.ruoyi.process.definition.domain.ProcessDefinition processDefinition) {
        return processDefinitionMapper.selectProcessDefinitionList(processDefinition);
    }

    public List<TaskAssign> selectRoleAssignList(TaskAssign taskAssign){
        return processDefinitionMapper.selectRoleAssignList(taskAssign);
    }

    public List<TaskAssign> selectUserAssignList(TaskAssign taskAssign){
        return processDefinitionMapper.selectUserAssignList(taskAssign);
    }

    /**
     * 根据用户或角色
     * @param taskAssign
     */
    public void deleteAssign(TaskAssign taskAssign){
        processDefinitionMapper.deleteAssign(taskAssign);
    }

    /**
     * 插入值
     * @param taskAssign
     */
    public void insertAssign(TaskAssign taskAssign){
      processDefinitionMapper.insertAssign(taskAssign);
    }

    @Transactional
    public void deployProcessDefinition(String filePath) throws FileNotFoundException {
        if (StringUtils.isNotBlank(filePath)) {
            if (filePath.endsWith(".zip")) {
                ZipInputStream inputStream = new ZipInputStream(new FileInputStream(filePath));
                repositoryService.createDeployment()
                        .addZipInputStream(inputStream)
                        .deploy();
            } else if (filePath.endsWith(".bpmn")) {
                repositoryService.createDeployment()
                        .addInputStream(filePath, new FileInputStream(filePath))
                        .deploy();
            }
        }
    }

    @Transactional
    public int deleteProcessDeploymentByIds(String deploymentIds) {
        String[] deploymentIdsArr = Convert.toStrArray(deploymentIds);
        int counter = 0;
        for (String deploymentId: deploymentIdsArr) {
            List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery()
                    .deploymentId(deploymentId)
                    .list();
            if (!CollectionUtils.isEmpty(instanceList)) continue;   // 跳过存在流程实例的流程定义
            repositoryService.deleteDeployment(deploymentId, true); // true 表示级联删除引用，比如 act_ru_execution 数据
            counter++;
        }
        return counter;
    }

}
