package com.dinoproblems.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by Katushka on 21.03.2019.
 */
public class DataBaseService {
    static final DataBaseService INSTANCE = new DataBaseService();

    private Connection connection;

    private DataBaseService() {
        connection = getRemoteConnection();
    }

    private static Connection getRemoteConnection() {
        if (System.getProperty("RDS_HOSTNAME") != null) {
            try {
                Class.forName("org.postgresql.Driver");
                String dbName = System.getProperty("RDS_DB_NAME");
                String userName = System.getProperty("RDS_USERNAME");
                String password = System.getProperty("RDS_PASSWORD");
                String hostname = System.getProperty("RDS_HOSTNAME");
                String port = System.getProperty("RDS_PORT");
                String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                System.out.println("Getting remote connection with connection string from environment variables.");
                Connection con = DriverManager.getConnection(jdbcUrl);
                System.out.println("Remote connection successful.");
                return con;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("RDS_HOSTNAME not specified");
        }
        return null;
    }

    public void updateMiscAnswersTable(String answer, String problem, String lastServerResponse) {

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO alisa.misc_answers (answer_text,problem_text,last_server_response)\n" +
                    "\tVALUES (" +
                    "\'" + answer + "\'," +
                    "\'" + problem + "\'," +
                    "\'" + lastServerResponse + "\')" +
                    "\tON CONFLICT (answer_text,last_server_response) DO UPDATE\n" +
                    "\tSET counter = alisa.misc_answers.counter + 1";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.commit();
            System.out.println("Records created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /* Метод
    insert into alisa.misc_answers (id, answer_text, last_server_response, problem_text) values ('1', 'выапывапывап', 'лолооло', 'ываывывыв');
    id - war char uID

    INSERT INTO alisa.misc_answers (answer_text,problem_text,last_server_response)
	VALUES ('sdfasdf', 'aaa','aaaaaaa')
	ON CONFLICT (answer_text,last_server_response) DO UPDATE
	SET counter = alisa.misc_answers.counter + 1
     */

}
