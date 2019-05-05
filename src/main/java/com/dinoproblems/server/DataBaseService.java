package com.dinoproblems.server;

import javax.xml.transform.Result;
import java.sql.*;

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

    public void insertSessionInfo(String device_id,
                                  String device_num,
                                  String problem_id,
                                  String problem_text,
                                  String difficulty,
                                  String reference_id,
                                  String user_id,
                                  String username,
                                  String scenario_id,
                                  String scenario,
                                  String solution_id,
                                  int points,
                                  boolean hint_used) {
        PreparedStatement preparedStatementInsertUsers = null;
        PreparedStatement preparedStatementInsertDevices = null;
        PreparedStatement preparedStatementInsertReference = null;
        PreparedStatement preparedStatementInsertScenarios = null;
        PreparedStatement preparedStatementInsertProblems = null;
        PreparedStatement preparedStatementInsertSolutions = null;
        Statement refCheckStatement = null;
        ResultSet refExists = null;
        Statement scenarioCheckStatement = null;
        ResultSet scenarioExists = null;

        String insertTableUsers = "INSERT INTO alisa.users (user_id, username) " +
                "VALUES" + "(?,?)";

        String insertTableDevices = "INSERT INTO alisa.devices (device_id, device_num) " +
                "VALUES" + "(?,?)";

        String insertTableReference = "INSERT INTO alisa.reference (reference_id, device_id, user_id) " +
                "VALUES" + "(?,?,?)";

        String insertTableScenarios = "INSERT INTO alisa.scenarios (scenario_id, scenario) " +
                "VALUES" + "(?,?)";

        String insertTableProblems = "INSERT INTO alisa.problems (problem_id, problem_text, difficulty, scenario_id) " +
                "VALUES" + "(?,?,?,?)";

        String insertTableSolutions = "INSERT INTO alisa.solutions (solution_id, reference_id, problem_id, points, hint_used) " +
                "VALUES" + "(?,?,?,?,?)";

        String refCheck = "SELECT * FROM alisa.reference WHERE reference_id = \'" + reference_id + "\'";

        String scenarioCheck = "SELECT * FROM alisa.scenarios WHERE scenario = \'" + scenario + "\'";



        try {
            refCheckStatement = connection.createStatement();
            refExists = refCheckStatement.executeQuery(refCheck);

            scenarioCheckStatement = connection.createStatement();
            scenarioExists =  scenarioCheckStatement.executeQuery(scenarioCheck);

            if(refExists.next()) {
                if(!scenarioExists.next()) {
                    preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios);
                    preparedStatementInsertScenarios.setString(1, scenario_id);
                    preparedStatementInsertScenarios.setString(2, scenario);
                    preparedStatementInsertScenarios.executeUpdate();
                }
                connection.setAutoCommit(false);
                preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems);
                preparedStatementInsertProblems.setString(1, problem_id);
                preparedStatementInsertProblems.setString(2, problem_text);
                preparedStatementInsertProblems.setString(3, difficulty);
                preparedStatementInsertProblems.setString(4, scenario_id);
                preparedStatementInsertProblems.executeUpdate();

                preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions);
                preparedStatementInsertSolutions.setString(1, solution_id);
                preparedStatementInsertSolutions.setString(2, reference_id);
                preparedStatementInsertSolutions.setString(3, problem_id);
                preparedStatementInsertSolutions.setInt(4, points);
                preparedStatementInsertSolutions.setBoolean(5, hint_used);
                preparedStatementInsertSolutions.executeUpdate();

                connection.commit();
                System.out.println("Records created successfully");
            } else {

                connection.setAutoCommit(false);

                preparedStatementInsertUsers = connection.prepareStatement(insertTableUsers);
                preparedStatementInsertUsers.setString(1, user_id);
                preparedStatementInsertUsers.setString(2, username);
                preparedStatementInsertUsers.executeUpdate();

                preparedStatementInsertDevices = connection.prepareStatement(insertTableDevices);
                preparedStatementInsertDevices.setString(1, device_id);
                preparedStatementInsertDevices.setString(2, device_num);
                preparedStatementInsertDevices.executeUpdate();

                preparedStatementInsertReference = connection.prepareStatement(insertTableReference);
                preparedStatementInsertReference.setString(1, reference_id);
                preparedStatementInsertReference.setString(2, device_id);
                preparedStatementInsertReference.setString(3, user_id);
                preparedStatementInsertReference.executeUpdate();

                preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios);
                preparedStatementInsertScenarios.setString(1, scenario_id);
                preparedStatementInsertScenarios.setString(2, scenario);
                preparedStatementInsertScenarios.executeUpdate();

                preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems);
                preparedStatementInsertProblems.setString(1, problem_id);
                preparedStatementInsertProblems.setString(2, problem_text);
                preparedStatementInsertProblems.setString(3, difficulty);
                preparedStatementInsertProblems.setString(4, scenario_id);
                preparedStatementInsertProblems.executeUpdate();

                preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions);
                preparedStatementInsertSolutions.setString(1, solution_id);
                preparedStatementInsertSolutions.setString(2, reference_id);
                preparedStatementInsertSolutions.setString(3, problem_id);
                preparedStatementInsertSolutions.setInt(4, points);
                preparedStatementInsertSolutions.setBoolean(5, hint_used);
                preparedStatementInsertSolutions.executeUpdate();

                connection.commit();
                System.out.println("Records created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (refCheckStatement != null) refCheckStatement.close(); } catch (Exception e) {};
            try { if (refExists != null) refExists.close(); } catch (Exception e) {};
            try { if (preparedStatementInsertUsers != null) preparedStatementInsertUsers.close(); } catch (Exception e) {}
            try { if (preparedStatementInsertDevices != null) preparedStatementInsertDevices.close(); } catch (Exception e) {}
            try { if (preparedStatementInsertReference != null) preparedStatementInsertReference.close(); } catch (Exception e) {}
            try { if (preparedStatementInsertScenarios != null) preparedStatementInsertScenarios.close(); } catch (Exception e) {}
            try { if (preparedStatementInsertProblems != null) preparedStatementInsertProblems.close(); } catch (Exception e) {}
            try { if (preparedStatementInsertSolutions != null) preparedStatementInsertSolutions.close(); } catch (Exception e) {}
            try { if (scenarioCheckStatement != null) scenarioCheckStatement.close(); } catch (Exception e) {};
            try { if (scenarioExists != null) scenarioExists.close(); } catch (Exception e) {};
        }

    }

}
