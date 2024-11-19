[TOC]



### oracle的基本使用—基本命令

#### 连接命令

```sql
1.conn
conn scott/tiger
conn system/oracle11g as sysdba
2.disc[onnect]	//断开与当前数据库的连接
3.passw[ord]	//该命令用于修改用户的密码
password
password scott(用户名)
4.show user
5.exit	//断开与数据库的连接，同时退出sql*plus
```

#### 文件操作命令

```sql
1.start和@	//运行sql脚本
@ d:\a.sql 
start d:\a.sql
2.edit	//编辑指定的sql脚本
edit d:\a.sql
3.spool	   //该命令可以将sql*plus屏幕上的内容输出到指定文件中去
spool d:\b.sql
spool off
```

#### 交互式命令

```sql
1.&		//可以代替变量，而该变量在执行时，需要用户输入
```

#### 显示和设置环境变量

```sql
1.linesize		//设置显示行的宽度,默认80个字符
set linesize 140;
2.pagesize		//设置每页现实的行数目，默认14（每页是查询时候的每页行数目）
set pagesize 20;
```

### 用户管理

```sql
1.创建用户，具有数据库管理员的权限才能使用
create uer 用户名 identified by m1234;	//如果第一位是数字，显示选项缺失或无效
2.修改密码
password 用户名;
alter user 用户名 identified by 新密码	//如果给别人修改密码则需要具有 dba 的权限，或是拥有 alter user 的系统权限 
3.删除用户
//一般以 dba 的身份去删除某个用户，如果用其它用户去删除用户则需要具
有 drop user 的权限
drop user 用户名 [cascade]
//如果要删除的用户，已经创建了表，那么就需要在删除的时候带一个参数
cascade; 
```

#### 使用profile管理用户口令

```sql
profile 是口令限制，资源限制的命令集合，当建立数据库的，oracle
会自动建立名称为 default 的 profile。当建立用户没有指定 profile 选项，那
么 oracle 就会将 default 分配给用户
create profile lock_account limit failed_login_attempts 3 password_lock_time 2;
alter user scott profile lock_account;
alter user scott account unlock;	//解锁
create profile myprofile limit password_life_time 10 password_grace_time 2;
alter user scott profile myprofile;
create profile password_history limit password_life_time 10 password_grace_time 2 password_reuse_time 10 password_reuse_time;//指定口令可重用时间，即10天后就可以重用
drop profile password_history [cascade];
```