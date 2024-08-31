# 说明

- CentOS 7.9

# 配置文件

编辑`/etc/profile`, 添加以下文件内容

```textile
http_proxy=socks5://192.168.1.4:10808
https_proxy=socks5://192.168.1.4:10808
export http_proxy
export https_proxy
```

`http_proxy`和`https_proxy`配置协议、ip、端口由实际代理软件配置与代理软件所在主机ip决定

# 使代理配置生效

执行`source /etc/profile`

```textile
source /etc/profile
```

# 测试

执行curl命令响应码为200则配置成功

```bash
curl -I www.google.com
```


