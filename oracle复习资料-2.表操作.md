### oracle表的管理

#### 数据类型

```sql
char	定长，最大2000字符
varchar2 变长，最大4000字符
clob	字符型大对象，最大4G
数字型
number	范围-10的38次方到10的38次方
可以表示整数也可以表示小数
number(5,2) 表示一个小数有5位有效数，2位小数
number(5)	表示一个5位整数
date	1-1月-1999
blob	二进制数据，可以存放图片/声音
```

#### 表创建删除

```sql
//创建表
create table student(
	xh number(4),
    xm varchar2(20),
    sex char(2),
    birthday date,
    sal number(7,2)
);
//修改表
alter table student add(classId number(2));
alter table student modify (xm varchar2(30));//修改字段的类型/名字（不能有数据），不建议做
alter table student drop column sal;//删除列，不建议做，顺序变
rename student to stu;//修改表的名字
drop table stu;//删除表
```

<img src="https://gitee.com/Black_aura/picture/raw/master/img/image-20241119203006885.png" alt="image-20241119203006885" style="zoom: 50%;" />

#### 数据操作

```sql
//插入操作
insert into student values('A001','张三'，‘01-5月-05’,10);
oracle 中默认的日期格式‘dd-mon-yy’ dd 日子（天） mon 月份 yy 2 位的
年 ‘09-6 月-99’ 1999 年 6 月 9 日 
alter session set nls_date_format = 'yyyy-mm-dd';
insert into student (xh,xm,sex) values('A004','JOHN','女');
select * from student where birthday is null;
//修改操作
update student set sex='女' where xh = 'A001';
update student sex sex='男',birthday='1984-04-01' where xh='A001';
无论是查询还是修改含有null值得数据，不要用=null而是用is null;
//删除数据
delete from student;
删除所有记录，表结构还在，写日志，可以恢复的，速度慢。 
Delete 的数据可以恢复。 
savepoint a; --创建保存点 
DELETE FROM student; 
rollback to a; --恢复到保存点 
```

### 表查询

<img src="https://gitee.com/Black_aura/picture/raw/master/img/image-20241119204929016.png" alt="image-20241119204929016" style="zoom: 50%;" />

```sql
desc emp;	//查看表结构
insert into users (userId,UNAME,UPASSW) select * from users;
select count(*) from users;
select distinct deptno from emp;	//取消重复行
oracle 对内容的大小写是区分的，所以 ename='SMITH'和 ename='smith'
是不同的 
select sal*12+nvl(comm,0)*12 "年薪",ename,comm from emp;
NVL 是 Oracle 数据库中的一个函数，用于处理空值（NULL）。当某个列或表达式的值为 NULL 时，NVL 会将其替换为指定的值。
别名
select ename "姓名",sal*12 as "年收入" from emp;
后面直接或者as都可以起别名
连接字符串
select ename || ' is a ' || job from emp;
```

<img src="D:/tyrecording/assets/image-20241119211344524.png" alt="image-20241119211344524" style="zoom: 67%;" />

```sql
eg1:如何显示首字符为S的员工姓名和工资？
select ename,sal from emp where ename like 'S%';
eg2:如何显示第三个字符为大写 O 的所有员工的姓名和工资？ 
select ename,sal from emp where enmame like '__0%'
eg3:查询工资高于 500 或者是岗位为 MANAGER 的雇员，同时还要满足他们的姓
名首字母为大写的 J？ 
 select * from emp where (job = 'MANAGER' or sal > 500) and ename like 'J%';
eg4:按年薪排序 
select ename,(sal+nvl(comm,0))*12 ysal from emp order by ysal asc;//中文需要”“圈中，英文不需要
```

数据分组—max,min,avg,sum,count

问题：如何显示所有员工中最高工资和最低工资？

```
select max(sal),min(sal) from emp;
```

问题：最高工资那个人是谁？

```sql
select ename,sal from emp where sal=max(sal);//错误写法
select ename,sal from emp where sal=(select max(sal) from emp);//正确写法
select ename,max(sal) from emp;这语句执行的时候会报错，如果列里面有一个分组函数，其它的都必须是分组函数，否则就出错。这是语法规定的
```

group by 和 having子句

group by用于对查询的结果分组统计

having子句用于限制分组显示结果

问题：如何显示每个部门的平均工资和最高工资？

```sql
select avg(sal),max(sal),deptno from emp group by deptno;
```

如果你要分组查询的话，分组的字段 deptno 一定要 出现在查询的列表里面，否则会报错。因为分组的字段都不出现的话，就没办法 分组了

问题：显示每个部门的每种岗位的平均工资和最低工资？ 

```sql
select min(sal),avg(sal),deptno,job from emp group by deptno,job;
```

<img src="https://gitee.com/Black_aura/picture/raw/master/img/image-20241120161336186.png" alt="image-20241120161336186" style="zoom: 50%;" />

问题：显示部门号为 10 的部门名、员工名和工资？ 

```sql
select emp.deptno,ename,sal from emp,dept where dept.deptno=emp.deptno and emp.deptno = 10;
```

问题：显示各个员工的姓名，工资及工资的级别？

```sql
select ename,sal,grade from emp,salgrade where sal between salgrade.losal and salgrade.hisal;
```

问题：显示雇员名，雇员工资及所在部门的名字，并按部门排序？ 

```sql
select ename,sal,dname from emp,dept where dept.deptno = emp.deptno order by emp.deptno;
```

问题：显示某个员工的上级领导的姓名？

```sql
select worker.ename,boss.ename from emp worker,emp boss where worker.mgr=boss.empno and worker.ename='FORD';
```

问题：如何查询和部门 10 的工作相同的雇员的名字、岗位、工资、部门号 

```sql
select ename,job,sal,deptno from emp where job in(select distinct job from emp where deptno = 10);
```

问题：如何显示工资比部门 30 的所有员工的工资高的员工的姓名、工资和部门号？

```sql
select ename,sal,deptno from emp where sal > (select max(sal) from emp where deptno = 30);
SELECT ename, sal, deptno FROM emp WHERE sal > all (SELECT sal FROM emp 
WHERE deptno = 30);
```

问题：查询与 SMITH 的部门和岗位完全相同的所有雇员。

```sql
select deptno,ename from emp where (deptno,job) = (select deptno,job from emp where ename = 'SMITH');
```

**问题：如何显示高于自己部门平均工资的员工的信息**

```sql
select emp.ename,emp.deptno,emp.sal,ds.mysal from emp,(select deptno,avg(sal) mysal from emp group by deptno) ds where emp.deptno = ds.deptno and emp.sal > ds.mysal;
```

解释：这里嵌套内的查询看似违背了分组函数和普通列，但尝试发现只要在group by里就可以执行

当在from子句中使用子查询，该子查询会被作为一个视图来对待，因此叫做内嵌视图，当在from子句中使用子查询，必须给子查询指定别名！！

给表取别名时候，不能加as，但给列取别名，是可以加as的

#### 分页查询

##### oracle的分页一共有三种方式

1.根据rowid来分

2.根据分析函数来分

**3.按rownum来分**

（前两种没讲）

问题：挑选出6—10条雇员的编号和工资记录

```sql
select * from (select e.*,rownum rn from (select ename,sal from emp) e where rownum
<=10) where rn >=6;
```

排序查询，只需要修改最里层的子查询 ，工资排序后查询 6-10 条数据 

```sql
select * from (select e.*,rownum rn from (select ename,sal from emp order by sal) e where rownum<=10) where rn >=6;
```

用查询结果创建新表

```sql
create table mytable(id,name,sal,job,deptno) as select empno,ename,sal,job,deptno from emp;
```

#### 合并查询

有时在实际应用中，为了合并多个select语句的结果，可以使用集合操作符号union,union all,intersect,minus

1.union

该操作符用于取得两个结果集的并集。当使用该操作符时，会自动去掉结果集中重复行。

```sql
select ename,sal,job from emp where sal > 2000 union select ename,sal,job from emp where job = 'CLERK';
```

使用 `UNION` 时，合并的结果集会自动去除重复行，并且默认情况下会对结果集进行排序。这意味着 `UNION` 操作会将来自各个 `SELECT` 语句的结果行合并，并去除重复的行，然后对最终的结果集进行排序。排序的依据是第一个 `SELECT` 语句中指定的列的顺序。

<img src="https://gitee.com/Black_aura/picture/raw/master/img/image-20241120175809700.png" alt="image-20241120175809700" style="zoom: 50%;" />

2.union all

该操作符与union相似，但是它不会取消重复行，而且不会排序

<img src="https://gitee.com/Black_aura/picture/raw/master/img/image-20241120175909653.png" alt="image-20241120175909653" style="zoom: 50%;" />

使用 `UNION ALL` 时，合并的结果集不会去除重复行，并且不会自动对结果集进行排序。这意味着 `UNION ALL` 操作只是简单地将来自各个 `SELECT` 语句的结果行合并，不会进行任何排序。

3.intersect

使用该操作符用于取得两个结果集的交集

```sql
select ename,sal,job from emp where sal > 1000 intersect select ename,sal,job from emp where job = 'MANAGER';
```

4.minus

使用改操作符用于取得两个结果集的差集，他只会显示存在第一个集合中，而不 存在第二个集合中的数据。 

```sql
select ename,sal,job from emp where sal > 1000 minus select ename,sal,job from emp where job = 'MANAGER';
```

