import java.sql.*;

public class oracleTest {
    public static void main(String[] args) {
        try{
            //1.加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //2.得到连接
            Connection ct = DriverManager.getConnection("jdbc:oracle:thin:@//Wneosy:1523/oracle","scott","tiger");
            //3.创建CallableStatement
            CallableStatement cs = ct.prepareCall("{call scott.get_employee_info(?)}");
            //4.给?赋值
            cs.setInt(1,10);
            //5.执行
            cs.execute();
            //6.关闭
            cs.close();
            ct.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

