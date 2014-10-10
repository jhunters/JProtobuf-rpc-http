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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

/**
 * 
 * @author xiemalin
 * @since 1.1.0
 */
public class AnnotationProxyFactoryBean<I, O> extends AbstractProxyFactoryBean implements
    FactoryBean<ClientInvoker<I, O>>, InitializingBean {

    private IDLHttpClientInvoker invoker;
    
    private Codec<? extends Object> inputCodec;
    private Codec<? extends Object> outputCodec;

    ClientInvoker<I, O> proxy = new ClientInvoker<I, O>() {

        @Override
        public I getInput() {
            if (inputClass == null) {
                return null;
            }
            try {
                return (I) inputClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public O invoke(I input) throws Exception {
            IDLProxyObject inputObject = null;
            if (inputClass != null) {
                inputObject = new IDLProxyObject(inputCodec, input, inputClass);
            }
            
            IDLProxyObject result = invoker.invoke(inputObject);
            if (result == null) {
                return null;
            }
            return (O) result.getTarget();
        }

    };
    
    private Class<I> inputClass;
    private Class<O> outputClass;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public ClientInvoker<I, O> getObject() throws Exception {
        return proxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return IDLHttpClientInvoker.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        IDLProxyObject inputIDLProxyObject = null;
        if (inputClass != null) {
            inputCodec = ProtobufProxy.create(inputClass);
            I input = inputClass.newInstance();
            inputIDLProxyObject = new IDLProxyObject(inputCodec, input, inputClass);
        }
        IDLProxyObject outputIDLProxyObject = null;
        if (outputClass != null) {
            outputCodec = ProtobufProxy.create(outputClass);
            O output = outputClass.newInstance();
            outputIDLProxyObject = new IDLProxyObject(outputCodec, output, outputClass);
        }

        invoker = new IDLHttpClientInvoker(getServiceUrl(), inputIDLProxyObject, outputIDLProxyObject);
        invoker.setConnectTimeout(getConnectTimeout());
        invoker.setReadTimeout(getReadTimeout());

    }

    /**
     * set inputClass value to inputClass
     * @param inputClass the inputClass to set
     */
    public void setInputClass(Class<I> inputClass) {
        this.inputClass = inputClass;
    }

    /**
     * set outputClass value to outputClass
     * @param outputClass the outputClass to set
     */
    public void setOutputClass(Class<O> outputClass) {
        this.outputClass = outputClass;
    }

    
}
