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

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;

/**
 *
 * @author xiemalin
 * @since 1.0.0
 */
public class IDLProxyFactoryBeanTest {

    
    @Test
    public void testProxyFactoryBean() throws Exception {
        String idl = "package pkg; " +  
        "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";" +
        "option java_outer_classname = \"StringTypeClass\";" +
        "message StringMessage { required string list = 1;}  message StringMessage2 { required string list = 1;} ";
        
        ByteArrayResource resource = new ByteArrayResource(idl.getBytes());
        
        IDLProxyFactoryBean proxyFactoryBean = new IDLProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("http://localhost:8080/myfirstproject/remoting/SimpleIDLTest");
        proxyFactoryBean.setInputIDL(resource);
        proxyFactoryBean.setOutputIDL(resource);
        proxyFactoryBean.setInputIDLObjectName("StringMessage");
        proxyFactoryBean.setOutputIDLObjectName("StringMessage2");
        proxyFactoryBean.afterPropertiesSet();
        ClientInvoker invoker = proxyFactoryBean.getObject();
        
        //set request param
        IDLProxyObject input = invoker.getInput();
        
        input.put("list", "how are you!");
        IDLProxyObject output = invoker.invoke(input);
        
        System.out.println(output.get("list"));
        
    }
}
