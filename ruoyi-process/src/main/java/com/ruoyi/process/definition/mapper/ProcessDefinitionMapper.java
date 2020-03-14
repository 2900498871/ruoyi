package com.ruoyi.process.definition.mapper;



import com.ruoyi.process.definition.domain.ProcessDefinition;

import java.util.List;

/**
 * 流程用户组Mapper接口
 * 
 * @author Xianlu Tech
 * @date 2019-10-02
 */
public interface ProcessDefinitionMapper
{

    /**
     * 查询流程列表
     * 
     * @param processDefinition
     * @return 流程用户组集合
     */
    public List<ProcessDefinition> selectProcessDefinitionList(ProcessDefinition processDefinition);

}
