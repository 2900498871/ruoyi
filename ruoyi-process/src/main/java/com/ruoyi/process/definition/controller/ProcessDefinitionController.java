package com.ruoyi.process.definition.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.process.definition.domain.ProcessDefinition;
import com.ruoyi.process.definition.service.impl.ProcessDefinitionService;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/process/definition")
public class ProcessDefinitionController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionController.class);

    private String prefix = "process/definition";

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @Autowired
    private RepositoryService repositoryService;


    @RequiresPermissions("process:definition:view")
    @GetMapping
    public String processDefinition() {
        return prefix + "/definition";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProcessDefinition processDefinition) {
        startPage();
        List<ProcessDefinition> list = processDefinitionService.selectProcessDefinitionList(processDefinition);
        return getDataTable(list);
    }

    /**
     * 跳转导设置流程的页面
     * @param processId
     * @param mmap
     * @return
     */
    @RequiresPermissions("process:definition:processView")
    @GetMapping("/processView/{processId}")
    public String modelList(@PathVariable("processId") String processId, ModelMap mmap) {
        //把流程id 存起来
        mmap.put("processId",processId);
        mmap.put("path","../../../process/definition/exportXmlOrImage?type=image&processId="+processId);
        return "process/definition/processView";
    }

    /**
     * 部署流程定义
     */
    @RequiresPermissions("process:definition:upload")
    @Log(title = "流程定义", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("processDefinition") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
                if (!"bpmn".equalsIgnoreCase(extensionName)
                        && !"zip".equalsIgnoreCase(extensionName)
                        && !"bar".equalsIgnoreCase(extensionName)) {
                    return error("流程定义文件仅支持 bpmn, zip 和 bar 格式！");
                }
                // p.s. 此时 FileUploadUtils.upload() 返回字符串 fileName 前缀为 Constants.RESOURCE_PREFIX，需剔除
                // 详见: FileUploadUtils.getPathFileName(...)
                String fileName = FileUploadUtils.upload(Global.getProfile() + "/processDefiniton", file);
                if (StringUtils.isNotBlank(fileName)) {
                    String realFilePath = Global.getProfile() + fileName.substring(Constants.RESOURCE_PREFIX.length());
                    processDefinitionService.deployProcessDefinition(realFilePath);
                    return success();
                }
            }
            return error("不允许上传空文件！");
        }
        catch (Exception e) {
            log.error("上传流程定义文件失败！", e);
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("process:definition:remove")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(processDefinitionService.deleteProcessDeploymentByIds(ids));
        }
        catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @Log(title = "流程定义", businessType = BusinessType.EXPORT)
    @RequiresPermissions("process:definition:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export() {
        List<ProcessDefinition> list = processDefinitionService.selectProcessDefinitionList(new ProcessDefinition());
        ExcelUtil<ProcessDefinition> util = new ExcelUtil<>(ProcessDefinition.class);
        return util.exportExcel(list, "流程定义数据");
    }

    /***
     * 挂起/激活
     */
    @Log(title = "流程挂起/激活", businessType = BusinessType.OTHER)
    @RequiresPermissions("process:definition:processup")
    @GetMapping("/processUp/{instanceId}/{active}")
    @ResponseBody
    public AjaxResult updateState(@PathVariable("instanceId") String procDefId,@PathVariable("active") String active){
        if (active.equals("1")) {//挂起
            repositoryService.suspendProcessDefinitionById(procDefId, true, null);
        } else  {//激活
            repositoryService.activateProcessDefinitionById(procDefId, true, null);
        }
        return success("操作成功");
    }


    /**
     * 导出model的xml文件或者image 文件
     */
    @RequestMapping(value = "/exportXmlOrImage")
    public void exportXmlOrImage(@RequestParam("processId") String processId,@RequestParam("type") String type, HttpServletResponse response) {
        try {
            //根据流程实例id获取流程定义
            org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processId).singleResult();
            String resourceNmae = "";
            if (type.equals("image")){
                resourceNmae = processDefinition.getDiagramResourceName();
                InputStream resourceAsStream = null;
                resourceAsStream =repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),resourceNmae);
                byte[] b = new byte[1024];
                int len = -1;
                while ((len=resourceAsStream.read(b,0,1024))!=-1){
                    response.getOutputStream().write(b,0,len);
                }
            }else if(type.equals("xml")){//导出bpm
                resourceNmae = processDefinition.getResourceName();
                //根据processId 获取modelId
                String  modelId=processDefinitionService.getModelerById(processId);
                Model modelData = repositoryService.getModel(modelId);
                BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
                JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
                BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
                // 流程非空判断
                if (!CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                    BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                    byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

                    ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
                    String filename = bpmnModel.getMainProcess().getId() + ".bpmn";
                    response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                    IOUtils.copy(in, response.getOutputStream());
                    response.flushBuffer();
                }
            }

        } catch (Exception e) {
            try {
                response.sendRedirect("process/definition/definition");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }




}
