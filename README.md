# Nginx configuration Java parser

>
> yswang 修改
> 
> 2025/6/14 - 调整 `Nginx.g4` 语法文件, 使用`antlr4-runtime-v4.13.1` 进行重新编译.
> 
> 编译方法:
>
> 1. IDEA安装 `ANTLR v4` 插件.
>
> 2. 右键 `resources/grammar/Nginx.g4` 文件选择 `Configure ANTLR...` 菜单进行参数配置:
> 
>   'Output directory where all output is generated: ' - 选择到 `src/main/java` 目录.
>   
>   'Package/namespace for the generated code: ' - 输入: `com.github.odiszapc.nginxparser.antlr`.
>   
>   'Language: ' - 输入: `Java`.
>
> 3. 上述配置完成后, 右键 `resources/grammar/Nginx.g4` 文件选择 `Generate ANTLR Recognizer` 菜单执行代码生成.
>
> 4. 生成的新代码会覆盖 `com.github.odiszapc.nginxparser.antlr` 下的旧代码.
>
> 5. 检查 `com.github.odiszapc.nginxparser.antlr` 下的每个 java 文件, 修复某些类头部出现重复 `package com.github.odiszapc.nginxparser.antlr;` 问题.
>
> 6. OK, 可以maven打包了.
> 

This library helps in analyzing Nginx web server configuration files, looking up for specified parameters, blocks, regular expressions or comments. Then AST can be modified and converted back to plain file.

#### Features
- Convert config file to AST tree using ANTLR4 parsing capabilities
- The same is available for JavaCC too (deprecated)
- Rebuild config files and dump them back to *.conf
- Nested blocks support
- If statements support
- Unquoted regexp within location/rewrite/if statements support
- Comments support

#### Installation
Add the following dependency to your POM:
```xml
<dependency>
    <groupId>com.github.odiszapc</groupId>
    <artifactId>nginxparser</artifactId>
    <version>0.9.3</version>
</dependency>
```

#### Examples
##### Parser

How to perform basic parsing of the following Nginx config:
```java
NgxConfig conf = NgxConfig.read("/etc/nginx/nginx.conf");
NgxParam workers = conf.findParam("worker_processes");       // Ex.1
workers.getValue(); // "1"
NgxParam listen = conf.findParam("http", "server", "listen"); // Ex.2
listen.getValue(); // "8889"
List<NgxEntry> rtmpServers = conf.findAll(NgxConfig.BLOCK, "rtmp", "server"); // Ex.3
for (NgxEntry entry : rtmpServers) {
    ((NgxBlock)entry).getName(); // "server"
    ((NgxBlock)entry).findParam("application", "live"); // "on" for the first iter, "off" for the second one
}
```

/etc/nginx/nginx.conf:
```
worker_processes  1;            # <- Ex.1

http {
    server {
        listen       8889;      # <- Ex.2
        server_name  localhost;
    }
}

rtmp {
    server {                    # <- Ex.3 (first)
        listen 1935;
        application myapp {
            live on;
        }
    }

    server {                    # <- Ex.3 (second)
        listen 1936;
        application myapp2 {
            live off;
        }
    }
}
```

##### Dumper

```java
NgxConfig conf = NgxConfig.read("/etc/nginx/nginx.conf");
// ...
NgxDumper dumper = new NgxDumper(conf);
return dumper.dump(System.out);
```

#### Authors
Alexey Plotnik (odiszapc@gmail.com, http://twitter.com/odiszapc, http://alexey-plotnik.me) I do it just because I like it.



#### License
Apache 2.0
