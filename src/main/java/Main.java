import java.io.*;
import java.sql.*;
import java.util.Properties;

public class Main {

    private String url;
    private String user;
    private String password;

    public Main(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    protected String getUrl() {
        return url;
    }

    protected String getUser() {
        return user;
    }

    protected String getPassword() {
        return password;
    }

    public Connection ConnexionBDD() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public ResultSet SelectDB(Connection connection) {
        String sql = "select * from testjdbcdb.personne";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            try {
                rs = statement.executeQuery(sql);
                while(rs.next()) {
                    System.out.println(rs.getString("prenom")+" "+rs.getString("nom")+" né le "+rs.getString("dateNaissance"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    public void Create(Connection connection, Integer id, String nom, String prenom, String dateNaissance) {
        String sql = "insert into testjdbcdb.personne (id, nom, prenom, dateNaissance) values (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setString(4, dateNaissance);
            try {
                statement.executeQuery();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Update(Connection connection, Integer id, String nomColonne, String valeur) {
        String sql = "UPDATE testjdbcdb.personne set "+nomColonne+"='"+valeur+"' where id="+id+"";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            try {
                statement.executeQuery();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Delete(Connection connection, Integer id) {
        String sql = "DELETE from testjdbcdb.personne where id='"+id+"'";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            try {
                statement.executeQuery(sql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        /* Création d'un fichier parameter.properties
        Properties p = new Properties();
        try {
            OutputStream os = new FileOutputStream("parameter.properties");
            p.setProperty("url","");
            p.setProperty("user","");
            p.setProperty("password","");
            p.store(os, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        /* Récupération des paramètres stockés dans le fichier properties */
        Properties p = new Properties();
        InputStream is = new FileInputStream("parameter.properties");
        p.load(is);

        Main bdd = new Main(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
        /* Test du CRUD */
        System.out.println("\n================== TEST DU CRUD ==================\n\nListing des données de la table personne :\n----------------------------------------------------");
        bdd.SelectDB(bdd.ConnexionBDD());
        System.out.println("\nCréation de la personne Kilian :\n----------------------------------------------------");
        bdd.Create(bdd.ConnexionBDD(), 5,"POITOU", "Kilian", "1999-05-06");
        bdd.SelectDB(bdd.ConnexionBDD());
        System.out.println("\nMise à jour de la personne Kilian (prenom = Kilian -> BloodShadow) :\n----------------------------------------------------");
        bdd.Update(bdd.ConnexionBDD(), 5, "prenom","BloodShadow" );
        bdd.SelectDB(bdd.ConnexionBDD());
        System.out.println("\nSuppression de la personne Kilian :\n----------------------------------------------------");
        bdd.Delete(bdd.ConnexionBDD(), 5);
        bdd.SelectDB(bdd.ConnexionBDD());
    }
}
