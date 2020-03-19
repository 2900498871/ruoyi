package com.ruoyi.process.definition.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户和角色的java bean
 * ren
 */
public class TaskAssign  extends BaseEntity{
    private static final long serialVersionUID = 1L;
    @Excel(name = "用户/角色名称")
    private String name;



    @Excel(name = "流程任务节点id")
    private String usertask;

    @Excel(name = "角色或用户id")
    private String assignId;

    @Excel(name = "角色或用户类型")
    private String datatype;

    @Excel(name = "流程定义key")
    private String processkey;

    @Excel(name = "是否分配")
    private String isassign;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsertask() {
        return usertask;
    }

    public void setUsertask(String usertask) {
        this.usertask = usertask;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }



    public String getIsassign() {
        return isassign;
    }

    public void setIsassign(String isassign) {
        this.isassign = isassign;
    }

    public String getProcesskey() {
        return processkey;
    }

    public void setProcesskey(String processkey) {
        this.processkey = processkey;
    }

    public TaskAssign(String usertask, String processkey) {
        this.usertask = usertask;
        this.processkey = processkey;
    }
}
