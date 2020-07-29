import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/* Classe permettant de tester les fonctionnalités du ResultSet modifiable */
public class ScrollResultSet {

    /* main() permettant de tester les méthodes de la classe ResultSet */
    public static void main(String[] args) throws IOException, SQLException {
        Properties p = new Properties();
        InputStream is = new FileInputStream("parameter.properties");
        p.load(is);
        Main bdd = new Main(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
        ResultSet rs = bdd.SelectDB(bdd.ConnexionBDD());

        /* ---------- Appel des méthodes de la classe ResultSet non modifible ---------- */
        /* Appel du premier record */
        rs.first();
        System.out.println("\nPREMIER RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* Appel du troisieme record */
        rs.absolute(3);
        System.out.println("\nTROISIEME RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* Appel du dernier record */
        rs.last();
        System.out.println("\nDERNIER RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* Appel de l'avant dernier record */
        rs.previous();
        rs.relative(-1);
        System.out.println("\nAVANT DERNIER RECORD RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* ---------- Appel des méthodes de la classe ResultSet modifible (notamment du CRUD) ---------- */

        /* Create avec le ResultSet*/
        rs.absolute(5);
        rs.moveToInsertRow();
        rs.updateInt (1, 5);
        rs.updateString (2, "POITOU");
        rs.updateString (3, "Kilian");
        rs.updateString (4, "1999-01-01");
        rs.insertRow();
        rs.last();
        System.out.println("\nCREATE personne : prenom = Kilian...");
        System.out.println("DERNIER RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* Update avec le ResultSet */
        rs.absolute(5);
        rs.updateString (3, "BloodShadow");
        rs.updateRow();
        System.out.println("\nMISE À JOUR DU PRENOM DE LA PERSONNE KILIAN POITOU :");
        rs.last();
        System.out.println("DERNIER RECORD...");
        System.out.println(rs.getInt(1) + " -> " + rs.getString("prenom") + " " + rs.getString("nom") + " né le " + rs.getString("dateNaissance"));

        /* Delete avec le ResultSet */
        rs.absolute(5);
        rs.deleteRow();
        System.out.println("\nSUPPRESSION DE LA PERSONNE KILIAN POITOU :");
        System.out.println("LISTING DE LA TABLE...");
        bdd.SelectDB(bdd.ConnexionBDD());
    }
}
