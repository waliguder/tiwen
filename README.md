## 码匠社区 项目

## 提问社区

##资料
####git使用


```
git add .   // 将项目中修改的文件加入提交目录
git commit -m "描述"  //将项目文件提交
git push  //确认提交
```

####flyway使用
######flay way介绍：一个数据库脚本版本控制器，帮助多人开发对数据库脚本进行集合整理。
######使用步骤：
```sql
//首先在maven中添加依赖
<project xmlns="...">
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-maven-plugin</artifactId>
                <version>6.2.2</version>
                <configuration>
                    <url>jdbc:h2:file:./target/foobar</url>
                    <user>sa</user>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>com.h2database</groupId>
                        <artifactId>h2</artifactId>
                        <version>1.4.197</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>

//确保本地是没有数据库的
//此工具对接h2数据库
//再创建脚本迁移目录src/main/resources/db/migration/V1__Create_person_table.sql
//在上面那个目录的sql文件中写好数据库脚本文件


//最后再terminal中运行mvn flyway:migrate
//它会自动的创建一个h2数据库和其中的表格
```

### 开发流程总结
```sql
    1，先确定一个页面路径，比如文章发布页面publish，主体对象是question，
    2，你就要开始写一个controller，叫publishController，定义好前端模版publish.html
       在controller中定一个访问方法，里面用RequestParam接收页面form表单传送过来的数据
       用Model会传给前端页面
    3.在controller中要对数据进行增删查改操作的话就需要写一个questionMapper，用Autowired注入即可
    5.在使用questionMapper之前必须先写一个question实例对象（放在model中），用来携带form表单传送来的数据到questionMapper中
    4.在questionMapper中定义一个方法接收一个question实例，然后再方法前面添加一个@inser（插入）或者@select（查询）的注解，在后面的括号中写sql语句
基本的开发流程就是这样，详细的看代码即可。
```