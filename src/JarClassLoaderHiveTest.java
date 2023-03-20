

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Properties;

/**
 * description hive多版本自定义加载类测试
 *
 * @author Cyber
 * <p> Created By 2022/11/22
 * @version 1.0
 */
public class JarClassLoaderHiveTest {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {

        String jarUrl2 = "/Users/lifangyu/soft/driver-lib/hive-jdbc-2.1.1/"; //自己定义的测试jar包，不同版本打印内容不同
        /**
         * description lib/目录下的日子文件删除以防冲突
         * log4j-1.2-api-2.10.0.jar
         * log4j-api-2.10.0.jar
         * log4j-core-2.10.0.jar
         * log4j-slf4j-impl-2.10.0.jar
         * log4j-web-2.10.0.jar
         */
        String url = "jdbc:hive2://127.0.0.1:10001/ty";
        String user = "test";
        String pwd = "test";
        // String sql = "show databases";
        // String sql = "show tables";
        String sql = "select * from user_level_demo1 limit 10";
        testHiveJdbc(jarUrl2, url, user, pwd, sql);


        String jarUrl3 = "/Users/lifangyu/soft/driver-lib/hive-jdbc-3.1.2/";
        //自己定义的测试jar包，不同版本打印内容不同
        /**
         * description lib/目录下的日子文件删除以防冲突
         * log4j-1.2-api-2.10.0.jar
         * log4j-api-2.10.0.jar
         * log4j-core-2.10.0.jar
         * log4j-slf4j-impl-2.10.0.jar
         * log4j-web-2.10.0.jar
         */
        url = "jdbc:hive2://127.0.0.1:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
        user = "test";
        pwd = "test";
        String sql3 = "SELECT x.* FROM dc_dwa.dwa_d_bd_blend x where x.pro_id = 10 and x.contact_no='13009502690'";
        testHiveJdbc(jarUrl3, url, user, pwd, sql3);
    }

    private static void testHiveJdbc(String jarUrl, String url, String user, String pwd, String sql) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        long start = System.currentTimeMillis();
        JarClassLoader jarLoader = new JarClassLoader(new String[]{jarUrl});
        JarClassLoaderSwapper classLoaderSwapper = JarClassLoaderSwapper.newCurrentThreadClassLoaderSwapper();
        classLoaderSwapper.setCurrentThreadClassLoader(jarLoader);
        Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("org.apache.hive.jdbc.HiveDriver");
        classLoaderSwapper.restoreCurrentThreadClassLoader();

        Driver driver = (Driver) aClass.newInstance();
        Properties properties = new Properties();
        properties.put("user", user);
        properties.put("password", pwd);
        Connection conn = driver.connect(url, properties);
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        int col = rs.getMetaData().getColumnCount();//列数
        System.out.println("============================:" + jarUrl);
        while (rs.next()) {//一行一行输出
            for (int i = 1; i <= col; i++) {
                System.out.print(rs.getString(i) + "\t");//输出
                if ((i == 2) && (rs.getString(i).length() < 8)) {//输出制表符
                    System.out.print("\t");
                }
            }
            System.out.println("");
        }
        System.out.println("============================耗时：" + (System.currentTimeMillis() - start) + "ms");
    }
}