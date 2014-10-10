/**
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Baidu company (the "License");
 * you may not use this file except in compliance with the License.
 *
 */
package com.baidu.jprotobuf.rpc.server;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.baidu.bjf.remoting.protobuf.IDLProxyObject;
import com.baidu.jprotobuf.rpc.support.IDLProxyCreator;
import com.baidu.jprotobuf.rpc.support.IOUtils;

/**
 * 
 * @author xiemalin
 * @since 1.0.0
 */
public class IDLServiceExporter implements ServiceExporter, InitializingBean {

    private String serviceName;

    private ServerInvoker invoker;

    /**
     * input protobuf IDL
     */
    private Resource inputIDL;

    /**
     * input protobuf IDL defined object name for multiple message object
     * defined select
     */
    private String inputIDLObjectName;

    /**
     * output protobuf IDL
     */
    private Resource outputIDL;

    /**
     * output protobuf IDL defined object name for multiple message object
     * defined select
     */
    private String outputIDLObjectName;

    private IDLProxyObject inputIDLProxyObject;

    private IDLProxyObject outputIDLProxyObject;

    /**
     * get the invoker
     * 
     * @return the invoker
     */
    protected ServerInvoker getInvoker() {
        return invoker;
    }

    /**
     * set invoker value to invoker
     * 
     * @param invoker
     *            the invoker to set
     */
    public void setInvoker(ServerInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * get the serviceName
     * 
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * set serviceName value to serviceName
     * 
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(invoker, "property 'invoker' is null.");
        Assert.hasText(serviceName, "property 'serviceName' is blank.");
        
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
    }

    public IDLProxyObject execute(IDLProxyObject inputIDLProxyObject) throws Exception {
        IDLProxyObject output = getOutputIDLProxyObject();
        invoker.invoke(inputIDLProxyObject, output);
        return output;
    }

    /**
     * get the inputIDLProxyObject
     * @return the inputIDLProxyObject
     */
    public IDLProxyObject getInputProxyObject() {
        if (inputIDLProxyObject != null) {
            return inputIDLProxyObject.newInstnace();
        }
        
        return inputIDLProxyObject;
    }

    /**
     * set inputIDLProxyObject value to inputIDLProxyObject
     * @param inputIDLProxyObject the inputIDLProxyObject to set
     */
    protected void setInputIDLProxyObject(IDLProxyObject inputIDLProxyObject) {
        this.inputIDLProxyObject = inputIDLProxyObject;
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

    /**
     * set outputIDLProxyObject value to outputIDLProxyObject
     * @param outputIDLProxyObject the outputIDLProxyObject to set
     */
    protected void setOutputIDLProxyObject(IDLProxyObject outputIDLProxyObject) {
        this.outputIDLProxyObject = outputIDLProxyObject;
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
    
    
}
