package com.ruoyi.process.leavedemo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.definition.service.IProcessDefinitionService;
import com.ruoyi.process.leavedemo.domain.Leavedemo;
import com.ruoyi.process.leavedemo.service.ILeavedemoService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.TaskService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 请假demoController
 * 
 * @author ren
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/leavedemo/leavedemo")
public class LeavedemoController extends BaseController
{
    private String prefix = "process/leavedemo";

    @Autowired
    private ILeavedemoService leavedemoService;

    @Autowired
    private IProcessDefinitionService iProcessDefinitionService;

    @Autowired
    TaskService taskService;

    @RequiresPermissions("leavedemo:leavedemo:view")
    @GetMapping()
    public String leavedemo()
    {
        return prefix + "/leavedemo";
    }



    /**
     * 查询请假demo列表
     */
    @RequiresPermissions("leavedemo:leavedemo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Leavedemo leavedemo)
    {
        startPage();
        List<Leavedemo> list = leavedemoService.selectLeavedemoList(leavedemo);
        return getDataTable(list);
    }



    /**
     * 导出请假demo列表
     */
    @RequiresPermissions("leavedemo:leavedemo:export")
    @Log(title = "请假demo", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Leavedemo leavedemo)
    {
        List<Leavedemo> list = leavedemoService.selectLeavedemoList(leavedemo);
        ExcelUtil<Leavedemo> util = new ExcelUtil<Leavedemo>(Leavedemo.class);
        return util.exportExcel(list, "leavedemo");
    }

    /**
     * 新增请假demo
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存请假demo
     */
    @RequiresPermissions("leavedemo:leavedemo:add")
    @Log(title = "请假demo", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Leavedemo leavedemo)
    {
        leavedemo.setId(System.currentTimeMillis()+"");
        return toAjax(leavedemoService.insertLeavedemo(leavedemo));
    }

    /**
     * 修改请假demo
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        Leavedemo leavedemo = leavedemoService.selectLeavedemoById(id);
        mmap.put("leavedemo", leavedemo);
        return prefix + "/edit";
    }

    /**
     * 修改保存请假demo
     */
    @RequiresPermissions("leavedemo:leavedemo:edit")
    @Log(title = "请假demo", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Leavedemo leavedemo)
    {
        return toAjax(leavedemoService.updateLeavedemo(leavedemo));
    }

    /**
     * 删除请假demo
     */
    @RequiresPermissions("leavedemo:leavedemo:remove")
    @Log(title = "请假demo", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(leavedemoService.deleteLeavedemoByIds(ids));
    }


    /***********************************处理流程待办********************************************/
    @RequiresPermissions("leavedemo:leavedemo:apply")
    @PostMapping( "/apply/{id}")
    @ResponseBody
    public AjaxResult apply(@PathVariable("id")String id)
    {
        //获取当前登陆用户
        SysUser user = ShiroUtils.getSysUser();
        HashMap map=leavedemoService.apply(id, user.getUserId().toString());
        if(map.get("code").equals("200")){//部署成功
            return AjaxResult.success(map.get("msg").toString());
        }else{//部署失败
            return AjaxResult.error(map.get("msg").toString());
        }

    }


    /**
     * 跳转到任务处理页面
     * @return
     */
    @RequiresPermissions("leavedemo:leavedemo:waitview")
    @GetMapping("/waitview")
    public String leavedemoWait()
    {
        return prefix + "/leavedemoWaitTodo";
    }

    /**
     * 查询请假demo列表待办
     */
    @PostMapping("/waitTodolist")
    @ResponseBody
    public TableDataInfo waitTodolist(Leavedemo leavedemo)
    {
        startPage();
        //获取当前用户
        SysUser user = ShiroUtils.getSysUser();
        leavedemo.setUserId(user.getUserId().toString());
        List<Leavedemo> list = leavedemoService.selectLeavedemoWaitTodo(leavedemo);
        return getDataTable(list);
    }

    /**
     * 修改请假demo审批页面
     */
    @GetMapping("/caim/{id}/{taskId}")
    public String caimEdit(@PathVariable("id") String id,@PathVariable("taskId") String taskId, ModelMap mmap)
    {
        Leavedemo leavedemo = leavedemoService.selectLeavedemoById(id);
        mmap.put("leavedemo", leavedemo);
        mmap.put("taskId",taskId);
        return prefix + "/caimEdit";
    }


    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId,
                               HttpServletRequest request) {
                Map<String, Object> var = new HashMap<String, Object>();
                //获取当前用户
                SysUser user = ShiroUtils.getSysUser();
                Leavedemo leavedemo =new Leavedemo();
                leavedemo.setUserId(user.getUserId().toString());
                leavedemo.setTaskId(taskId);
                leavedemo=leavedemoService.selectLeavedemoWaitTodoByTaskId(leavedemo);
                String instanceId=leavedemo.getInstanceId();
                String defkey=leavedemo.getActId();
        try{
            //获取leavedemo 结果
            String pass = request.getParameter("pass");//获取审批结果
            String comment = request.getParameter("comment");//获取审批内容
            String user_id=user.getUserId().toString();
            var.put("user_id",user_id);
            var.put("comment",comment);
            if(pass.equals("1")){//同意
                var.put("pass",1);
            }else{//驳回
                var.put("pass",0);
            }
            //签收流程
            try{
                iProcessDefinitionService.caim(taskId,instanceId);
                //办理流程
                iProcessDefinitionService.completeTask(taskId,instanceId,comment,var);
            }catch (ActivitiTaskAlreadyClaimedException e){//已经签收过了
                //办理流程
                iProcessDefinitionService.completeTask(taskId,instanceId,comment,var);
            }
            if(defkey.equals("endTask")){//结束任务
                if(pass.equals("1")){//通过
                    leavedemo.setRunstatus("end");
                    leavedemoService.updateLeavedemo(leavedemo);
                }
            }
            return success("任务已完成");
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{instanceId, var, e});
            return error("完成任务失败");
        }
    }


    /*********************************流程处理结束*****************************/



}
