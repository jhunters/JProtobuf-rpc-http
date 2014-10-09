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

import org.springframework.util.Assert;


/**
 * <p>Pre-implements serialization of Protobuf objects and
 * deserialization of Protobuf objects.
 * 
 * @author xiemalin
 * @since 1.0.0
 */
public abstract class AbstractHttpRequestExecutor {

    /**
     * Default content type: "application/x-java-serialized-object"
     */
    public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";


    protected static final String HTTP_METHOD_POST = "POST";

    protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";

    protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";

    protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";

    protected static final String ENCODING_GZIP = "gzip";
    
    private String contentType = CONTENT_TYPE_SERIALIZED_OBJECT;

    private boolean acceptGzipEncoding = true;
    
    /**
     * Specify the content type to use for sending HTTP invoker requests.
     * <p>Default is "application/x-java-serialized-object".
     */
    public void setContentType(String contentType) {
        Assert.notNull(contentType, "'contentType' must not be null");
        this.contentType = contentType;
    }

    /**
     * Return the content type to use for sending HTTP invoker requests.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Set whether to accept GZIP encoding, that is, whether to
     * send the HTTP "Accept-Encoding" header with "gzip" as value.
     * <p>Default is "true". Turn this flag off if you do not want
     * GZIP response compression even if enabled on the HTTP server.
     */
    public void setAcceptGzipEncoding(boolean acceptGzipEncoding) {
        this.acceptGzipEncoding = acceptGzipEncoding;
    }

    /**
     * Return whether to accept GZIP encoding, that is, whether to
     * send the HTTP "Accept-Encoding" header with "gzip" as value.
     */
    public boolean isAcceptGzipEncoding() {
        return this.acceptGzipEncoding;
    }
}
