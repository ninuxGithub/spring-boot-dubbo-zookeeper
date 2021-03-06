

# spring boot dubbo zookeeper
    项目的目的： 在spring boot 里面使用dubbo 和zookeeper
    
    xml 配置dubbo 可以参考： https://github.com/ninuxGithub/dubbo-study
    

## zookepper 伪集群的搭建
    启动zookeeper在10.1.51.96:2181,10.1.51.96:2182 的两个端口
    参考zoo.cfg 保证 clientPort=2181，clientPort=2182 不同  
    在 dataDir 目录建立一个文件myid 输入编号
    server.1=10.1.51.96:2888:3888
    server.2=10.1.51.96:2889:3889
    
    格式为：
    server.编号=ip:端口：端口

    https://www.cnblogs.com/jtlgb/p/7792004.html
    
    
    
    在dataDir目录建立myid 填写服务的编号
    
    集群的配置参考：DubboConfigConfiguration
    
    zookeeper 查看软件
    https://blog.csdn.net/insis_mo/article/details/79196365
    
    集群搭建
    https://www.jianshu.com/p/1f4c70d7ef40
    
    java连接zookeeper 参考NettyRPC
    或者https://blog.csdn.net/u010398771/article/details/82420504
    
    zookeeper系列教程
    https://segmentfault.com/a/1190000012185322?utm_source=tag-newest#articleHeader3
    
    
## activemq 
    参考：https://www.cnblogs.com/elvinle/p/8457596.html
    
    
    

## swagger 使用
    
    swagger2页面：http://localhost:9091/swagger-ui.html
    

    参考了：https://www.cnblogs.com/jtlgb/p/8532433.html
    @Api：用在类上，说明该类的作用
    @ApiOperation：用在方法上，说明方法的作用
    @ApiImplicitParams：用在方法上包含一组参数说明
    @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
        paramType：参数放在哪个地方
        header-->请求参数的获取：@RequestHeader
        query-->请求参数的获取：@RequestParam
        path（用于restful接口）-->请求参数的获取：@PathVariable
        body（不常用）
        form（不常用）
        name：参数名
        dataType：参数类型
        required：参数是否必须传
        value：参数的意思
        defaultValue：参数的默认值
    @ApiResponses：用于表示一组响应
    @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
        code：数字，例如400
        message：信息，例如"请求参数没填好"
        response：抛出异常的类
    @ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
    @ApiModelProperty：描述一个model的属性    
    
    
    
## 多数据源的配置
    增加了一个sqlserver 数据源的配置， 测试连接jydb 获取数据
    
    insert , select, update, delete demo
    
    @Insert("insert into User(name,age) values(#{name},#{age})")
    int addUser(@Param("name") String name, @Param("age") int age);

    @Select("select * from User where id =#{id}")
    User findById(@Param("id") int id);

    @Update("update User set name=#{name} where id=#{id}")
    void updataById(@Param("id") int id, @Param("name") String name);

    @Delete("delete from User where id=#{id}")
    void deleteById(@Param("id") int id);
    
    logging:
      level:
        com.example.consumer.mapper: debug  # mybatis 开启debug 显示sql语句
        
        
        
## git 撤销最后的提交

    git log 命令查看最后几次的提交的commitid
    
    git reset --hard commitid1
    git reset --hard commitid2
    .....
    git reset --hard commitidn
    
    git push origin HEAD --force
    
    这样是暴力的撤销， 撤销的提交部分会被删除，所以撤销之前需要备份好文件
    

## mybatis 采用注解的方式

    参考SecuMainMapper ， SqlServerDbConfig  
    
## ehcache
    @Cacheable : 将方法的返回值放入到缓存中, 检查缓存中是否存在key , 如果不存在那么将值加入缓存
    @Cacheput : 将方法的返回值放入到缓存中, 不检查缓存中是否有对应的key
    @CacheEvict: 清除key 对应的缓存
        value ehcache 名称
        key: 缓存的键值
        condition: 触发的条件
        allEntries: 布尔值 是否全部清除  


```java
class Demo{
    @Cacheable(value = "models", key = "#testModel.name", condition = "#testModel.address !=  '' ")
    public TestModel getFromMem(TestModel testModel) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        testModel.setName(testModel.getName().toUpperCase());
        return testModel;
    }
    @CacheEvict(value = "models", allEntries = true)
    @Scheduled(fixedDelay = 10000)
    public void deleteFromRedis() {
    }
 
    @CacheEvict(value = "models", key = "#name")
    public void deleteFromRedis(String name) {
    }
    @CachePut(value = "models", key = "#name")
    public TestModel saveModel(String name, String address) {
        return new TestModel(name, address);
    }
}
```



## motan 轻量级的rpc
    参考了： https://github.com/fallsea/spring-boot-starter-motan
    
    
    代码：
    MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER,true);
    
    这段代码非常重要， 必须开启， 不然客户端无法发现服务端，客户端的端口一直是0；
    
    
## spring 事物
    mysqlTransactionManager返回的是JpaTransactionManager ， 不支持nested事物
    
    所以要测试Propagation.NESTED ， 需要选择DataSourceTransactionManager
    采用jdbcTemplate去持久化对象
    
    
    Propagation.REQUIRES_NEW : service A 采用Propagation.REQUIRED service B 采用Propagation.REQUIRES_NEW 
    在A的方法里面调用B的方法， 那么A方法开启一个事物， B开启一个全新的事物，是2个不同的事物；
    如果A方法抛异常，B不抛异常； A 回滚 ，B事物提交
    如果A方法抛异常，B抛异常； A 回滚 ，B回滚
    如果A方法不抛异常，B不抛异常； A  ，B事物提交
    如果A方法不抛异常，B抛异常；  B方法业务如果么有try, 那么A的事物会捕捉到B事物抛出的异常， A 方法内部如果对B
    方法进行try 那么  B 回滚 A不会滚； 如果A 对B方法不try那么 都回滚；
    
    Propagation.NESTED: 内部事物是外部事物的一个子事物； 内部事物会受到外部事物的影响， 及时内部运行的时候没有异常，如果外部有异常也会导致内部回滚
    
    
    
    Propagation.REQUIRES_NEW 和 Propagation.NESTED 
    当内部事物有异常的时候  如果外部不try catch , 外部，内部的时候都会回滚
    当外部有异常的时候REQUIRES_NEW 的外部异常不会影响内部的事物； NESTED 如果外部有异常的时候，内部事物，外部都会进行回滚
    
### docker
    http://blog.chinaunix.net/uid-27174191-id-5753087.html
    
    centos67.5 需要修改docker的文件才可以打开tcp
    修改配置文件：/etc/sysconfig/docker 添加以下参数：    
    other_args="-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock"
    
    
### spring dubbo 启动分析
    AbstractApplication.refresh()的时候
    
    
    try {
        // Allows post-processing of the bean factory in context subclasses.
        postProcessBeanFactory(beanFactory);
        
        // Invoke factory processors registered as beans in the context.
        invokeBeanFactoryPostProcessors(beanFactory);//DubboAutoConfiguration.serviceAnnotationBeanPostProcessor()

        // Register bean processors that intercept bean creation.
        registerBeanPostProcessors(beanFactory);//注册beanPostprocessor 扫描dubbo的@Service注解的bean

        // Initialize message source for this context.
        initMessageSource();

        // Initialize event multicaster for this context.
        initApplicationEventMulticaster();

        // Initialize other special beans in specific context subclasses.
        onRefresh();

        // Check for listener beans and register them.
        registerListeners();

        // Instantiate all remaining (non-lazy-init) singletons.
        finishBeanFactoryInitialization(beanFactory);

        // Last step: publish corresponding event.
        finishRefresh();//完成刷新的时候开始暴露服务 , publishEvent(new ContextRefreshedEvent(this));
        
    } catch (BeansException ex) {
        if (logger.isWarnEnabled()) {
            logger.warn("Exception encountered during context initialization - " +
                    "cancelling refresh attempt: " + ex);
        }

        // Destroy already created singletons to avoid dangling resources.
        destroyBeans();

        // Reset 'active' flag.
        cancelRefresh(ex);

        // Propagate exception to caller.
        throw ex;
    } finally {
        // Reset common introspection caches in Spring's core, since we
        // might not ever need metadata for singleton beans anymore...
        resetCommonCaches();
    }
    
    
    ServiceBean 继承了ServiceConfig 实现类SpringApplicationListener实现了onApplicationEvent方法--->export
    实现类InitializingBean 实现了afterPropertiesSet方法 设置属性完毕后调用该方法
    
    ServiceConfig.doExportUrls 服务暴露的主要的方法
    ServiceConfig.exportLocal 服务暴露到本地
    protocol.export(wrapperInvoker); 服务暴露到远程
        ProtocolListenerWrapper.export
        ProtocolFilterWrapper.export 
        QosProtocolWrapper export  启动 qos 服务
        RegistryProtocal.export 的时候注册服务到zookeeper  register(registryUrl, registedProviderUrl);
            doLocalExport 
                DubboProtocol.export
                    openServer-->createServer-->Exchangers.bind(url, requestHandler); 启动netty 绑定服务 为远程调用做准备
            register(registryUrl, registedProviderUrl); 注册信息到zookeeper
            registry.subscribe(overrideSubscribeUrl, overrideSubscribeListener);订阅服务 provider信息 
    
    
    dubbo 服务发现
    ReferenceBean继承了ReferenceConfig 实现了InitaializationBean 当bean的属性设置的时候调用afterPropertiesSet
    初始化consumer, application, module, registries,monitor--->ReferenceConfig.getObject() 
    ReferenceConfig.getObject()-->get()-->init()--ref = createProxy(map);
    referotocol.refer(interfaceClass, urls.get(0)); 最终会和服务注册一样的经过一系列的xxxWrapper
    ...
    在此过程中 会调用DubboProtocal的refer-->getClients-->AbstractClient的构造器-->doOpen() 开启netty客户端
    
    RegistryProtocol.refer
        registryFactory.getRegistry(url) 方法通过注册工厂来获取注册器 （zookeeper）
        doRefer 服务发现的业务
            创建RegistryDirectory对象 设置注册器， 协议， 订阅服务；
        
    
    loadbalance策略： 
    AbstractLoadBalance 抽象类
    RandomLoadBalance 随机策略
    RoundRobinLoadBalance 轮询策略
    LeastActiveLoadBalance 最少活跃的用户
    ConsistentHashLoadBalance 一致哈希 让相同的provider的请求 请求同一个节点
    
    
    dubbo 容错
    failover cluster : 失败重试 （默认） 调用服务的提供者失败后切换服务的提供者重试
    failfast cluster: 快速失败 立即报错 不重试
    failsafe cluster: 调用服务提供者的时候 有异常忽略
    forking cluster : 并行调用 调用多个服务提供方 只要有一个调用成功就返回
    broadcast cluster : 广播调用 逐个调用服务提供方  任何一台调用失败 此次的调用就失败；
    
    
    dubbo qos:
    centos控制台中连接qos  : telnet 10.1.51.96 33333 连接成功 可以使用ls  online offline 来显示 控制服务的上线和线性
    参考了https://segmentfault.com/a/1190000014520742
    
    
### jvm 参数
    -verbose:gc -verbose:class -verbose:jni -Xms32m -Xmx64m
    -verbose:gc 每次gc的情况
    -verbose:class jvm载入类的信息
    -verbose:jni jvm 调用jni的情况
    
    -Xms32m : 设置jvm的内存
    -Xmx64m : 设置jvm的最大内存
    
    -XX:+PrintGC 打印每次GC的情况
    -XX:+PrintGCDetails 打印每次GC的详细情况
    
    -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:ParallelGCThreads=4 -XX:+UseAdaptiveSizePolicy -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=1073741824 -XX:NewSize=1073741824 -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCTimeStamps
    

### 设置chrome 为默认的浏览器   
    1、开始→运行→输入regedit，打开注册表编辑器
    2、找到HKEY_CLASSES_ROOT\http\shell\open\command，在右边的窗口中双击"默认"，将要用浏览器的可执行文件的完全路径输入到这里，
    例如设置IE为默认浏览器：输入“C:\Program Files\Internet Explorer\iexplore.exe”
    3、然后找到 HKEY_CLASSES_ROOT\http\shell\open\ddeexec\Application，在右边的窗口中双击"默认"，设置浏览器名，
    如果是Firefox则输入Firefox，如果是IE则输入IExplore。（小提示：切记不能写错名称，如果你不知道浏览器的标准名称是什么，
    只需要到安装目录下查看该浏览器的名称即可）
    
    
    
    
    
      
    
    
    