package com.swust.kelab.web.json;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 处理IE下文件上传的返回视图 jquery.form.js特定的json格式
 * 
 * @author xiaolong.chang
 * 
 */
public class FileUploadView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Writer writer = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(model.get("result"));
        writer.write("<textarea>");
        writer.write(json);
        writer.write("</textarea>");
    }
}
