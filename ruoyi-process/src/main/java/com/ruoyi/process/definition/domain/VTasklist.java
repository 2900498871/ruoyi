package com.ruoyi.process.definition.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**查看当前代办的审批
 * ren 根据v_tasklist
 */

public class VTasklist extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String taskId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String procInstId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String actName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String assignee;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String delegationId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String description;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date dueDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String candidate;

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskId()
    {
        return taskId;
    }
    public void setProcInstId(String procInstId)
    {
        this.procInstId = procInstId;
    }

    public String getProcInstId()
    {
        return procInstId;
    }
    public void setActId(String actId)
    {
        this.actId = actId;
    }

    public String getActId()
    {
        return actId;
    }
    public void setActName(String actName)
    {
        this.actName = actName;
    }

    public String getActName()
    {
        return actName;
    }
    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }

    public String getAssignee()
    {
        return assignee;
    }
    public void setDelegationId(String delegationId)
    {
        this.delegationId = delegationId;
    }

    public String getDelegationId()
    {
        return delegationId;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate()
    {
        return dueDate;
    }
    public void setCandidate(String candidate)
    {
        this.candidate = candidate;
    }

    public String getCandidate()
    {
        return candidate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("procInstId", getProcInstId())
                .append("actId", getActId())
                .append("actName", getActName())
                .append("assignee", getAssignee())
                .append("delegationId", getDelegationId())
                .append("description", getDescription())
                .append("createTime", getCreateTime())
                .append("dueDate", getDueDate())
                .append("candidate", getCandidate())
                .toString();
    }
}
