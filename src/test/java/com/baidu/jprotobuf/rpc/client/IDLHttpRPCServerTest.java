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

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.jprotobuf.rpc.server.HttpRPCServer;
import com.baidu.jprotobuf.rpc.server.HttpServerRequestHandler;
import com.baidu.jprotobuf.rpc.server.IDLServiceExporter;
import com.baidu.jprotobuf.rpc.server.ServerInvoker;

/**
 * Simple test for RPC client and server from Google protobuf IDL 
 *
 * @author xiemalin
 * @since 1.5.0
 */
public class IDLHttpRPCServerTest {

    @Test
    public void testSimpleRPC() {
        // google protobuf defined idl 
        String idl = "package pkg; "
            + "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";"
            + "option java_outer_classname = \"StringTypeClass\";"
            + "message StringMessage { required string msg = 1;}  ";
        
        ByteArrayResource resource = new ByteArrayResource(idl.getBytes());
        
        HttpRPCServer httpRPCServer = null;
        try {
            // 创建RPC Server  指定端口
            httpRPCServer = new HttpRPCServer(8080, 10);

            IDLServiceExporter idlServiceExporter = new IDLServiceExporter();
            idlServiceExporter.setInputIDL(resource);
            idlServiceExporter.setOutputIDL(resource);
            idlServiceExporter.setServiceName("SimpleTest");
            idlServiceExporter.setInvoker(new ServerInvoker() {
                
                @Override
                public void invoke(IDLProxyObject input, IDLProxyObject output) throws Exception {
                    
                    if (input != null) { // if has request
                        Object msg = input.get("msg");
                        System.out.println("Get 'msg' from request value =" + msg);
                    }
                    
                    if (output != null) { // if need response result
                        output.put("msg", "new message");
                    }
                    
                }
            });
            idlServiceExporter.afterPropertiesSet();
            
            // 新增RPC服务
            HttpServerRequestHandler httpHandler = new HttpServerRequestHandler(idlServiceExporter);
            httpRPCServer.addRPCHandler(httpHandler);
            
            // 启动 RPC Server
            // 启动，RPC服务url地址为: http://localhost:8080/{serviceExporter.getServiceName}
            httpRPCServer.start();
            
            
            // 客户端访问
            IDLProxyFactoryBean idlProxyFactoryBean = new IDLProxyFactoryBean();
            idlProxyFactoryBean.setServiceUrl("http://localhost:8080/SimpleTest");
            idlProxyFactoryBean.setInputIDL(resource);
            idlProxyFactoryBean.setOutputIDL(resource);
            idlProxyFactoryBean.afterPropertiesSet();
            
            ClientInvoker<IDLProxyObject, IDLProxyObject> invoker = idlProxyFactoryBean.getObject();
            
            // send request
            IDLProxyObject input = invoker.getInput();
            if (input != null) {
                input.put("msg", "hello message.");
            }
            IDLProxyObject result = invoker.invoke(input);
            if (result != null) {
                Object msg = result.get("msg");
                Assert.assertEquals("new message", msg);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpRPCServer != null) {
                httpRPCServer.stop(0);
            }
        }
    }
    
}
