package com.ruoyi.process.leavedemo.service;

import com.ruoyi.process.leavedemo.domain.Leavedemo;

import java.util.HashMap;
import java.util.List;


/**
 * 请假demoService接口
 * 
 * @author ren
 * @date 2020-03-19
 */
public interface ILeavedemoService 
{
    /**
     * 查询请假demo
     * 
     * @param id 请假demoID
     * @return 请假demo
     */
    public Leavedemo selectLeavedemoById(String id);

    /**
     * 查询请假demo列表
     * 
     * @param leavedemo 请假demo
     * @return 请假demo集合
     */
    public List<Leavedemo> selectLeavedemoList(Leavedemo leavedemo);

    /**
     * 新增请假demo
     * 
     * @param leavedemo 请假demo
     * @return 结果
     */
    public int insertLeavedemo(Leavedemo leavedemo);

    /**
     * 修改请假demo
     * 
     * @param leavedemo 请假demo
     * @return 结果
     */
    public int updateLeavedemo(Leavedemo leavedemo);

    /**
     * 批量删除请假demo
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLeavedemoByIds(String ids);

    /**
     * 删除请假demo信息
     * 
     * @param id 请假demoID
     * @return 结果
     */
    public int deleteLeavedemoById(String id);

    /**
     * 启动流程
     * @param id
     * @param user_id
     * @return
     */
    public HashMap apply(String id,String user_id);

    /**
     * 请假待办
     * @param leavedemo
     * @return
     */
    public List<Leavedemo> selectLeavedemoWaitTodo(Leavedemo leavedemo);

    /**
     * 根据taskId 获取待办
     * @param leavedemo
     * @return
     */
    public Leavedemo selectLeavedemoWaitTodoByTaskId(Leavedemo leavedemo);

}
