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

/**
 *
 * @author xiemalin
 * @since 1.1.0
 */
public abstract class AbstractProxyFactoryBean {

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
     * set serviceUrl value to serviceUrl
     * @param serviceUrl the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
    
    /**
     * set connectTimeout value to connectTimeout
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * set readTimeout value to readTimeout
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * get the serviceUrl
     * @return the serviceUrl
     */
    protected String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * get the connectTimeout
     * @return the connectTimeout
     */
    protected int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * get the readTimeout
     * @return the readTimeout
     */
    protected int getReadTimeout() {
        return readTimeout;
    }
    
    
}
