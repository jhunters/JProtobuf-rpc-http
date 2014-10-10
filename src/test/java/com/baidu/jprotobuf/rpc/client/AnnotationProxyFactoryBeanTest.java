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

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.jprotobuf.rpc.server.AnnotationServiceExporter;
import com.baidu.jprotobuf.rpc.server.ServerInvoker;
import com.baidu.jprotobuf.rpc.server.ServiceExporter;

/**
 * 
 *
 * @author xiemalin
 * @since 1.1.0
 */
public class AnnotationProxyFactoryBeanTest extends ProxyFactoryBeanTestBase {

    @Test
    public void testClientProxy() throws Exception {
        AnnotationProxyFactoryBean<StringMessagePOJO, StringMessagePOJO> factoryBean;
        
        factoryBean = new AnnotationProxyFactoryBean<StringMessagePOJO, StringMessagePOJO>();
        factoryBean.setServiceUrl("http://localhost:8080/SimpleIDLTest");
        
        factoryBean.setInputClass(StringMessagePOJO.class);
        factoryBean.setOutputClass(StringMessagePOJO.class);
        
        factoryBean.afterPropertiesSet();
        
        ClientInvoker<StringMessagePOJO, StringMessagePOJO> invoker = factoryBean.getObject();
        
        StringMessagePOJO input = invoker.getInput();
        if (input != null) {
            input.setList("how are you!");
        }
        
        StringMessagePOJO output = invoker.invoke(input);
        if (output != null) {
            Assert.assertEquals("hello world", output.getList());
        }
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.rpc.client.ProxyFactoryBeanTestBase#doCreateServiceExporter()
     */
    @Override
    protected ServiceExporter doCreateServiceExporter() {
        AnnotationServiceExporter exporter = new AnnotationServiceExporter();
        exporter.setInputClass(StringMessagePOJO.class);
        exporter.setOutputClass(StringMessagePOJO.class);
        
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
    
    @Override
    protected String getPathInfo() {
        return "/SimpleIDLTest";
    }
}
