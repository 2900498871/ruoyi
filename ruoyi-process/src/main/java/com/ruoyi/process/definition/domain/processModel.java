package com.ruoyi.process.definition.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * author:ren
 */
public class processModel extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;

    @Excel(name = "流程名称")
    private String name;

    @Excel(name = "流程KEY")
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
