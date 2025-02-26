package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String url = "jdbc:mysql://localhost:3306/gymtowndb";
    public final String user = "root";
    public final String mdp = "";
    private Connection cnx;
    private static MyDataBase myDataBase;

    private MyDataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, user, mdp);
            System.out.println("Connexion établie");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    public static MyDataBase getInstance() {
        if (myDataBase == null)
            myDataBase = new MyDataBase();
        return myDataBase;

    }

    public Connection getCnx() {
        return cnx;
    }
}
