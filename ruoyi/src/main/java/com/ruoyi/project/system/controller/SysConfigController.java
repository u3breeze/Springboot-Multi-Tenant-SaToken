package com.ruoyi.project.system.controller;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysConfig;
import com.ruoyi.project.system.service.ISysConfigService;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

  @Autowired
  private ISysConfigService configService;

  /**
   * 获取参数配置列表
   */
  @SaCheckPermission("system:config:list")
  @GetMapping("/list")
  public TableDataInfo list(SysConfig config) {
//    checkSuperAdmin();
    startPage();
    List<SysConfig> list = configService.selectConfigList(config);
    return getDataTable(list);
  }

  @Log(title = "参数管理", businessType = BusinessType.EXPORT)
  @SaCheckPermission("system:config:export")
  @GetMapping("/export")
  public AjaxResult export(SysConfig config) {
//    checkSuperAdmin();
    List<SysConfig> list = configService.selectConfigList(config);
    ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
    return util.exportExcel(list, "参数数据");
  }

  /**
   * 根据参数编号获取详细信息
   */
  @SaCheckPermission("system:config:query")
  @GetMapping(value = "/{configId}")
  public AjaxResult getInfo(@PathVariable Long configId) {
    return AjaxResult.success(configService.selectConfigById(configId));
  }

  /**
   * 根据参数键名查询参数值
   */
  @GetMapping(value = "/configKey/{configKey}")
  public AjaxResult getConfigKey(@PathVariable String configKey) {
    return AjaxResult.success(configService.selectConfigByKey(configKey));
  }

  /**
   * 新增参数配置
   */
  @SaCheckPermission("system:config:add")
  @Log(title = "参数管理", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@Validated @RequestBody SysConfig config) {
//    checkSuperAdmin();
    if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
      return AjaxResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
    }
    config.setCreateBy(SecurityUtils.getUsername());
    return toAjax(configService.insertConfig(config));
  }

  /**
   * 修改参数配置
   */
  @SaCheckPermission("system:config:edit")
  @Log(title = "参数管理", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@Validated @RequestBody SysConfig config) {
//    checkSuperAdmin();
    if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
      return AjaxResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
    }
    config.setUpdateBy(SecurityUtils.getUsername());
    return toAjax(configService.updateConfig(config));
  }

  /**
   * 删除参数配置
   */
  @SaCheckPermission("system:config:remove")
  @Log(title = "参数管理", businessType = BusinessType.DELETE)
  @DeleteMapping("/{configIds}")
  public AjaxResult remove(@PathVariable Long[] configIds) {
//    checkSuperAdmin();
    return toAjax(configService.deleteConfigByIds(configIds));
  }

  /**
   * 清空缓存
   */
  @SaCheckPermission("system:config:remove")
  @Log(title = "参数管理", businessType = BusinessType.CLEAN)
  @DeleteMapping("/clearCache")
  public AjaxResult clearCache() {
//    checkSuperAdmin();
    configService.clearCache();
    return AjaxResult.success();
  }
}
