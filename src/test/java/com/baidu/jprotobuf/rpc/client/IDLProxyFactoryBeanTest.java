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
import com.baidu.jprotobuf.rpc.server.IDLServiceExporter;
import com.baidu.jprotobuf.rpc.server.ServerInvoker;
import com.baidu.jprotobuf.rpc.server.ServiceExporter;

/**
 * 
 * @author xiemalin
 * @since 1.0.0
 */
public class IDLProxyFactoryBeanTest extends ProxyFactoryBeanTestBase {

    String idl = "package pkg; "
            + "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";"
            + "option java_outer_classname = \"StringTypeClass\";"
            + "message StringMessage { required string list = 1;}  ";

    ByteArrayResource resource = new ByteArrayResource(idl.getBytes());

    protected ServiceExporter doCreateServiceExporter() {
        IDLServiceExporter exporter = new IDLServiceExporter();
        exporter.setInputIDL(resource);
        exporter.setOutputIDL(resource);
        exporter.setServiceName("SimpleIDLTest");
        exporter.setInvoker(new ServerInvoker() {

            @Override
            public void invoke(IDLProxyObject input, IDLProxyObject output) throws Exception {
                Assert.assertNotNull(input);
                Assert.assertEquals("how are you!", input.get("list"));

                if (output != null) {
                    output.put("list", "hello world");
                }
            }
        });
        try {
            exporter.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exporter;
    }
    
    @Test
    public void testRPCRequestIDL() throws Exception {
        HttpRPCServerTest test = new HttpRPCServerTest();
        test.testIDLClientQuery(idl, idl);
    }

    @Test
    public void testProxyFactoryBean() throws Exception {

        IDLProxyFactoryBean proxyFactoryBean = new IDLProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("http://localhost:8080/SimpleIDLTest");
        proxyFactoryBean.setInputIDL(resource);
        proxyFactoryBean.setOutputIDL(resource);
        proxyFactoryBean.setInputIDLObjectName("StringMessage");
        proxyFactoryBean.setOutputIDLObjectName("StringMessage");
        proxyFactoryBean.afterPropertiesSet();
        ClientInvoker<IDLProxyObject, IDLProxyObject> invoker = proxyFactoryBean.getObject();

        // set request param
        IDLProxyObject input = invoker.getInput();

        input.put("list", "how are you!");
        IDLProxyObject output = invoker.invoke(input);

        Assert.assertEquals("hello world", output.get("list"));

    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.rpc.client.ProxyFactoryBeanTestBase#getPathInfo()
     */
    @Override
    protected String getPathInfo() {
        return "/SimpleIDLTest";
    }
}
