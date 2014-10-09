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
package com.baidu.jprotobuf.rpc.support;

import java.util.Map;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;

/**
 * protobuf IDL proxy object creator.
 *
 * @author xiemalin
 * @since 1.0.0
 */
public class IDLProxyCreator {

    /**
     * input protobuf IDL content
     */
    private String inputIDL;
    
    /**
     * output protobuf IDL content
     */
    private String outputIDL;
    
    private Map<String, IDLProxyObject> inputProxyObjectMap;
    
    private Map<String, IDLProxyObject> outputProxyObjectMap;

    /**
     * @param inputIDL
     * @param outputIDL
     */
    public IDLProxyCreator(String inputIDL, String outputIDL) {
        super();
        this.inputIDL = inputIDL;
        this.outputIDL = outputIDL;
        
        if (inputIDL != null) {
            inputProxyObjectMap = ProtobufIDLProxy.create(inputIDL);
        }
        
        if (outputIDL != null) {
            outputProxyObjectMap = ProtobufIDLProxy.create(outputIDL);
        }
    }
    
    /**
     * get Input {@link IDLProxyObject}. if exist more one defined object will
     * throws exception.
     * 
     * @return
     */
    public IDLProxyObject getInputProxyObject() {
        return getOne(inputProxyObjectMap);
    }
    
    /**
     * get Input {@link IDLProxyObject}. if exist more one defined object will
     * throws exception.
     * @param name defined object name
     * @return
     */
    public IDLProxyObject getInputProxyObject(String name) {
        return getOne(inputProxyObjectMap, name);
    }
    
    private IDLProxyObject getOne( Map<String, IDLProxyObject> proxyObjectMap) {
        if (proxyObjectMap == null) {
            return null;
        }
        
        if (proxyObjectMap.size() > 1) {
            throw new RuntimeException("IDL include multi object defined please get by name.");
        }
        
        return proxyObjectMap.entrySet().iterator().next().getValue();
    }
    
    private IDLProxyObject getOne( Map<String, IDLProxyObject> proxyObjectMap,
        String name) {
        
        if (proxyObjectMap == null) {
            return null;
        }
        
        if (name == null) {
            return getOne(proxyObjectMap);
        }
        return proxyObjectMap.get(name);
    }
    
    /**
     * get Output {@link IDLProxyObject}. if exist more one defined object will
     * throws exception.
     * @return
     */
    public IDLProxyObject getOutputProxyObject() {
        return getOne(outputProxyObjectMap);
    }
    
    /**
     * get Output {@link IDLProxyObject}. if exist more one defined object will
     * throws exception.
     * @param name defined object name
     * @return
     */
    public IDLProxyObject getOutputProxyObject(String name) {
        return getOne(outputProxyObjectMap, name);
    }

    /**
     * get the inputIDL
     * @return the inputIDL
     */
    public String getInputIDL() {
        return inputIDL;
    }

    /**
     * get the outputIDL
     * @return the outputIDL
     */
    public String getOutputIDL() {
        return outputIDL;
    }
    
    
}
