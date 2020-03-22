package com.ruoyi.process.leavedemo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.process.definition.service.impl.ProcessDefinitionService;
import com.ruoyi.process.leavedemo.domain.Leavedemo;
import com.ruoyi.process.leavedemo.mapper.LeavedemoMapper;
import com.ruoyi.process.leavedemo.service.ILeavedemoService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;

/**
 * 请假demoService业务层处理
 * 
 * @author ren
 * @date 2020-03-19
 */
@Service
public class LeavedemoServiceImpl implements ILeavedemoService
{

    /**
     * 请假的流程key
     */
    public static final String processKey="QJLC";


    /**
     * 请假的流程name
     */
    public static final String processName="请假流程";

    @Autowired
    private LeavedemoMapper leavedemoMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * 查询请假demo
     * 
     * @param id 请假demoID
     * @return 请假demo
     */
    @Override
    public Leavedemo selectLeavedemoById(String id)
    {
        return leavedemoMapper.selectLeavedemoById(id);
    }

    /**
     * 查询请假demo列表
     * 
     * @param leavedemo 请假demo
     * @return 请假demo
     */
    @Override
    public List<Leavedemo> selectLeavedemoList(Leavedemo leavedemo)
    {
        return leavedemoMapper.selectLeavedemoList(leavedemo);
    }

    /**
     * 查询待办
     * @param leavedemo
     * @return
     */
    public List<Leavedemo> selectLeavedemoWaitTodo(Leavedemo leavedemo){

        return leavedemoMapper.selectLeavedemoWaitTodo(leavedemo);
    }

    public Leavedemo selectLeavedemoWaitTodoByTaskId(Leavedemo leavedemo){
        return leavedemoMapper.selectLeavedemoWaitTodoByTaskId(leavedemo);
    }

    /**
     * 新增请假demo
     * 
     * @param leavedemo 请假demo
     * @return 结果
     */
    @Override
    public int insertLeavedemo(Leavedemo leavedemo)
    {
        return leavedemoMapper.insertLeavedemo(leavedemo);
    }

    /**
     * 修改请假demo
     * 
     * @param leavedemo 请假demo
     * @return 结果
     */
    @Override
    public int updateLeavedemo(Leavedemo leavedemo)
    {
        return leavedemoMapper.updateLeavedemo(leavedemo);
    }

    /**
     * 删除请假demo对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLeavedemoByIds(String ids)
    {
        return leavedemoMapper.deleteLeavedemoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除请假demo信息
     * 
     * @param id 请假demoID
     * @return 结果
     */
    @Override
    public int deleteLeavedemoById(String id)
    {
        return leavedemoMapper.deleteLeavedemoById(id);
    }

    /**
     * 启动流程
     * @param id
     * @return
     */
    public HashMap apply(String id,String user_id){
        Map<String,Object> var = new HashMap<>();
        HashMap dataMap=processDefinitionService.startProcess(id,processKey,var);
        if(dataMap.get("code").equals("200")){//成功
            //更新请假任务
            String instenceId=(String)dataMap.get("procInsId");
            Leavedemo leavedemo=leavedemoMapper.selectLeavedemoById(id);
            leavedemo.setInstanceId(instenceId);
            leavedemo.setRunstatus("run");
            leavedemoMapper.updateLeavedemo(leavedemo);

            //由提交人完成第一个任务
            Task startTask = taskService.createTaskQuery().processInstanceId(instenceId).singleResult();
            var.put("user_id",user_id);
            var.put("action","提交申请");
            var.put("title",processName);

            //分配任务
            taskService.setAssignee(startTask.getId(),String.valueOf(user_id));
            //签收任务
            taskService.claim(startTask.getId(),String.valueOf(user_id));
            //完成任务
            processDefinitionService.completeTask(startTask.getId(),var);

        }
        return dataMap;
    }
}
