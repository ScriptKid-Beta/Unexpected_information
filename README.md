# Unexpected_information
Unexpected information Extensions

最近写了一个BurpSuite Extensions用来标记请求包中的一些敏感信息、JS接口和一些特殊字段，防止我们疏忽了一些数据包，我将它命名为“Unexpected information”，使用它可能会有意外的收获信息。

#### 介绍

##### 支持列表

- [x] 身份证信息
- [x] 手机号信息
- [x] IP信息
- [x] 邮箱信息
- [x] JS文件API接口路径
- [x] 特殊字段(password、method: "post"...)
- [x] 双向检测
- [x] 高亮显示

##### 高亮模式

```
邮箱 -> 黄色
内网IP -> 红色
手机号码 -> 绿色
身份证号码 -> 绿色
其他 -> 无 (只开启Unexpected information标签页)
```

当如数据包中存在有相关的对应信息如(手机号码、IP地址、邮箱、身份证号码等)存在时HTTP history标签页中的对应请求中自动标记颜色高亮，并且开启一个新的标签页名为”Unexpected information”显示匹配到的信息。

#### 如何使用

```
BurpSuite >> Extender >> Extensions >> Add >> Extension type: Java >> Select file ...>> 选择对应的插件(Unexpected information.jar)
注意：避免使用中文目录
```

![image-20200913144353237](/image-20200913144353237.png)

#### 效果

![image-20200913151710096](/image-20200913151710096.png)

![image-20200913152201413](/image-20200913152201413.png)

#### 项目地址

```
https://github.com/ScriptKid-Beta/Unexpected_information
```

#### 最后

欢迎师傅star，最重要的是如果师傅们有什么建议或者Bug，请在ISSUES里提出来或者公众号留言。
