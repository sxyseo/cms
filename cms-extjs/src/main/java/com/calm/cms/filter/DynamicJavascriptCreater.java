package com.calm.cms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.fastjson.JSON;
import com.calm.cms.api.entity.Relation;
import com.calm.cms.api.entity.TableColumn;
import com.calm.cms.api.entity.TableDefined;
import com.calm.cms.api.service.ITableDefinedService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Servlet Filter implementation class Dy
 */
@Service
public class DynamicJavascriptCreater implements Filter {
	private String contextPath ;
	@Resource
	private FreeMarkerConfigurer markerConfig;
	@Resource
	private ITableDefinedService tableDefinedService;
    /**
     * Default constructor. 
     */
    public DynamicJavascriptCreater() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Transactional
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hsr=(HttpServletRequest) request;
		request.setCharacterEncoding("UTF-8");;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/javascript;charset=UTF-8");
		String requestURI = hsr.getRequestURI();
		if(contextPath==null){
			contextPath = hsr.getContextPath();
		}
		if(contextPath.length()>0){
			requestURI = requestURI.substring(contextPath.length());
		}
		String substring = requestURI.substring(0,requestURI.lastIndexOf("."));
		String[] split = substring.split("-");
		String[] split2 = split[0].split("/");
		String join = StringUtils.join(split2, "/");
		Configuration configuration = markerConfig.getConfiguration();
		markerConfig.setDefaultEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			configuration.setEncoding(Locale.CHINA, "UTF-8");
			Template template = configuration.getTemplate(join+".ftl");
			TableDefined loadById = tableDefinedService.loadById(Integer.parseInt(split[1]));
			template.process(buildData(loadById), writer);
		} catch (Exception e) {
			e.printStackTrace(writer);
		}
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	public Map<String,Object> buildData(TableDefined loadById){
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("id", loadById.getId());
		result.put("title", loadById.getName());
		getColumnFields(loadById,result);
		return result;
	}
	private void getColumnFields(TableDefined loadById,Map<String,Object> map){
		List<TableColumn> columns = loadById.getColumns();
		
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<String> fields =new ArrayList<String>();
		for(TableColumn tc:columns){
			Map<String,Object> item=column2Items(tc);
			if(item!=null){
				items.add(item);
				if(tc.getRelation()==Relation.MANY2ONE){
					fields.add(tc.getId().getId()+"_ID");
    			}else{
    				fields.add(tc.getId().getId());
    			}
			}
		}
		map.put("fields", JSON.toJSONString(fields));
		map.put("columns", JSON.toJSONString(items));
		map.put("width",""+( fields.size()*200+11));
	}

	private Map<String, Object> column2Items(TableColumn tc) {
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("text", tc.getName());
		result.put("width", 200);
		switch (tc.getRelation()) {
		case ONE2MANY:
			return null;
		case MANY2ONE:
			result.put("dataIndex", tc.getId().getId()+"_ID");
			return result;
		default:
			break;
		}
//		if(tc.getProcessor().getType()==ProcessorType.TABLE){
			result.put("dataIndex", tc.getId().getId());
			return result;
//		}else{
//			
//		}
//    	if(c.processor.type='table'){
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}else{
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}
//		return null;
	}
} 
