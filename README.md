jprotobuf-rpc-http
=========

jprotobuf-rpc-http 是应用jprotobuf类库实现基于http协议的RPC开发组件。
目前1.0提供可以直接把Google protobuf的IDL定义语言发布成RPC服务，客户端也可以直接应用IDL定义语言进行动态创建，帮助开发完全省去了手工编译protobuf IDL语言的麻烦。

jprotobuf文档：[https://github.com/jhunters/jprotobuf](https://github.com/jhunters/jprotobuf)


## 环境要求 ##
JDK 6 或以上版本
Spring 3.0+

## API使用说明 ##

### RPC服务的发布 ###

a 在Spring配置文件，定义IDLServiceExporter服务发布配置 

```xml
	<bean class="com.baidu.jprotobuf.rpc.server.IDLServiceExporter">
		<property name="serviceName" value="SimpleIDLTest"></property>
		<property name="invoker" ref="simpleIDLInvoker"></property>
		<property name="inputIDL" value="classpath:/simplestring.proto"></property>
		<property name="outputIDL" value="classpath:/simplestring.proto"></property>	
	</bean>

	<bean id="simpleIDLInvoker" class="com.baidu.bjf.SimpleIDLInvoker"></bean>
```
<pre>
inputIDL 属性表示接收的protobuf协议定义
outputIDL 属性表示返回的protobuf协议定义
serviceName 服务名称，必须填写。 在服务的servlet发布后，服务名称会以path路径方式查找
invoker 服务回调实现，必须实现 com.baidu.jprotobuf.rpc.server.ServerInvoker接口
</pre>

```java
public interface ServerInvoker {

    
    /**
     * RPC service call back method.
     * 
     * @param input request IDL proxy object by protobuf deserialized
     * @param output return back IDL proxy object to serialized
     * @throws Exception in case of any exception
     */
    void invoke(IDLProxyObject input, IDLProxyObject output) throws Exception;
}
``` 

```java
public class SimpleIDLInvoker implements ServerInvoker {

    @Override
    public void invoke(IDLProxyObject input, IDLProxyObject output) throws Exception {
        if (input != null) {
            System.out.println(input.get("list"));
        }
        if (output != null) {
            output.put("list", "hello world");
        }
    }

}
``` 

simplestring.proto 文件定义：

```java
package pkg;  

option java_package = "com.baidu.bjf.remoting.protobuf.simplestring";
  
option java_outer_classname = "StringTypeClass";  
  
message StringMessage {  
  required string list = 1;
}  
```


b web.xml文件配置服务发布servlet
```xml
	<servlet>
	    <servlet-name>protobufExporter</servlet-name>
	    <servlet-class>com.baidu.jprotobuf.rpc.server.HttpRequestHandlerServlet</servlet-class>
	</servlet>
	
	<servlet-mapping>
	    <servlet-name>protobufExporter</servlet-name>
	    <url-pattern>/remoting/*</url-pattern>
	</servlet-mapping>
``` 


### RPC客户端的API开发 ###
RPC客户端使用IDLProxyFactoryBean进行访问，示例代码如下：
```java
    @Test
    public void testProxyFactoryBean() throws Exception {
        String idl = "package pkg; " +  
        "option java_package = \"com.baidu.bjf.remoting.protobuf.simplestring\";" +
        "option java_outer_classname = \"StringTypeClass\";" +
        "message StringMessage { required string list = 1;}  ";
        
        ByteArrayResource resource = new ByteArrayResource(idl.getBytes());
        
        IDLProxyFactoryBean proxyFactoryBean = new IDLProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("http://localhost:8080/myfirstproject/remoting/SimpleIDLTest");
        proxyFactoryBean.setInputIDL(resource);
        proxyFactoryBean.setOutputIDL(resource);
        
        proxyFactoryBean.afterPropertiesSet();
        ClientInvoker invoker = proxyFactoryBean.getObject();
        
        //set request param
        IDLProxyObject input = invoker.getInput();
        
        input.put("list", "how are you!");
        IDLProxyObject output = invoker.invoke(input);
        
        System.out.println(output.get("list"));
       
    }
```
### RPC客户端Spring配置 ###

```xml
	<bean id="simpleTestClientForIDLProxy" class="com.baidu.jprotobuf.rpc.client.IDLProxyFactoryBean">
		<property name="inputIDL" value="classpath:/simplestring.proto"></property>
		<property name="outputIDL" value="classpath:/simplestring.proto"></property>
		<property name="inputIDLObjectName" value="StringMessage"></property>
		<property name="serviceUrl" value="http://localhost:8080/myfirstproject/remoting/SimpleIDLTest"></property>
	</bean>
```

### 多个IDL message定义解决方案 ###
例如下面定义了多个message定义时，则在服务发布以及客户连接时，需要指定objectName
```java
package pkg;  

option java_package = "com.baidu.bjf.remoting.protobuf.simplestring";
  
option java_outer_classname = "StringTypeClass";  
  
message StringMessage {  
  required string list = 1;
  optional StringMessage2 msg = 2;
}  

message StringMessage2 {  
  required string name = 1;
}  
```
下面示例只演示了客户端的配置，服务发布的配置也是相同

```xml
	<bean id="simpleTestClientForIDLProxy" class="com.baidu.jprotobuf.rpc.client.IDLProxyFactoryBean">
		<property name="inputIDL" value="classpath:/simplestring.proto"></property>
		<property name="outputIDL" value="classpath:/simplestring.proto"></property>
		<property name="inputIDLObjectName" value="StringMessage"></property>
		<property name="serviceUrl" value="http://localhost:8080/myfirstproject/remoting/SimpleIDLTest"></property>
	</bean>
```


## 联系我们 ##

email: [rigel-opensource@baidu.com](mailto://rigel-opensource@baidu.com "发邮件给jprotobuf开发组")


