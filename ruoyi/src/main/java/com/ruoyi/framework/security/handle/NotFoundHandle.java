package com.ruoyi.framework.security.handle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.util.SaResult;

/**
 * 处理 404  
 * @author kong 
 */
@RestController
public class NotFoundHandle implements ErrorController {

    @SaIgnore
	@RequestMapping("/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(200);
        return SaResult.get(404, "not found", null);
    }
	
}
