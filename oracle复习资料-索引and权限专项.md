#### 索引

（知道就行，估计期末不涉及）

单列索引

```sql
create index 索引名 on 表名(列名);
```

复合索引

```sql
create index 索引名 on 表明(列名1,列名2,...);
```

#### 权限

分为系统权限和对象权限

系统权限

常用的：

create session 连接数据库

create table 建表

create view 建视图

create procedure 建过程、函数、包

授予系统权限是由 dba 完成的，如果用其他用户来授予系统权限， 则要求该用户必须具有 grant any privilege 的系统权限。在授予系统权限时， 可以带有 with admin option 选项，这样，被授予权限的用户或是角色还可以将 该系统权限授予其它的用户或是角色。

一般情况下，回收系统权限是 dba 来完成的，如果其它的用户来回收系统权限， 要求该用户必须具有相应系统权限及转授系统权限的选项（with admin option）。 回收系统权限使用 revoke 来完成。 

系统权限不是级联回收！！

```sql
with admin option;
```

对象权限

指访问其它方案对象的权利，用户可以直接访问自己方案的对象，但是如果要 访问别的方案的对象，则必须具有对象的权限。

常用的有：

alter 修改 delete 删除 select 查询 insert 添加 

update 修改 index 索引 references 引用 execute 执行 

对象权限可以授予用户，角色，和 public。在授予权限时，如果带有 with grant  option 选项，则可以将该权限转授给其它用户。但是要注意 with grant option 选项不能被授予角色。

```sql
grant select on emp to A;
grant update on emp to A;
grant delete on emp to A;
grant all on emp to A;
grant update on emp(sal) to A;
grant select on emp(ename,sal) to A;
grant alter on emp to A;
grant execute on dbms_trasaction to ken;
grant index on scott.emp to A;
with grant option;
```

级联回收