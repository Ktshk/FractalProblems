package com.dinoproblems.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.dinoproblems.server.generators.ProblemScenarioImpl;

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
                                  String problem_text,
                                  String difficulty,
                                  String username,
                                  String scenario,
                                  String generator_id,
                                  int points,
                                  boolean hint_used) {
        int problem_id = 0;
        int reference_id = 0;
        int user_id = 0;
        int scenario_id = 0;
        int solution_id = 0;
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
        ResultSet scenarioIdQuery = null;
        ResultSet problemLastId = null;


        String insertTableUsers = "INSERT INTO alisa.users (username) " +
                "VALUES" + "(?)";

        String insertTableDevices = "INSERT INTO alisa.devices (device_id, device_num) " +
                "VALUES" + "(?,?)";

        String insertTableReference = "INSERT INTO alisa.reference (device_id, user_id) " +
                "VALUES" + "(?,?)";

        String insertTableScenarios = "INSERT INTO alisa.scenarios (scenario, generator_id) " +
                "VALUES" + "(?,?)";

        String insertTableProblems = "INSERT INTO alisa.problems (problem_text, difficulty, scenario_id) " +
                "VALUES" + "(?,?,?)";

        String insertTableSolutions = "INSERT INTO alisa.solutions (reference_id, problem_id, points, hint_used) " +
                "VALUES" + "(?,?,?,?)";

        String refCheck = "SELECT reference_id FROM alisa.reference WHERE device_id = \'" + device_id + "\'";

        String scenarioCheck = "SELECT scenario_id FROM alisa.scenarios WHERE scenario = \'" + scenario + "\'";


        try {
            refCheckStatement = connection.createStatement();
            refExists = refCheckStatement.executeQuery(refCheck);

            scenarioCheckStatement = connection.createStatement();
            scenarioExists = scenarioCheckStatement.executeQuery(scenarioCheck);
            if (refExists.next()) {
                reference_id = refExists.getInt(1);
                scenario_id = scenarioExists.getInt(1);
                if (!scenarioExists.next()) {
                    preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios, Statement.RETURN_GENERATED_KEYS);
                    preparedStatementInsertScenarios.setString(1, scenario);
                    preparedStatementInsertScenarios.setString(2, generator_id);
                    preparedStatementInsertScenarios.executeUpdate();
                    scenarioIdQuery = preparedStatementInsertProblems.getGeneratedKeys();
                    if (scenarioIdQuery.next()) {
                        scenario_id = scenarioIdQuery.getInt(1);
                    }
                }

                connection.setAutoCommit(false);
                preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertProblems.setString(1, problem_text);
                preparedStatementInsertProblems.setString(2, difficulty);
                preparedStatementInsertProblems.setInt(3, scenario_id);
                preparedStatementInsertProblems.executeUpdate();
                problemLastId = preparedStatementInsertProblems.getGeneratedKeys();
                if (problemLastId.next()) {
                    problem_id = problemLastId.getInt(1);
                }
                preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertSolutions.setInt(1, reference_id);
                preparedStatementInsertSolutions.setInt(2, problem_id);
                preparedStatementInsertSolutions.setInt(3, points);
                preparedStatementInsertSolutions.setBoolean(4, hint_used);
                preparedStatementInsertSolutions.executeUpdate();

                connection.commit();
                System.out.println("Records created successfully");
            } else {

                connection.setAutoCommit(false);

                preparedStatementInsertUsers = connection.prepareStatement(insertTableUsers, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertUsers.setString(1, username);
                preparedStatementInsertUsers.executeUpdate();
                ResultSet userLastId = preparedStatementInsertUsers.getGeneratedKeys();
                if (userLastId.next()) {
                    user_id = userLastId.getInt("user_id");
                }

                preparedStatementInsertDevices = connection.prepareStatement(insertTableDevices, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertDevices.setString(1, device_id);
                preparedStatementInsertDevices.setString(2, device_num);
                preparedStatementInsertDevices.executeUpdate();

                preparedStatementInsertReference = connection.prepareStatement(insertTableReference, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertReference.setString(1, device_id);
                preparedStatementInsertReference.setInt(2, user_id);
                preparedStatementInsertReference.executeUpdate();
                ResultSet referenceLastId = preparedStatementInsertReference.getGeneratedKeys();
                if (referenceLastId.next()) {
                    reference_id = referenceLastId.getInt("reference_id");
                }

                preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertScenarios.setString(1, scenario);
                preparedStatementInsertScenarios.setString(2, generator_id);
                preparedStatementInsertScenarios.executeUpdate();
                ResultSet scenarioLastId = preparedStatementInsertScenarios.getGeneratedKeys();
                if (scenarioLastId.next()) {
                    scenario_id = scenarioLastId.getInt("scenario_id");
                }

                preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertProblems.setString(1, problem_text);
                preparedStatementInsertProblems.setString(2, difficulty);
                preparedStatementInsertProblems.setInt(3, scenario_id);
                preparedStatementInsertProblems.executeUpdate();
                problemLastId = preparedStatementInsertProblems.getGeneratedKeys();
                if (problemLastId.next()) {
                    problem_id = problemLastId.getInt("problem_id");
                }

                preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertSolutions.setInt(1, reference_id);
                preparedStatementInsertSolutions.setInt(2, problem_id);
                preparedStatementInsertSolutions.setInt(3, points);
                preparedStatementInsertSolutions.setBoolean(4, hint_used);
                preparedStatementInsertSolutions.executeUpdate();
                problemLastId = preparedStatementInsertSolutions.getGeneratedKeys();
                if (problemLastId.next()) {
                    solution_id = problemLastId.getInt("solution_id");
                }

                connection.commit();
                System.out.println("Records created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (refCheckStatement != null) refCheckStatement.close();
                if (refExists != null) refExists.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (preparedStatementInsertUsers != null) preparedStatementInsertUsers.close();
            } catch (Exception e) {
            }
            try {
                if (preparedStatementInsertDevices != null) preparedStatementInsertDevices.close();
            } catch (Exception e) {
            }
            try {
                if (preparedStatementInsertReference != null) preparedStatementInsertReference.close();
            } catch (Exception e) {
            }
            try {
                if (preparedStatementInsertScenarios != null) preparedStatementInsertScenarios.close();
            } catch (Exception e) {
            }
            try {
                if (preparedStatementInsertProblems != null) preparedStatementInsertProblems.close();
            } catch (Exception e) {
            }
            try {
                if (preparedStatementInsertSolutions != null) preparedStatementInsertSolutions.close();
            } catch (Exception e) {
            }
            try {
                if (scenarioCheckStatement != null) scenarioCheckStatement.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (scenarioExists != null) scenarioExists.close();
            } catch (Exception e) {
            }
            ;
        }

    }

    public Map<String, UserInfo> getUserInfoFromDB() {
        Map<String, UserInfo> dbUserInfo = new HashMap<>();
        UserInfo userInfo;
        Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();
        Problem problem;

        ResultSet users = null;
        ResultSet devices = null;
        ResultSet problems = null;
        ResultSet scenarios = null;
        ResultSet solutions = null;
        ResultSet reference = null;

        String selectTableUsers = "SELECT * FROM alisa.users";
        String selectTableDevices = "SELECT * FROM alisa.devices";
        String selectTableProblems = "SELECT * FROM alisa.problems";
        String selectTableScenarios = "SELECT * FROM alisa.scenarios";
        String selectTableSolutions = "SELECT * FROM alisa.solutions";
        String selectTableReference = "SELECT * FROM alisa.reference";

        Statement problemsQuery = null;
        Statement scenariosQuery = null;
        Statement solutionsQuery = null;
        Statement referenceQuery = null;

        try (final Statement usersQuery = connection.createStatement();
             final Statement devicesQuery = connection.createStatement()){
            connection.setAutoCommit(false);

            users = usersQuery.executeQuery(selectTableUsers);

            devices = devicesQuery.executeQuery(selectTableDevices);

            problemsQuery = connection.createStatement();
            problems = problemsQuery.executeQuery(selectTableProblems);

            scenariosQuery = connection.createStatement();
            scenarios = scenariosQuery.executeQuery(selectTableScenarios);

            solutionsQuery = connection.createStatement();
            solutions = solutionsQuery.executeQuery(selectTableSolutions);

            referenceQuery = connection.createStatement();
            reference = referenceQuery.executeQuery(selectTableReference);

            connection.commit();
            System.out.println("Records downloaded successfully");

            String currentDeviceId;
            String theme = null;
            String hint = null;
            String problem_text = null;
            String difficulty_text = null;
            String username = null;
            ProblemScenario scenario = null;
            while (reference.next() && users.next()) {
                currentDeviceId = reference.getString("device_id");
                while (solutions.next() && problems.next()) {
                    if (solutions.getInt("reference_id") == reference.getInt("reference_id")) {
                        while (scenarios.next()) {
                            if (scenarios.getInt("scenario_id") == problems.getInt("scenario_id")) {
                                theme = scenarios.getString("generator_id");
                                scenario = new ProblemScenarioImpl(scenarios.getString("scenario"));
                            }
                        }

                        if (solutions.getBoolean("hint_used")) {
                            hint = "true";
                        }

                        problem_text = problems.getString("problem_text");
                        difficulty_text = problems.getString("difficulty");
                        problem = new ProblemWithPossibleTextAnswers(
                                problem_text,
                                0,
                                theme,
                                null,
                                hint,
                                scenario,
                                Problem.Difficulty.valueOf(difficulty_text));
                        solvedProblemsByTheme.put(theme, problem);
                    }
                }
                username = users.getString("username");
                userInfo = new UserInfo(
                        currentDeviceId,
                        username,
                        solvedProblemsByTheme);
                dbUserInfo.put(currentDeviceId, userInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (devices != null) devices.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (users != null) users.close();
            } catch (Exception e) {
            }
            try {
                if (problemsQuery != null) problemsQuery.close();
            } catch (Exception e) {
            }
            try {
                if (problems != null) problems.close();
            } catch (Exception e) {
            }
            try {
                if (scenariosQuery != null) scenariosQuery.close();
            } catch (Exception e) {
            }
            try {
                if (scenarios != null) scenarios.close();
            } catch (Exception e) {
            }
            try {
                if (solutionsQuery != null) scenariosQuery.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (solutions != null) solutions.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (referenceQuery != null) referenceQuery.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (reference != null) reference.close();
            } catch (Exception e) {
            }
            ;
        }
        return dbUserInfo;
    }
}
