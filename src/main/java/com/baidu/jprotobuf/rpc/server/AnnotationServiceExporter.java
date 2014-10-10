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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

/**
 * Export protobuf RPC service for JProtobuf annotation POJO class.
 * 
 * @author xiemalin
 * @since 1.2.0
 * @see HttpRequestHandlerServlet
 */
public class AnnotationServiceExporter extends AbstractServiceExporter implements InitializingBean {

    private IDLProxyObject inputIDLProxyObject;

    private IDLProxyObject outputIDLProxyObject;
    
    private Class<?> inputClass;
    private Class<?> outputClass;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.jprotobuf.rpc.server.ServiceExporter#execute(com.baidu.bjf.
     * remoting.protobuf.IDLProxyObject)
     */
    @Override
    public IDLProxyObject execute(IDLProxyObject input) throws Exception {
        IDLProxyObject output = getOutputIDLProxyObject();
        getInvoker().invoke(input, output);
        return output;
    }
    
    /**
     * get the outputIDLProxyObject
     * @return the outputIDLProxyObject
     */
    protected IDLProxyObject getOutputIDLProxyObject() {
        if (outputIDLProxyObject != null) {
            return outputIDLProxyObject.newInstnace();
        }
        return outputIDLProxyObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.jprotobuf.rpc.server.ServiceExporter#getInputProxyObject()
     */
    @Override
    public IDLProxyObject getInputProxyObject() {
        return inputIDLProxyObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getInvoker(), "property 'invoker' is null.");
        Assert.hasText(getServiceName(), "property 'serviceName' is blank.");
        
        if (inputClass != null) {
            Codec inputCodec = ProtobufProxy.create(inputClass);
            Object input = inputClass.newInstance();
            inputIDLProxyObject = new IDLProxyObject(inputCodec, input, inputClass);
        }
        if (outputClass != null) {
            Codec outputCodec = ProtobufProxy.create(outputClass);
            Object output = outputClass.newInstance();
            outputIDLProxyObject = new IDLProxyObject(outputCodec, output, outputClass);
        }
    }

    /**
     * set outputIDLProxyObject value to outputIDLProxyObject
     * @param outputIDLProxyObject the outputIDLProxyObject to set
     */
    public void setOutputIDLProxyObject(IDLProxyObject outputIDLProxyObject) {
        this.outputIDLProxyObject = outputIDLProxyObject;
    }

    /**
     * set outputClass value to outputClass
     * @param outputClass the outputClass to set
     */
    public void setOutputClass(Class<?> outputClass) {
        this.outputClass = outputClass;
    }

    /**
     * set inputClass value to inputClass
     * @param inputClass the inputClass to set
     */
    public void setInputClass(Class<?> inputClass) {
        this.inputClass = inputClass;
    }

    
}
