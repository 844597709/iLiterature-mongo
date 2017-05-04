/**
 * $Id: JsonExceptionResolver.java 5075 2012-10-18 07:11:36Z you.zhou $ Copyright (c) 2012 Qunar.com. All Rights
 * Reserved.
 */
package com.swust.kelab.web.json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

/**
 * 处理IE环境下，文件大小超出限制抛出的异常
 * 
 * @author xiaolong.chang
 * 
 */
public class FileUploadExceptionResolver extends AbstractHandlerMethodExceptionResolver implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadExceptionResolver.class);

    private int order = Ordered.LOWEST_PRECEDENCE;

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    protected boolean shouldApplyTo(HttpServletRequest request, Object handler) {
        // 只处理返回类型为FileUploadView的handler
        if (super.shouldApplyTo(request, handler)) {
            if (handler != null) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (ModelAndView.class.isAssignableFrom(handlerMethod.getMethod().getReturnType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 处理文件上传相关异常，将异常封装成特定格式
     */
    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex) {
        // request无法得到属性，当浏览器版本为chrome，但是又抛出异常时，无法处理
        ModelAndView mv = new ModelAndView("fileUploadView");
        if (ex instanceof MaxUploadSizeExceededException) {
            logger.error("抛出文件大小超出限制异常", ex);
            mv.addObject("result", new JsonAndView().setRet(false).setErrcode(601).setErrmsg("文件大小超出限制，不能大于100MB"));
        }
        return mv;
    }
}
