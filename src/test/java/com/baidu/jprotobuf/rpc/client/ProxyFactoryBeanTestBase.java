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
package com.baidu.jprotobuf.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.baidu.jprotobuf.rpc.server.HttpRequestHandlerServlet;
import com.baidu.jprotobuf.rpc.server.ServiceExporter;
import com.baidu.jprotobuf.rpc.support.IOUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * 
 * @author xiemalin
 * @since 1.4.0
 */
public abstract class ProxyFactoryBeanTestBase {
    
    HttpServer server;
    @Before
    public void setUp() {
        try {
            server = createServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    protected HttpRequestHandlerServlet servlet = new HttpRequestHandlerServlet() {

        protected java.util.Map<String, com.baidu.jprotobuf.rpc.server.ServiceExporter> getServiceExporters() {
            Map<String, ServiceExporter> ret = new HashMap<String, ServiceExporter>();
            ret.put("SimpleIDLTest", createServiceExporter());
            return ret;
        };
    };

    private ServiceExporter createServiceExporter() {
        return doCreateServiceExporter();
    }

    /**
     * @return
     */
    protected abstract ServiceExporter doCreateServiceExporter();
    
    protected abstract String getPathInfo();

    protected HttpServer createServer() throws Exception {

        servlet.init();

        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(8080), 10);

        httpserver.createContext(getPathInfo(), new HttpHandler() {

            @Override
            public void handle(HttpExchange httpExchange) throws IOException {

                MockHttpServletRequest request = new MockHttpServletRequest();
                request.setPathInfo(getPathInfo());

                InputStream requestBody = httpExchange.getRequestBody();
                request.setContent(IOUtils.toByteArray(requestBody));

                MockHttpServletResponse response = new MockHttpServletResponse();
                response.setOutputStreamAccessAllowed(true);

                try {
                    servlet.service(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                httpExchange.sendResponseHeaders(200, response.getContentLength());
                OutputStream out = httpExchange.getResponseBody(); // 获得输出流
                out.write(response.getContentAsByteArray());
                out.flush();
                httpExchange.close();
            }
        });
        httpserver.setExecutor(null);
        httpserver.start();

        return httpserver;
    }
}
