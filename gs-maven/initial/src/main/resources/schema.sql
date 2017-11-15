/*
Spring Boot可以自动创建数据源的模式（DDL脚本）并对其进行初始化（DML脚本）：它分别从标准根类路径位置schema.sql和data.sql中加载SQL。

此外，Spring Boot可以处理名称格式为schema-${platform}.sql和data-${platform}.sql文件（如果存在），其中platform是spring.datasource.platform属性的值，这允许你在必要时切换到数据库特定的脚本，例如
您可以选择将其设置为数据库的供应商名称（hsqldb，h2，oracle，mysql，postgresql等）。

Spring Boot默认启用Spring JDBC初始化程序的快速失败功能，所以如果脚本导致异常，应用程序将无法启动。
你可以使用spring.datasource.continue-on-error来调整它

您也可以通过将spring.datasource.initialize设置为false来禁用初始化
*/
drop table BOOKINGS if exists;
create table BOOKINGS(ID serial, FIRST_NAME varchar(5) NOT NULL);