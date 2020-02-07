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