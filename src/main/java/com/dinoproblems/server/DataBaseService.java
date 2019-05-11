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

        String problemCheck = "select problem_text, difficulty, problem_id FROM alisa.problems WHERE problem_text = \'" + problem_text + "\' and difficulty = \'" + difficulty + "\'";

        try {
            refCheckStatement = connection.createStatement();
            refExists = refCheckStatement.executeQuery(refCheck);

            scenarioCheckStatement = connection.createStatement();
            scenarioExists = scenarioCheckStatement.executeQuery(scenarioCheck);

            Statement problemCheckStatement = connection.createStatement();
            ResultSet problemExists = problemCheckStatement.executeQuery(problemCheck);


            if (refExists.next()) {
                reference_id = refExists.getInt(1);

                if (!scenarioExists.next()) {
                    preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios, Statement.RETURN_GENERATED_KEYS);
                    preparedStatementInsertScenarios.setString(1, scenario);
                    preparedStatementInsertScenarios.setString(2, generator_id);
                    preparedStatementInsertScenarios.executeUpdate();
                    scenarioIdQuery = preparedStatementInsertScenarios.getGeneratedKeys();
                    if (scenarioIdQuery.next()) {
                        scenario_id = scenarioIdQuery.getInt("scenario_id");
                    }
                } else {
                    scenario_id = scenarioExists.getInt("scenario_id");
                }

                connection.setAutoCommit(false);

                if (!problemExists.next()) {
                    preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems, Statement.RETURN_GENERATED_KEYS);
                    preparedStatementInsertProblems.setString(1, problem_text);
                    preparedStatementInsertProblems.setString(2, difficulty);
                    preparedStatementInsertProblems.setInt(3, scenario_id);
                    preparedStatementInsertProblems.executeUpdate();
                    problemLastId = preparedStatementInsertProblems.getGeneratedKeys();
                    if (problemLastId.next()) {
                        problem_id = problemLastId.getInt("problem_id");
                    }
                } else {
                    problem_id = problemExists.getInt("problem_id");
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

                if (!scenarioExists.next()) {
                    preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios, Statement.RETURN_GENERATED_KEYS);
                    preparedStatementInsertScenarios.setString(1, scenario);
                    preparedStatementInsertScenarios.setString(2, generator_id);
                    preparedStatementInsertScenarios.executeUpdate();
                    ResultSet scenarioLastId = preparedStatementInsertScenarios.getGeneratedKeys();
                    if (scenarioLastId.next()) {
                        scenario_id = scenarioLastId.getInt("scenario_id");
                    }
                } else {
                    scenario_id = scenarioExists.getInt("scenario_id");
                }

                if (!problemExists.next()) {
                    preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems, Statement.RETURN_GENERATED_KEYS);
                    preparedStatementInsertProblems.setString(1, problem_text);
                    preparedStatementInsertProblems.setString(2, difficulty);
                    preparedStatementInsertProblems.setInt(3, scenario_id);
                    preparedStatementInsertProblems.executeUpdate();
                    problemLastId = preparedStatementInsertProblems.getGeneratedKeys();
                    if (problemLastId.next()) {
                        problem_id = problemLastId.getInt("problem_id");
                    }
                } else {
                    problem_id = problemExists.getInt("problem_id");
                }

                preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertSolutions.setInt(1, reference_id);
                preparedStatementInsertSolutions.setInt(2, problem_id);
                preparedStatementInsertSolutions.setInt(3, points);
                preparedStatementInsertSolutions.setBoolean(4, hint_used);
                preparedStatementInsertSolutions.executeUpdate();

                connection.commit();
                System.out.println("Records created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (refCheckStatement != null) refCheckStatement.close();
                if (refExists != null) refExists.close();
                if (preparedStatementInsertUsers != null) preparedStatementInsertUsers.close();
                if (preparedStatementInsertDevices != null) preparedStatementInsertDevices.close();
                if (preparedStatementInsertReference != null) preparedStatementInsertReference.close();
                if (preparedStatementInsertScenarios != null) preparedStatementInsertScenarios.close();
                if (preparedStatementInsertProblems != null) preparedStatementInsertProblems.close();
                if (preparedStatementInsertSolutions != null) preparedStatementInsertSolutions.close();
                if (scenarioCheckStatement != null) scenarioCheckStatement.close();
                if (scenarioExists != null) scenarioExists.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Map <String, UserInfo> getUserInfoFromDB()
    {
        Map <String, UserInfo> dbUserInfo = new HashMap<String, UserInfo>();
        UserInfo userInfo;
        Multimap<String, Problem> solvedProblemsByTheme;
        Problem problem;

        String currentDeviceId;
        String theme;
        String hint;
        String problem_text;
        String difficulty_text;
        String username;
        ProblemScenario scenario;

        String selectAllTables = "select alisa.reference.device_id, username, problem_text, difficulty, scenario, points, hint_used, generator_id\n" +
                "from alisa.reference, alisa.devices, alisa.users, alisa.problems, alisa.solutions, alisa.scenarios\n" +
                "where alisa.reference.device_id = alisa.devices.device_id and alisa.users.user_id = alisa.reference.user_id\n" +
                "and alisa.solutions.reference_id = alisa.reference.reference_id and alisa.solutions.problem_id = alisa.problems.problem_id\n" +
                "and alisa.problems.scenario_id = alisa.scenarios.scenario_id";

        try (Statement selectAllTablesQuery = connection.createStatement()){
            ResultSet allTables = selectAllTablesQuery.executeQuery(selectAllTables);
            while(allTables.next())
            {
                currentDeviceId = allTables.getString("device_id");
                theme = allTables.getString("generator_id");
                scenario = new ProblemScenarioImpl (allTables.getString("scenario"));
                if(allTables.getBoolean("hint_used")) {
                    hint = "true";
                } else {
                    hint = "false";
                }
                problem_text = allTables.getString("problem_text");
                difficulty_text = allTables.getString("difficulty");
                problem = new ProblemWithPossibleTextAnswers(
                        problem_text,
                        0,
                        theme,
                        null,
                        hint,
                        scenario,
                        Problem.Difficulty.valueOf(difficulty_text));
                solvedProblemsByTheme = HashMultimap.create();
                solvedProblemsByTheme.put(theme, problem);
                username = allTables.getString("username");
                userInfo = new UserInfo(
                        currentDeviceId,
                        username,
                        solvedProblemsByTheme);
                if(dbUserInfo.containsKey(currentDeviceId)) {
                    UserInfo addedUserInfo = dbUserInfo.get(currentDeviceId);
                    addedUserInfo.getSolvedProblemsByTheme().put(theme, problem);
                    dbUserInfo.put(currentDeviceId, addedUserInfo);
                } else {
                    dbUserInfo.put(currentDeviceId, userInfo);
                }
            }
        } catch (Exception e) {

        }

        return dbUserInfo;
    }
}
