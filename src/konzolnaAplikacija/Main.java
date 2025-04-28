package konzolnaAplikacija;


/*TODO
*  Napiši Java konzolnu aplikaciju sa sljedećim funkcionalnostima:
* Korisniku se prikazuje izbornik sa sljedećim opcijama:
1 – nova država
2 - izmjena postojeće države
3 - brisanje postojeće države
4 – prikaz svih država sortiranih po nazivu
5 – kraj

Opcije 1 do 4 odnose se na CRUD operacije and tablicom Drzava u bazi AdventureWorks
Odabirom opcije 2 i 3, od korisnika je potrebno tražiti ID države koje želite izmijeniti ili pobrisati
Napomena: brišite i mijenjajte samo one države koje ste Vi ubacili (one za koje je IdDrzava veći od 3)*/

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static konzolnaAplikacija.DatabaseConnection.createDataSource;

public class Main {
    public static void main(String[] args) {
        Boolean izlaz = false;
        DataSource ds = createDataSource();
        Scanner s = new Scanner(System.in);
        do {
            printOptions();
            System.out.print("Vaš izbor: ");
            int izbor = Integer.parseInt(s.nextLine().trim());
            switch (izbor) {
                case 1 -> unosDržave(s, ds);
                case 2 -> izmjenaDržave(s,ds);
                case 3 -> brisanjeDržave(s,ds);
                case 4 -> ispisDržava(ds);
                default -> izlaz = true;
            }


        } while (!izlaz);

    }

    private static void ispisDržava(DataSource ds) {
        try (Connection connection = ds.getConnection()){
            Statement s = connection.createStatement();
            System.out.println("Popis Drđava: ");
            ResultSet rs =s.executeQuery("SELECT Naziv FROM Drzava ORDER BY Naziv");
            while (rs.next()){
                System.out.printf("%s, ",rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void brisanjeDržave(Scanner s, DataSource ds) {
        try (Connection connection = ds.getConnection()){
            Statement st = connection.createStatement();
            System.out.print("Unesite ime države koju želite obrisati: ");
            String drzava = s.nextLine().trim();


            String query = String.format("DELETE FROM Drzava WHERE Naziv = '%s' AND IdDrzava >3",drzava);
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printOptions() {
        System.out.println("\n");
        System.out.println("""
                1 – nova država
                2 - izmjena postojeće države
                3 - brisanje postojeće države
                4 – prikaz svih država sortiranih po nazivu
                5 – kraj""");

    }
    private static  void izmjenaDržave(Scanner s, DataSource ds){
        try (Connection connection = ds.getConnection()){
            Statement st = connection.createStatement();
            System.out.print("Unesite ime države koju želite izmjeniti: ");
            String drzava = s.nextLine().trim();
            System.out.print("Unesite novu vrijednost: ");
            String novaVrijednost = s.nextLine().trim();
            String query = String.format("UPDATE Drzava SET Naziv ='%s' WHERE Naziv = '%s'",novaVrijednost,drzava);
            st.executeUpdate(query);
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void unosDržave(Scanner s, DataSource ds) {


        try (Connection connection = ds.getConnection()) {
            Statement st = connection.createStatement();


            System.out.print("Unesite Ime Države: ");
            String naziv = s.nextLine().trim();

            String unos = String.format("INSERT INTO Drzava (Naziv) VALUES ('%s')", naziv);
            st.execute(unos);
            System.out.println("Nova Država Unesena");

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
