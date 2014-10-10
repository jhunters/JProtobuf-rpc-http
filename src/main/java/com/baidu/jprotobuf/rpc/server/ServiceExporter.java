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

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;

/**
 * ServiceExporter interface. 
 * All implements ServiceExporter class will be auto detected by {@link HttpRequestHandlerServlet}
 *
 * @author xiemalin
 * @since 1.2.0
 * @see HttpRequestHandlerServlet
 */
public interface ServiceExporter {

    
    /**
     * execute service action.
     * 
     * @param input
     * @return
     * @throws Exception
     */
    IDLProxyObject execute(IDLProxyObject input) throws Exception;
    
    
    /**
     * get RPC service input proxy object
     * 
     * @return
     */
    IDLProxyObject getInputProxyObject();
    
    /**
     * get service name
     * 
     * @return
     */
    String getServiceName() ;
}
