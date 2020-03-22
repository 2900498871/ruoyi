package com.ruoyi.process.leavedemo.mapper;

import java.util.List;
import com.ruoyi.process.leavedemo.domain.Leavedemo;

/**
 * 请假demoMapper接口
 * 
 * @author ren
 * @date 2020-03-19
 */
public interface LeavedemoMapper 
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
     * 删除请假demo
     * 
     * @param id 请假demoID
     * @return 结果
     */
    public int deleteLeavedemoById(String id);

    /**
     * 批量删除请假demo
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLeavedemoByIds(String[] ids);

    /**
     * 获取leaveDome的待办
     * @param leavedemo
     * @return
     */
    public List<Leavedemo> selectLeavedemoWaitTodo(Leavedemo leavedemo);

    /**
     * 获取leaveDome的待办
     * @param leavedemo
     * @return
     */
    public Leavedemo selectLeavedemoWaitTodoByTaskId(Leavedemo leavedemo);

}
