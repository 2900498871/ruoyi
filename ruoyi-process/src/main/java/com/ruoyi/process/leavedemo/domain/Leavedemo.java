package com.ruoyi.process.leavedemo.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 请假demo对象 leavedemo
 *
 * @author ren
 * @date 2020-03-19
 */
public class Leavedemo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 图片 */
    @Excel(name = "图片")
    private String pic;

    /** 请假开始时间 */
    @Excel(name = "请假开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date starttime;

    /** 请假结束时间 */
    @Excel(name = "请假结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endtime;

    /** 流程id */
    @Excel(name = "流程id")
    private String instanceId;

    /** 运行状态 start：初始值 ；run：运行中 ； end ：结束； */
    @Excel(name = "运行状态 start：初始值 ；run：运行中 ； end ：结束；")
    private String runstatus;

    /***********流程配置start******/
    private String userId;

    private String taskId;

    private String procInstId;

    private String actId;

    private String actName;

    /**********流程设置结束*********/

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getPic()
    {
        return pic;
    }
    public void setStarttime(Date starttime)
    {
        this.starttime = starttime;
    }

    public Date getStarttime()
    {
        return starttime;
    }
    public void setEndtime(Date endtime)
    {
        this.endtime = endtime;
    }

    public Date getEndtime()
    {
        return endtime;
    }
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId()
    {
        return instanceId;
    }
    public void setRunstatus(String runstatus)
    {
        this.runstatus = runstatus;
    }

    public String getRunstatus()
    {
        return runstatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("remark", getRemark())
                .append("pic", getPic())
                .append("starttime", getStarttime())
                .append("endtime", getEndtime())
                .append("instanceId", getInstanceId())
                .append("runstatus", getRunstatus())
                .toString();
    }
}
