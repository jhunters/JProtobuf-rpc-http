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

/**
 * 
 *
 * @author xiemalin
 * @since 1.1.0
 */
public class AnnotationProxyFactoryBeanTest {

    @Test
    public void testClientProxy() throws Exception {
        AnnotationProxyFactoryBean<StringMessagePOJO, StringMessagePOJO> factoryBean;
        
        factoryBean = new AnnotationProxyFactoryBean<StringMessagePOJO, StringMessagePOJO>();
        factoryBean.setServiceUrl("http://localhost:8080/myfirstproject/remoting/SimpleIDLTest2");
        
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
            System.out.println(output.getList());
        }
    }
}
