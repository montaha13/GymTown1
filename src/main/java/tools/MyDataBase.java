package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public static final String url = "jdbc:mysql://localhost:3306/gymtowndb";
    public static final String user = "root";
    public static final String mdp = "";


    private Connection cnx;
    private static MyDataBase myDataBase;

    private MyDataBase () {
        try {
            cnx = DriverManager.getConnection(url, user, mdp);
            System.out.println("cnx établie");
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }}
        public static MyDataBase getInstance() {
        if (myDataBase==null)
            myDataBase=new MyDataBase();
            return myDataBase;
        }


    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver JDBC pour MySQL (si nécessaire)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Retourner la connexion à la base de données
            return DriverManager.getConnection(url , user , mdp);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Le driver JDBC pour MySQL n'a pas été trouvé", e);
        }
    }


    public Connection getCnx() {
        return cnx;
    }

}

