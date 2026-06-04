package database;

import java.sql.*;

public class DBConnection {
    public Connection con;
    String url="jdbc:postgresql://localhost:5432/prisonmanagementdb";
    String password ="admin123";
    String user = "postgres";
    
    public DBConnection(){
        con = getconnections();
    }
   public final  Connection getconnections(){
       //load driver
       Connection con = null;
       try{
        Class.forName("org.postgresql.Driver");
        System.out.println("load driver success");
       }catch (ClassNotFoundException cnfe){
           System.out.println("load driver failed"+ cnfe.getMessage());
       }
       //establishhhhhhhh the connection 
       try{
           con  = DriverManager.getConnection(url, user, password);
           System.out.println("driver loaded success");
       }
<<<<<<< HEAD
       catch(SQLException sqle) {
    System.out.println("driver loadeer failed "+ sqle.getMessage())
            ;
    
}
       
=======
       catch(SQLException sqle){
           System.out.println("driver loadeer failed "+ sqle.getMessage());
       }
>>>>>>> b3dd21e08003f196dcab0d220dd9841ba0f7f145
       return con;
   }

}
