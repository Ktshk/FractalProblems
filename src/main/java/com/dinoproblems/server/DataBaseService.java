package com.dinoproblems.server;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.dinoproblems.server.generators.ProblemScenarioImpl;

/**
 * Created by Katushka on 21.03.2019.
 */
public class DataBaseService {
    static final DataBaseService INSTANCE = new DataBaseService();
    private String schemeName = "alisa";

    private Connection connection;

    private DataBaseService() {
        connection = getRemoteConnection();
    }

    private Connection getRemoteConnection() {
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

                if (System.getProperty("DB_SCHEME") != null) {
                    schemeName = System.getProperty("DB_SCHEME");
                    System.out.println("schemeName = " + schemeName);
                } else {
                    System.out.println("DB_SCHEME is not specified, using default: " + schemeName);
                }

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
        if (connection == null) {
            System.out.println("Could not connect to DB");
            return;
        }

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO " + schemeName + ".misc_answers (answer_text,problem_text,last_server_response)\n" +
                    "\tVALUES (" +
                    "\'" + answer + "\'," +
                    "\'" + problem + "\'," +
                    "\'" + lastServerResponse + "\')" +
                    "\tON CONFLICT (answer_text,last_server_response) DO UPDATE\n" +
                    "\tSET counter = " + schemeName + ".misc_answers.counter + 1";
            System.out.println(sql);
            stmt.executeUpdate(sql);

            stmt.close();
            connection.commit();
            System.out.println("Records created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSessionInfo(String deviceId,
                                  String deviceNum,
                                  String problemText,
                                  String difficulty,
                                  String username,
                                  String scenario,
                                  String generatorId,
                                  int points,
                                  boolean hintUsed) {

        if (connection == null) {
            System.out.println("Could not connect to DB");
            return;
        }

        String refCheck = "SELECT reference_id FROM " + schemeName + ".reference WHERE device_id = \'" + deviceId + "\'";

        String scenarioCheck = "SELECT scenario_id FROM " + schemeName + ".scenarios WHERE scenario = \'" + scenario + "\'";

        String problemCheck = "select problem_text, difficulty, problem_id FROM " + schemeName + ".problems WHERE problem_text = \'" + problemText + "\' and difficulty = \'" + difficulty + "\'";

        try (Statement refCheckStatement = connection.createStatement();
             ResultSet refExists = refCheckStatement.executeQuery(refCheck);

             Statement scenarioCheckStatement = connection.createStatement();
             ResultSet scenarioExists = scenarioCheckStatement.executeQuery(scenarioCheck);

             Statement problemCheckStatement = connection.createStatement();
             ResultSet problemExists = problemCheckStatement.executeQuery(problemCheck)) {

            connection.setAutoCommit(false);

            final int referenceId;
            if (refExists.next()) {
                System.out.println("Reference already exists");
                referenceId = refExists.getInt(1);
            } else {
                referenceId = insertReference(deviceId, deviceNum, username);
            }

            final int scenarioId = getScenarioId(scenario, generatorId, scenarioExists);
            final int problemId = getProblemId(problemText, difficulty, scenarioId, problemExists);

            insertSolution(points, hintUsed, referenceId, problemId);

            connection.commit();
            System.out.println("Records created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int insertReference(String deviceId, String deviceNum, String username) throws SQLException {
        System.out.println("Create new reference");
        String insertTableUsers = "INSERT INTO " + schemeName + ".users (username) " +
                "VALUES" + "(?)";

        String insertTableDevices = "INSERT INTO " + schemeName + ".devices (device_id, device_num) " +
                "VALUES" + "(?,?)";

        String insertTableReference = "INSERT INTO " + schemeName + ".reference (device_id, user_id) " +
                "VALUES" + "(?,?)";

        try (PreparedStatement preparedStatementInsertUsers = connection.prepareStatement(insertTableUsers, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementInsertDevices = connection.prepareStatement(insertTableDevices, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatementInsertUsers.setString(1, username);
            preparedStatementInsertUsers.executeUpdate();
            ResultSet userLastId = preparedStatementInsertUsers.getGeneratedKeys();
            int userId = 0;
            if (userLastId.next()) {
                userId = userLastId.getInt("user_id");
            }

            preparedStatementInsertDevices.setString(1, deviceId);
            preparedStatementInsertDevices.setString(2, deviceNum);
            preparedStatementInsertDevices.executeUpdate();

            try (PreparedStatement preparedStatementInsertReference = connection.prepareStatement(insertTableReference, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementInsertReference.setString(1, deviceId);
                preparedStatementInsertReference.setInt(2, userId);
                preparedStatementInsertReference.executeUpdate();
                ResultSet referenceLastId = preparedStatementInsertReference.getGeneratedKeys();
                if (referenceLastId.next()) {
                    return referenceLastId.getInt("reference_id");
                } else {
                    return 0;
                }
            }
        }
    }

    private void insertSolution(int points, boolean hintUsed, int referenceId, int problemId) throws SQLException {
        String insertTableSolutions = "INSERT INTO " + schemeName + ".solutions (reference_id, problem_id, points, hint_used) " +
                "VALUES" + "(?,?,?,?)";

        try (PreparedStatement preparedStatementInsertSolutions = connection.prepareStatement(insertTableSolutions, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatementInsertSolutions.setInt(1, referenceId);
            preparedStatementInsertSolutions.setInt(2, problemId);
            preparedStatementInsertSolutions.setInt(3, points);
            preparedStatementInsertSolutions.setBoolean(4, hintUsed);
            preparedStatementInsertSolutions.executeUpdate();
        }
    }

    private int getScenarioId(String scenario, String generatorId, ResultSet scenarioExists) throws SQLException {
        if (!scenarioExists.next()) {
            String insertTableScenarios = "INSERT INTO " + schemeName + ".scenarios (scenario, generator_id) " +
                    "VALUES" + "(?,?)";

            try (PreparedStatement preparedStatementInsertScenarios = connection.prepareStatement(insertTableScenarios, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementInsertScenarios.setString(1, scenario);
                preparedStatementInsertScenarios.setString(2, generatorId);
                preparedStatementInsertScenarios.executeUpdate();
                ResultSet scenarioIdQuery = preparedStatementInsertScenarios.getGeneratedKeys();
                if (scenarioIdQuery.next()) {
                    return scenarioIdQuery.getInt("scenario_id");
                } else {
                    throw new IllegalStateException();
                }
            }
        } else {
            return scenarioExists.getInt("scenario_id");
        }
    }

    private int getProblemId(String problemText, String difficulty, int scenarioId, ResultSet problemExists) throws SQLException {
        ResultSet problemLastId;
        String insertTableProblems = "INSERT INTO " + schemeName + ".problems (problem_text, difficulty, scenario_id) " +
                "VALUES" + "(?,?,?)";

        if (!problemExists.next()) {
            try (PreparedStatement preparedStatementInsertProblems = connection.prepareStatement(insertTableProblems, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatementInsertProblems.setString(1, problemText);
                preparedStatementInsertProblems.setString(2, difficulty);
                preparedStatementInsertProblems.setInt(3, scenarioId);
                preparedStatementInsertProblems.executeUpdate();
                problemLastId = preparedStatementInsertProblems.getGeneratedKeys();
                if (problemLastId.next()) {
                    return problemLastId.getInt("problem_id");
                } else {
                    throw new IllegalStateException();
                }
            }
        } else {
            return problemExists.getInt("problem_id");
        }
    }

    public Map<String, UserInfo> getUserInfoFromDB() {
        if (connection == null) {
            System.out.println("Could not connect to DB");
            return new HashMap<>();
        }

        Map<String, UserInfo> dbUserInfo = new HashMap<>();

        Problem problem;

        String currentDeviceId;
        String theme;
        String problemЕext;
        String difficultyText;
        ProblemScenario scenario;

        String selectAllTables = "select " + schemeName + ".reference.device_id, username, problem_text, difficulty, scenario, points, hint_used, generator_id\n" +
                "from " + schemeName + ".reference, " + schemeName + ".devices, " + schemeName + ".users, " + schemeName + ".problems, " + schemeName + ".solutions, " + schemeName + ".scenarios\n" +
                "where " + schemeName + ".reference.device_id = " + schemeName + ".devices.device_id and " + schemeName + ".users.user_id = " + schemeName + ".reference.user_id\n" +
                "and " + schemeName + ".solutions.reference_id = " + schemeName + ".reference.reference_id and " + schemeName + ".solutions.problem_id = " + schemeName + ".problems.problem_id\n" +
                "and " + schemeName + ".problems.scenario_id = " + schemeName + ".scenarios.scenario_id";

        try (Statement selectAllTablesQuery = connection.createStatement()) {
            ResultSet allTables = selectAllTablesQuery.executeQuery(selectAllTables);
            while (allTables.next()) {
                currentDeviceId = allTables.getString("device_id");
                theme = allTables.getString("generator_id");
                scenario = new ProblemScenarioImpl(allTables.getString("scenario"));

                final boolean hintUsed = allTables.getBoolean("hint_used");
                final int points = allTables.getInt("points");

                problemЕext = allTables.getString("problem_text");
                difficultyText = allTables.getString("difficulty");

                problem = new ProblemWithPossibleTextAnswers.Builder().text(problemЕext).answer(0).theme(theme)
                        .possibleTextAnswers(null).hint("").scenario(scenario).difficulty(Problem.Difficulty.valueOf(difficultyText)).create();
                problem.setState(points <= 0 ? Problem.State.ANSWER_GIVEN : (hintUsed ? Problem.State.SOLVED_WITH_HINT : Problem.State.SOLVED));

                final UserInfo userInfo;
                if (dbUserInfo.containsKey(currentDeviceId)) {
                    userInfo = dbUserInfo.get(currentDeviceId);
                    dbUserInfo.put(currentDeviceId, userInfo);
                } else {
                    final String username = allTables.getString("username");

                    userInfo = new UserInfo(currentDeviceId, username);
                    System.out.println("userInfo = " + userInfo);
                    dbUserInfo.put(currentDeviceId, userInfo);
                }
                userInfo.addSolvedProblem(theme, problem, points);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbUserInfo;
    }
}
