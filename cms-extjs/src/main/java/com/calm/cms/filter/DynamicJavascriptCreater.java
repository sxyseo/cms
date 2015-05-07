package com.calm.cms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

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
	private ApplicationContext applicationContext;
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
		int lastIndexOf = substring.lastIndexOf("/");
		String templatePath = substring.substring(0, lastIndexOf);
		String id = substring.substring(lastIndexOf+1);
		Configuration configuration = markerConfig.getConfiguration();
		markerConfig.setDefaultEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			configuration.setEncoding(Locale.CHINA, "UTF-8");
			Template template = configuration.getTemplate(templatePath+".ftl");
			StaticDataBuilder bean = applicationContext.getBean(templatePath, StaticDataBuilder.class);
			Object builder = bean.builder(id);
			template.process(builder, writer);
			return ;
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
	
} 
