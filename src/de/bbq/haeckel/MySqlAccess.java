package de.bbq.haeckel;

// Adapted from http://www.vogella.com/tutorials/MySQLJava/article.html
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

public class MySqlAccess {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    final private String host = "localhost";
    final private String user = "root";
    final private String passwd = "toor";

    public void readDataBase() throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");

            // Setup the connection with the DB
            connect = DriverManager.
                    getConnection("jdbc:mysql://" + host + "/company?" +
                            "user=" + user + "&password=" + passwd );

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("SELECT * FROM employee ");
            writeResultSet(resultSet);

            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  company.employee values (default, ?, ?, ?, ? , ?, ?, ?)");

            // Parameters start with 1
            preparedStatement.setString(1, "Vorname");
            preparedStatement.setString(2, "Nachname");
       //     preparedStatement.setDate(3, new java.sql.Date(2000,12,24)); is deprecated
            preparedStatement.setObject(3 , LocalDate.of(2001, 12, 24)  );
            preparedStatement.setInt(4, 2);
            preparedStatement.setFloat(5, 3333.33f);
            preparedStatement.setString(6, "w");
            preparedStatement.setInt(7, 2);
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT * from company.employee");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from company.employee where lastName= ? ; ");
            preparedStatement.setString(1, "Nachname");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from company.employee");
            writeMetaData(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //   Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getString(2);
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            String monthlySalary = resultSet.getString("monthlySalary");
            Date dateOfBirth = resultSet.getDate("dateOfBirth");
            int department_id = resultSet.getInt("department_id");
            System.out.println("id: " + id);
            System.out.println("firstName: " + firstName );
            System.out.println("lastName: " + lastName);
            System.out.println("gender: " + gender);
            System.out.println("dateOfBirth: " + dateOfBirth);
            System.out.println("department_id: " + department_id);
            System.out.println("monthlySalary: " + monthlySalary);
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}
