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
import org.springframework.core.io.Resource;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.jprotobuf.rpc.support.IDLProxyCreator;
import com.baidu.jprotobuf.rpc.support.IOUtils;

/**
 *
 * @author xiemalin
 * @since 1.0.0
 */
public class IDLProxyFactoryBean implements FactoryBean<ClientInvoker>, InitializingBean {
    
    /**
     * service url
     */
    private String serviceUrl;
    
    /**
     * connection time out
     */
    private int connectTimeout = -1;

    /**
     * read time out
     */
    private int readTimeout = -1;
    
    /**
     * input protobuf IDL
     */
    private Resource inputIDL;
    
    /**
     * input protobuf IDL defined object name for multiple message object defined select
     */
    private String inputIDLObjectName;
    
    /**
     * output protobuf IDL
     */
    private Resource outputIDL;
    
    /**
     * output protobuf IDL defined object name for multiple message object defined select
     */
    private String outputIDLObjectName;
    
    private IDLProxyObject inputIDLProxyObject;
    
    private IDLProxyObject outputIDLProxyObject;
    
    private IDLHttpClientInvoker invoker;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public ClientInvoker getObject() throws Exception {
        return invoker;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return IDLHttpClientInvoker.class;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        
        String inputIDLStr = null;
        if (inputIDL != null) {
            inputIDLStr = IOUtils.toString(inputIDL.getInputStream());
        }
        String outputIDLStr = null;
        if (outputIDL != null) {
            outputIDLStr = IOUtils.toString(outputIDL.getInputStream());
        }
        IDLProxyCreator idlProxyCreator = new IDLProxyCreator(inputIDLStr, outputIDLStr);
        
        inputIDLProxyObject = idlProxyCreator.getInputProxyObject(inputIDLObjectName);
        outputIDLProxyObject = idlProxyCreator.getOutputProxyObject(outputIDLObjectName);
        
        invoker = new IDLHttpClientInvoker(serviceUrl, inputIDLProxyObject, outputIDLProxyObject);
        invoker.setConnectTimeout(connectTimeout);
        invoker.setReadTimeout(readTimeout);
    }

    /**
     * set serviceUrl value to serviceUrl
     * @param serviceUrl the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * set inputIDL value to inputIDL
     * @param inputIDL the inputIDL to set
     */
    public void setInputIDL(Resource inputIDL) {
        this.inputIDL = inputIDL;
    }

    /**
     * set outputIDL value to outputIDL
     * @param outputIDL the outputIDL to set
     */
    public void setOutputIDL(Resource outputIDL) {
        this.outputIDL = outputIDL;
    }

    /**
     * set inputIDLObjectName value to inputIDLObjectName
     * @param inputIDLObjectName the inputIDLObjectName to set
     */
    public void setInputIDLObjectName(String inputIDLObjectName) {
        this.inputIDLObjectName = inputIDLObjectName;
    }

    /**
     * set outputIDLObjectName value to outputIDLObjectName
     * @param outputIDLObjectName the outputIDLObjectName to set
     */
    public void setOutputIDLObjectName(String outputIDLObjectName) {
        this.outputIDLObjectName = outputIDLObjectName;
    }

    /**
     * set connectTimeout value to connectTimeout
     * @param connectTimeout the connectTimeout to set
     */
    protected void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * set readTimeout value to readTimeout
     * @param readTimeout the readTimeout to set
     */
    protected void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    
}
