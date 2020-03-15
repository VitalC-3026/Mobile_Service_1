import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDAO {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/javahw?&serverTimezone=Asia/Shanghai";
    PreparedStatement pstmt = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    /*/DatabaseUtil(){
        try {
            init();//这种作用到底能不能完成我在调用getconnection之前初始化
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* private void init() throws Exception{
        Properties params = new Properties();
        String configFile = "database.properties";
        InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream(configFile);
        try{
            assert is != null;
            params.load(is);
        }catch(IOException e){
            e.printStackTrace();
            is.close();
            //is是空，还能进行关闭吗
        }
        try {
            driver = params.getProperty("driver");
            url = params.getProperty("url");
            user = params.getProperty("user");
            password = params.getProperty("password");
        }catch(Exception e){
            e.printStackTrace();
            //如果出现配置文件中没有参数怎么办
        }
        try{
            Class.forName(driver);
        }catch (Exception e){
            e.getMessage();
        }
        try{
            con = DriverManager.getConnection(url,user,password);
            stmt = con.createStatement();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }*/
    int executeUpdate(String prepareSQL, Object[] objects) throws Exception {
        getConnection();
        int count = 0;
        pstmt = con.prepareStatement(prepareSQL);
        try {
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    pstmt.setObject(i + 1, objects[i]);
                }
                count = pstmt.executeUpdate();
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Update Failed!");
            return 0;
        }
        return count;
    }

    ResultSet executeQuery(String prepareSQL, Object[] objects) throws Exception {
        getConnection();
        ResultSet rs = null;
        pstmt = con.prepareStatement(prepareSQL);
        try {
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    pstmt.setObject(i + 1, objects[i]);
                }
                rs = pstmt.executeQuery();
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Update Failed!");
            return null;
        }
        return rs;
    }

    Statement getConnection()throws Exception{
        try{
            Class.forName(JDBC_DRIVER);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to build driver!");
            return null;
        }
        try{
            con = DriverManager.getConnection(DB_URL,"root", "Nku1811499");
            stmt = con.createStatement();
        }catch(Exception e){
            e.printStackTrace();
            stmt = null;
        }
        return stmt;
    }
    void closeAll() throws SQLException {
        try {
            if(pstmt != null){
                pstmt.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if(con != null){
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}

