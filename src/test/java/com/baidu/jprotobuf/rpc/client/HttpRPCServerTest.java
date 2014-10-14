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
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.jprotobuf.rpc.server.HttpRPCServer;
import com.baidu.jprotobuf.rpc.server.HttpServerRequestHandler;
import com.baidu.jprotobuf.rpc.server.ServiceExporter;

/**
 * Test class for {@link HttpRPCServer}.
 *
 * @author xiemalin
 * @since 1.4.0
 */
public class HttpRPCServerTest {
    
    @Test
    public void testRPCRequestIDL() {
        HttpRPCServer httpRPCServer = null;
        try {
            // 创建RPC Server  指定端口
            httpRPCServer = new HttpRPCServer(8080, 10);

            AnnotationProxyFactoryBeanTest test = new AnnotationProxyFactoryBeanTest();
            ServiceExporter serviceExporter = test.doCreateServiceExporter();
            
            //新增RPC服务
            HttpServerRequestHandler httpHandler = new HttpServerRequestHandler(serviceExporter);
            httpRPCServer.addRPCHandler(httpHandler);
            
            //启动 RPC Server
            //启动，RPC服务url地址为: http://localhost:8080/{serviceExporter.getServiceName}
            httpRPCServer.start();
            
            HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/SimpleIDLTest?" + ServiceExporter.INPUT_IDL_PARAMETER + "=aa").openConnection();
            connection.connect();
            byte[] readResponse = readResponse(connection);
            
            Assert.assertEquals(serviceExporter.getInputIDL(), new String(readResponse));
            
            connection = (HttpURLConnection) new URL("http://localhost:8080/SimpleIDLTest?" + ServiceExporter.OUTPUT_IDL_PARAMETER + "=aa").openConnection();
            connection.connect();
            readResponse = readResponse(connection);
            
            Assert.assertEquals(serviceExporter.getOutputIDL(), new String(readResponse));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpRPCServer != null) {
                httpRPCServer.stop(0);
            }
        }
    }

    /**
     * 
     */
    @Test
    public void testRPCRequest() {
        HttpRPCServer httpRPCServer = null;
        try {
            // 创建RPC Server  指定端口
            httpRPCServer = new HttpRPCServer(8080, 10);

            AnnotationProxyFactoryBeanTest test = new AnnotationProxyFactoryBeanTest();
            ServiceExporter serviceExporter = test.doCreateServiceExporter();
            
            //新增RPC服务
            HttpServerRequestHandler httpHandler = new HttpServerRequestHandler(serviceExporter);
            httpRPCServer.addRPCHandler(httpHandler);
            
            //启动 RPC Server
            //启动，RPC服务url地址为: http://localhost:8080/{serviceExporter.getServiceName}
            httpRPCServer.start();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpRPCServer != null) {
                httpRPCServer.stop(0);
            }
        }
    }

    /**
     * @param connection
     * @return
     */
    private byte[] readResponse(HttpURLConnection connection) {
        InputStream in = null;
        HttpURLConnection httpconnection = (HttpURLConnection) connection;
        byte[] resBytes;
        try {
            if (httpconnection.getResponseCode() == 200) {
                in = httpconnection.getInputStream();
            } else if ( (httpconnection.getErrorStream() != null)) {
                in = httpconnection.getErrorStream();
            } else {
                in = httpconnection.getInputStream();
            }

            int len = httpconnection.getContentLength();
            if (len <= 0) {
                throw new RuntimeException("no response to get.");
            }
            resBytes = new byte[len];
            int offset = 0;
            while (offset < resBytes.length) {
                int bytesRead = in.read(resBytes, offset, resBytes.length - offset);

                if (bytesRead == -1)
                    break;
                offset += bytesRead;
            }
            if (offset <= 0) {
                throw new RuntimeException("there is no service to ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return resBytes;
    }

   
}
