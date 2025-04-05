## 简介
针对注入内存马可能存在的请求头长度过长的一种解决方法，原工具采用 rememberme 处写类加载逻辑，读取post参数。

改进之后在注入内存马时可选择是否进行分段注入，采用根据 Payload 长度进行分块写入系统属性，最后一次去读取拼接然后加载，每次发包长度约2k

![image-20250405182024140](https://aniale-blog.oss-cn-beijing.aliyuncs.com/blog/20250405182024203.png)