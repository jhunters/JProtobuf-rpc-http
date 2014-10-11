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
import java.io.OutputStream;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.jprotobuf.rpc.support.IOUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This is to implements {@link HttpHandler} for JDK 6+ to provide
 * {@link ServiceExporter} <br>
 * beans RPC service handler support.
 * 
 * @author xiemalin
 * @since 1.4.0
 * @see HttpRPCServer
 */
public class HttpServerRequestHandler implements HttpHandler {
    
    /**
     * content length header code
     */
    private static final int CONTENT_LENGTH = 200;

    private ServiceExporter serviceExporter;
    
    /**
     * context name by create by httpserver
     */
    private String contextName;

    /**
     * @param serviceExporter
     */
    public HttpServerRequestHandler(ServiceExporter serviceExporter) {
        if (serviceExporter == null) {
            throw new RuntimeException("Param 'serviceExporter' is null");
        }
        contextName = serviceExporter.getServiceName();
        this.serviceExporter = serviceExporter;
    }

    /**
     * get context name
     * @return context name
     */
    public String getContextName() {
        return contextName;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
     * )
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        
        InputStream requestBody = httpExchange.getRequestBody();
        byte[] requestBytes = IOUtils.toByteArray(requestBody);
        
        IDLProxyObject inputIDLProxyObject = serviceExporter.getInputProxyObject();
        IDLProxyObject input = null;
        if (inputIDLProxyObject != null && requestBytes != null && requestBytes.length > 0) {
            input = inputIDLProxyObject.decode(requestBytes);
        }
        
        
        IDLProxyObject result = null;
        try {
            result = serviceExporter.execute(input);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
        OutputStream out = httpExchange.getResponseBody(); // 获得输出流
        if (result != null) {
            byte[] bytes = result.encode();
            if (bytes != null) {
                httpExchange.sendResponseHeaders(CONTENT_LENGTH, bytes.length);
                out.write(bytes);
            }
        }
        out.flush();
        httpExchange.close();
    }

}
