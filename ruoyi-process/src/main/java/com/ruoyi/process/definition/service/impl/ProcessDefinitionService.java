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
import org.activiti.engine.*;
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

    @Autowired
    private ProcessEngine processEngine;

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
     * 监听使用
     * @param taskAssign
     * @return
     */
    public List<TaskAssign> selectListTaskRoleAssign(TaskAssign taskAssign){
        return processDefinitionMapper.selectListTaskRoleAssign(taskAssign);
    }

    public List<TaskAssign> selectListTaskUserAssign(TaskAssign taskAssign){
        return processDefinitionMapper.selectListTaskUserAssign(taskAssign);
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

    /***
     * 启动流程(详细补充)
     */
    @Transactional
    public HashMap startProcess(String id, String defKey, Map<String, Object> var){
        HashMap datamap=new HashMap();
        try {
            if(var==null){
                var = new HashMap<String, Object>();
            }
            org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(defKey).latestVersion().singleResult();
            if (processDefinition==null){
                datamap.put("code","500");
                datamap.put("msg","流程未部署！请先部署流程！");
                return datamap;
            }else{
                if (!processDefinition.isSuspended()){
                    ProcessInstance procIns = runtimeService.startProcessInstanceByKey(defKey,id,var);
                    datamap.put("code","200");
                    datamap.put("msg","流程启动成功");
                    datamap.put("procInsId",procIns.getId());
                    return datamap;
                }else{
                    datamap.put("code","500");
                    datamap.put("msg","流程启动失败");
                    return datamap;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            datamap.put("code","500");
            datamap.put("msg","流程部署失败");
            return datamap;
        }
    }

    /**
     * 签收流程
     * @param taskid
     * @param user_id
     */
    @Transactional
    public void caim(String taskid,String user_id){
        taskService.claim(taskid,user_id);
    }


    /***
     * 完成任务(补充)
     */
    @Transactional
    public void completeTask(String taskid, Map<String,Object> var){
        if(var!=null){
            var = new HashMap<String,Object>();
        }
        processEngine.getTaskService().complete(taskid, var);
    }

    /**
     * 完成任务补充
     * @param taskid
     * @param insid
     * @param comment
     * @param var
     */
    @Transactional
    public void completeTask(String taskid,String insid, String comment, Map<String,Object> var){
        TaskService service = processEngine.getTaskService();
        if(!StringUtils.isEmpty(insid)&&!StringUtils.isEmpty(comment)){
            service.addComment(taskid, insid, comment);
        }
        if(var==null){
            var = new HashMap<String,Object>();
        }
        service.complete(taskid, var);
    }





}
