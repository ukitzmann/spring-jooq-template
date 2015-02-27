package de.evas.sample.servlet;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        /*
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations("classpath:spring-context.xml");
        servletContext.addListener(new ContextLoaderListener(context));
        */

        ServletRegistration.Dynamic appServlet = servletContext.addServlet("restServlet", new DispatcherServlet());
        appServlet.setInitParameter("contextConfigLocation", "classpath:spring-context.xml");
        appServlet.addMapping("/*");
    }
}
