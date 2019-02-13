

# spring boot dubbo zookeeper
    项目的目的： 在spring boot 里面使用dubbo 和zookeeper
    

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