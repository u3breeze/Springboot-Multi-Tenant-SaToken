package com.ruoyi.framework.web.controller;

import com.github.pagehelper.page.PageMethod;
import com.ruoyi.common.domain.ActionResult;
import com.ruoyi.common.domain.ResultCode;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageMethod.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

//    protected void checkSuperAdmin() {
//        if (!SecurityUtils.isSuperAdmin()) {
//            throw new CustomException("无权访问:" + this.getClass().getName());
//        }
//    }
//
//    protected void checkComAdmin() {
//        if (!SecurityUtils.isComAdmin()) {
//            throw new CustomException("无权访问:" + this.getClass().getName());
//        }
//    }

    protected String getComId() {
        return SecurityUtils.getCurrComId();
    }

    protected Long getUserId() {
        return SecurityUtils.getCurrUserId();
    }


    public <T> ActionResult<T> actionResult(ResultCode code, T value) {
        return new ActionResult<T>(code.getCode(),
                code.getDesc(),
                value);
    }

    public <T> ActionResult<T> actionResult(T value) {
        ResultCode code = ResultCode.SUCCESS;
        return actionResult(code, value);
    }

}
