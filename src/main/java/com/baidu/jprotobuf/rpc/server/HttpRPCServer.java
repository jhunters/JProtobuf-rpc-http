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
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * 
 * Standalone HTTP server base on JDK 6 httpserver engine.<br>
 * It provides RPC service for {@link HttpServerRequestHandler}
 *
 * @author xiemalin
 * @since 1.4.0
 */
public class HttpRPCServer {

    /**
     * {@link HttpServer} instance.
     */
    private HttpServer httpserver;
    
    /**
     * cached context names set
     */
    private Set<String> cachedContextNames = new HashSet<String>();
    
    /**
     * 
     * @param port server port
     * @param backlog backlog size
     * @exception IOException in case of any io operation failed
     */
    public HttpRPCServer(int port, int backlog) throws IOException {
        this(new InetSocketAddress(port), backlog);
    }
    
    public HttpRPCServer(InetSocketAddress address, int backlog) throws IOException {
        HttpServerProvider provider = HttpServerProvider.provider();
        httpserver = provider.createHttpServer(address, backlog);
        
    }
    
    /**
     * To set specified {@link Executor} of thread pool handler to process concurrent HTTP request
     * @param executor
     */
    public void setExecutor(Executor executor) {
        httpserver.setExecutor(executor);
    }
    
    /**
     * @param handler
     */
    public void addRPCHandler(HttpServerRequestHandler handler) {
        if (handler == null) {
            throw new RuntimeException("param 'handler' is null.");
        }
        String contextName = handler.getContextName();
        if (contextName == null) {
            contextName = "/";
        } else if (!contextName.startsWith("/")) {
            contextName = "/" + contextName;
        }
        
        if (cachedContextNames.contains(contextName)) {
            throw new RuntimeException("contextName already exist. name = " + contextName);
        }
        
        httpserver.createContext(contextName, handler);
    }
    
    public void start() {
        if (httpserver == null) {
            throw new RuntimeException("failed to start http server. 'httpserver' is null");
        }
        httpserver.start();
    }
    
    public void stop(int timeout) {
        if (httpserver != null) {
            httpserver.stop(timeout);
        }
    }
    
}
