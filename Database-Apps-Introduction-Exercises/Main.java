
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static Properties properties;
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1111");

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        //2. Get Villains’ Names
        //  getVilliansNames();

        //3. Get Minion Names
        //  getMinionsNames();

        //4. Add Minion
        //  addingMinion();

        //5. Change Town Names Casing
        //  changeTownName();

        //6. Remove Villain
        //  removeVillianIfExist();

        //7. Print All Minion Names
        //    printingMinionsName();

        //8. Increase Minions Age
        //  updateMinionAge();

        //9. Increase Age Stored Procedure
        // increaseAgeWithProcedure();

    }

    private static void increaseAgeWithProcedure() throws SQLException {
        int minionId = Integer.parseInt(scanner.nextLine());
        CallableStatement ps = connection.prepareCall("CALL usp_get_older(?);");
        ps.setInt(1, minionId);
        ps.executeQuery();
        printNameAndAgeMinionWithCurrentId(minionId);
    }

    private static void printNameAndAgeMinionWithCurrentId(int minionId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT  name , age FROM  minions WHERE id = ?;");
        ps.setInt(1, minionId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.printf("%s %d%n", rs.getString("name"), rs.getInt("age"));
        }
    }

    private static void updateMinionAge() throws SQLException {
        List<Integer> minionsId = Arrays.stream(scanner.nextLine().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int id : minionsId) {
            changeFirstLetterInMinionName(id);
            changeMinionAge(id);
        }
        PreparedStatement ps = connection.prepareStatement("SELECT  name , age FROM  minions;");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.printf("%s %d%n", rs.getString("name"), rs.getInt("age"));
        }
    }

    private static void changeMinionAge(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE  minions\n" +
                "SET  age = age + 1\n" +
                "where  id = ?;");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static void changeFirstLetterInMinionName(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE  minions\n" +
                "SET  name = concat(lower(LEFT(name,1)),SUBSTRING(name, 2))\n" +
                "where  id = ?;");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static void printingMinionsName() throws SQLException {
        int minionCount = countingMinions();
        for (int i = 1; i <= minionCount / 2; i++) {
            if (i == 1) {
                System.out.println(getFirstMinion());
                System.out.println(getLastMinion());
            } else {
                System.out.println(getFirstPlusMinion(i));
                System.out.println(getLastMinusMinion(minionCount - i + 1));
            }
        }
    }

    private static String getLastMinusMinion(int minionId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  minions\n" +
                "WHERE  id = ?\n" +
                "ORDER BY id DESC ");
        ps.setInt(1, minionId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("name") : null;
    }

    private static String getFirstPlusMinion(int minionId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  minions\n" +
                "WHERE  id = ?\n" +
                "ORDER BY id ");
        ps.setInt(1, minionId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("name") : null;
    }

    private static String getLastMinion() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  minions\n" +
                "ORDER BY id DESC\n" +
                "LIMIT  1;");
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("name") : null;
    }

    private static String getFirstMinion() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  minions\n" +
                "ORDER BY id\n" +
                "LIMIT  1;");
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("name") : null;
    }

    private static int countingMinions() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT  count(id) as countId FROM  minions;");
        ResultSet rs = ps.executeQuery();
        int numTowns = 0;
        while (rs.next()) {
            numTowns = rs.getInt("countId");
        }
        return numTowns;
    }

    private static void removeVillianIfExist() throws SQLException {
        int id = Integer.parseInt(scanner.nextLine());
        String villian = getNameById(id);
        int countRealisedMinion = releacedMinion(id);
        if (villian == null) {
            System.out.println("No such villain was found");
        } else {
            deleteIdVillianFromMinionVillianTable(id);
            deleteVillianFromVilliansTable(id);
            System.out.printf("%s was deleted%n", villian);
            System.out.printf("%d minions released", countRealisedMinion);
        }
    }

    private static void deleteVillianFromVilliansTable(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE  FROM  villains\n" +
                "where  id = ?;");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static void deleteIdVillianFromMinionVillianTable(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE  FROM  minions_villains\n" +
                "where  villain_id = ?;");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private static int releacedMinion(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("\n" +
                "SELECT  count(m.id) as count_minion FROM  minions  as m\n" +
                "JOIN  minions_villains mv\n" +
                "ON m.id = mv.minion_id\n" +
                "WHERE  villain_id = ?;");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            count += rs.getInt("count_minion");
        }
        return count;
    }


    private static void changeTownName() throws SQLException {
        String country = scanner.nextLine();

        int numOfTowns = checkIfCountryHaveTowns(country);
        if (numOfTowns > 0) {
            PreparedStatement ps = connection.prepareStatement("UPDATE  towns\n" +
                    "SET  name = upper(name)\n" +
                    "WHERE  country = ?;\n");
            ps.setString(1, country);
            ps.executeUpdate();

            List<String> towns = new ArrayList<>();
            getTownsName(country, towns);
            System.out.printf("%d town names were affected.%n", numOfTowns);
            System.out.println(towns);
        } else {
            System.out.println("No town names were affected.");
        }
    }

    private static void getTownsName(String country, List<String> towns) throws SQLException {
        PreparedStatement ps2 = connection.prepareStatement("SELECT  name FROM  towns\n" +
                "WHERE  country = ?;");
        ps2.setString(1, country);
        ResultSet rs = ps2.executeQuery();
        while (rs.next()) {
            towns.add(rs.getString("name"));
        }
    }

    private static int checkIfCountryHaveTowns(String country) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT  count(id) as count FROM  towns\n" +
                "WHERE  country = ?;");
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        int numTowns = 0;
        while (rs.next()) {
            numTowns = rs.getInt("count");
        }
        return numTowns;
    }

    private static void addingMinion() throws SQLException {
        String[] minionInfo = scanner.nextLine().split("\\s+");
        String[] villainInfo = scanner.nextLine().split("\\s+");
        String mName = minionInfo[1];
        int age = Integer.parseInt(minionInfo[2]);
        String city = minionInfo[3];
        String vName = villainInfo[1];

        if (!checkFromExistingTown(city)) {
            PreparedStatement ps = connection.prepareStatement("INSERT  INTO towns(name,country) VALUE\n" +
                    "(?,null);");
            ps.setString(1, city);
            ps.executeUpdate();
            System.out.printf("Town %s was added to the database.%n", city);
        }

        if (!checkFromExistingVillian(vName)) {
            PreparedStatement ps = connection.prepareStatement("\n" +
                    "INSERT  INTO  villains (name,evilness_factor) VALUE \n" +
                    "( ?,'evil');");
            ps.setString(1, vName);
            ps.executeUpdate();
            System.out.printf("Villain %s was added to the database.%n", vName);
        }
        addMinionToTable(mName, age, city);
        addMinionIntoVillian(mName, vName);
    }

    private static void addMinionIntoVillian(String mName, String vName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT  INTO  minions_villains(minion_id, villain_id) VALUE\n" +
                "((SELECT id FROM  minions WHERE  name = ?),(SELECT id FROM villains WHERE  name = ?));");
        ps.setString(1, mName);
        ps.setString(2, vName);
        ps.executeUpdate();
        System.out.printf("Successfully added %s to be minion of %s.%n", mName, vName);
    }

    private static void addMinionToTable(String mName, int age, String city) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT  INTO  minions (name, age, town_id) VALUE\n" +
                "(?,?,(SELECT id from towns where towns.name = ?));");
        ps.setString(1, mName);
        ps.setInt(2, age);
        ps.setString(3, city);
        ps.executeUpdate();
    }

    private static boolean checkFromExistingVillian(String vName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  villains\n" +
                "WHERE  name = ?;");
        ps.setString(1, vName);
        ResultSet re = ps.executeQuery();
        return re.next();
    }

    private static boolean checkFromExistingTown(String city) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT  name from towns\n" +
                "WHERE  name = ?;");
        ps.setString(1, city);
        ResultSet re = ps.executeQuery();
        return re.next();
    }

    private static void getMinionsNames() throws SQLException {
        int villainId = Integer.parseInt(scanner.nextLine());
        if (!checkIfVillianIdNotExist(villainId)) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
        } else {
            System.out.printf("Villain: %s%n", getNameById(villainId));
            PreparedStatement ps = connection.prepareStatement("SELECT m.name,m.age FROM minions as m\n" +
                    "JOIN  minions_villains as mv\n" +
                    "ON  mv.minion_id = m.id\n" +
                    "JOIN  villains as v\n" +
                    "ON  v.id = mv.villain_id\n" +
                    "WHERE villain_id = ?;");
            ps.setInt(1, villainId);
            ResultSet rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                System.out.printf("%d. %s %d%n", index++, rs.getString("name"), rs.getInt("age"));
            }
        }
    }

    private static String getNameById(int villainId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM  villains\n" +
                "where id = ?");
        ps.setInt(1, villainId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getString("name") : null;
    }

    private static boolean checkIfVillianIdNotExist(int villainId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM  villains\n" +
                "where id = ?");
        ps.setInt(1, villainId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static void getVilliansNames() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT  v.name,count(mv.minion_id) as count" +
                " FROM  villains as v\n" +
                "JOIN  minions_villains as mv\n" +
                "ON  mv.villain_id = v.id\n" +
                "GROUP BY  v.name\n" +
                "HAVING  count > 15\n" +
                "ORDER BY count DESC;");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.printf("%s %d%n", rs.getString("name"), rs.getInt("count"));
        }
    }
}
