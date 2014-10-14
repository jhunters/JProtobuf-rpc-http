/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.jprotobuf.rpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;

/**
 * Simple HttpServlet that delegates to an {@link ServiceExporter} bean defined
 * in Spring's root web application context. The target bean name must match the
 * HttpRequestHandlerServlet servlet-name as defined in <code>web.xml</code>.
 *
 * @author xiemalin
 * @since 1.0.0
 */
public class HttpRequestHandlerServlet extends HttpServlet {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(HttpRequestHandlerServlet.class);
    
    private Map<String, ServiceExporter> serviceMap;
    
    protected Map<String, ServiceExporter> getServiceExporters() {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        Map<String, ServiceExporter> beans = wac.getBeansOfType(ServiceExporter.class);
        return beans;
    }
    
    public void init() throws ServletException {
        
        serviceMap = new HashMap<String, ServiceExporter>();
        
        Map<String, ServiceExporter> beans = getServiceExporters();
        
        if (beans != null) {
            Collection<ServiceExporter> services = beans.values();
            for (ServiceExporter idlServiceExporter : services) {
                serviceMap.put(idlServiceExporter.getServiceName(), idlServiceExporter);
                
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Detected protobuf exporter serivce name=" + idlServiceExporter.getServiceName());
                }
            }
        }
    }


    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String context = request.getPathInfo();
        if (context == null) {
            LOGGER.warn("invalid request path.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            if (context.startsWith("/")) {
                context = context.substring(1);
            }
            
            if (!serviceMap.containsKey(context)) {
                LOGGER.warn("invalid request path service name[" + context + "] not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;  
            }
        }
        
        try {
            ServiceExporter idlServiceExporter = serviceMap.get(context);
            
            //to check if a get idl request 
            if (request.getParameter(ServiceExporter.INPUT_IDL_PARAMETER) != null) {
                
                String inputIDL = idlServiceExporter.getInputIDL();
                if (inputIDL !=  null) {
                    response.setContentLength(inputIDL.length());
                    response.getOutputStream().write(inputIDL.getBytes());
                    
                    return;
                }
                
            } else if (request.getParameter(ServiceExporter.OUTPUT_IDL_PARAMETER) != null) {
                String outputIDL = idlServiceExporter.getOutputIDL();
                if (outputIDL !=  null) {
                    response.setContentLength(outputIDL.length());
                    response.getOutputStream().write(outputIDL.getBytes());
                    
                    return;
                }
            }
            
            IDLProxyObject inputIDLProxyObject = idlServiceExporter.getInputProxyObject();
            IDLProxyObject input = null;
            if (inputIDLProxyObject != null && request.getContentLength() > 0) {
                byte[] bytes = readStream(request.getInputStream(), request
                        .getContentLength());
                input = inputIDLProxyObject.decode(bytes);
            }
            
            IDLProxyObject result = idlServiceExporter.execute(input);
            if (result != null) {
                byte[] bytes = result.encode();
                response.setContentLength(bytes.length);
                response.getOutputStream().write(bytes);
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } finally {
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
    
    private byte[] readStream(InputStream input, int length) throws IOException {
        byte[] bytes = new byte[length];
        int offset = 0;
        while (offset < bytes.length) {
            int bytesRead = input.read(bytes, offset, bytes.length - offset);
            if (bytesRead == -1)
                break; // end of stream
            offset += bytesRead;
        }
        return bytes;
    }
}
