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


/**
 * Abstract class provide for base properties.
 *
 * @author xiemalin
 * @since 1.2.0
 */
public abstract class AbstractServiceExporter implements ServiceExporter {

    private String serviceName;

    private ServerInvoker invoker;
    
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

}
