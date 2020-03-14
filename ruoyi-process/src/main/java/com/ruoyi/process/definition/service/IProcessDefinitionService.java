package com.ruoyi.process.definition.service;




import com.ruoyi.process.definition.domain.ProcessDefinition;

import java.util.List;

/**
 * ren
 */
public interface IProcessDefinitionService {

    public List<ProcessDefinition> selectProcessDefinitionList(ProcessDefinition processDefinition);

}
