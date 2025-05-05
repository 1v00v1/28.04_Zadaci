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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static konzolnaAplikacija.DatabaseConnection.createDataSource;

public class Main {
    public static void main(String[] args) {
        Boolean izlaz = false;
        DataSource ds = createDataSource();
        List<String> države = ispisDržava(ds);
        Scanner s = new Scanner(System.in);
        do {
            printOptions();
            System.out.print("Vaš izbor: ");
            try {


                switch (Integer.parseInt(s.nextLine().trim())) {

                    case 1 -> unosDržave(s, ds, države);
                    case 2 -> izmjenaDržave(s, ds);
                    case 3 -> brisanjeDržave(s, ds);
                    case 4 -> ispisDržava(ds);
                    case 5 -> izlaz = true;

                }

            } catch (NumberFormatException e) {
                System.out.println("Krivi unos. Pokušajte ponovo");
            }
        } while (!izlaz);


    }

    private static List<String> ispisDržava(DataSource ds) {
        List<String> l = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {

            Statement s = connection.createStatement();
            System.out.println("Popis Država: ");
            ResultSet rs = s.executeQuery("SELECT Naziv FROM Drzava ORDER BY Naziv");
            while (rs.next()) {
                System.out.printf("%s, ", rs.getString("Naziv"));
                l.add(rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    private static void brisanjeDržave(Scanner s, DataSource ds) {
        try (Connection connection = ds.getConnection()) {
            Statement st = connection.createStatement();
            System.out.print("Unesite ime države koju želite obrisati: ");
            String drzava = s.nextLine().trim();


            String query = String.format("DELETE FROM Drzava WHERE Naziv = '%s' AND IdDrzava >3", drzava);
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

    private static void izmjenaDržave(Scanner s, DataSource ds) {
        Boolean ex = false;
        String drzava;
        try (Connection connection = ds.getConnection()) {


            Statement st = connection.createStatement();

            System.out.print("Unesite ime države koju želite izmjeniti: ");
            drzava = s.nextLine().trim();
            ResultSet rs = st.executeQuery("SELECT Naziv FROM Drzava");
            while (rs.next()) {
                System.out.println(rs.getString("Naziv"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void unosDržave(Scanner s, DataSource ds, List<String> l) {


        try (Connection connection = ds.getConnection()) {
            Statement st = connection.createStatement();


            System.out.print("Unesite Ime Države: ");
            String naziv = s.nextLine().trim();
            if (l.contains(naziv)) {
                System.out.println("Država već postoji");

            } else {

                String unos = String.format("INSERT INTO Drzava (Naziv) VALUES ('%s')", naziv);
                st.execute(unos);
                System.out.println("Nova Država Unesena");

                st.close();
                s.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
