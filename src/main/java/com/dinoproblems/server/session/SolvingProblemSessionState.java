package com.dinoproblems.server.session;

import com.dinoproblems.server.DataBaseService;
import com.dinoproblems.server.Problem;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import static com.dinoproblems.server.session.Session.*;
import static com.dinoproblems.server.utils.Dictionary.PROBLEM;
import static com.dinoproblems.server.utils.Dictionary.SCORE;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;

/**
 * Created by Katushka
 * on 04.06.2019.
 */
public class SolvingProblemSessionState implements SessionState {

    private static final HashSet<String> EASIER = Sets.newHashSet("попроще", "проще", "еще проще", "еще попроще", "более простую задачу", "простую", "простую задачу");
    private static final HashSet<String> HARDER = Sets.newHashSet("сложнее", "посложнее", "более сложную задачу", "сложную", "сложную задачу");
    private static final HashSet<String> ANOTHER_PROBLEM = Sets.newHashSet("другую", "другая", "другую задачу",
            "другая задача", "новую задачу", "дай другую задачу", "дай мне новую задачу", "дай другую", "дай другую задачу");
    private static Set<String> continueAnswers = Sets.newHashSet("продолжим", "продолжаем", "давай задачу", "решаем",
            "дальше", "еще 1", "еще 1 задачу", "давай решим", "давай решим еще 1", "давайте решим еще 1",
            "решаем дальше", "говори", "говори следующую задачу", "хочу решить задачу", "жги", "еще", "решаем решаем",
            "гоу", "поехали", "хотим", "новая задача", "новую задачу");
    private static final String[] DONT_HAVE_EASIER = new String[]{"Извините, но проще уже некуда. Продолжаем на этом уровне сложности? ",
            "Простите, но это самые простые мои задачи, но я верю, что вы сможете их решить, если хорошенько подумаете. Продолжаем? ",
            "К сожалению, проще задач у меня для вас нет. Продолжаем решать? "};
    private static final String[] EASIER_PROBLEM = new String[]{"Хорошо, давайте попробуем попроще. ", "Попроще так попроще. ",
            "Попроще задачи у меня тоже есть. ", "Согласна, давайте сначала потренируемся решать задачи попроще. ",
            "Пожалуй, и правда, задача была сложновата. Но у меня есть и попроще. "};
    private static final String[] DONT_HAVE_HARDER = new String[]{"Давайте попробуем другую задачу, может быть, она будет посложнее. ",
            "Мне кажется, это была довольно сложная задача. Давайте попробуем другую на этом уровне. ",
            "К сожалению, сложнее задач у меня пока нет, но я попрошу программистов их добавить. А пока посмотрим, как вы справитесь со следующей задачей. "};
    private static final String[] WANT_A_PROBLEM_QUESTION = {"Хотите решить задачу?", "Хотите решить ещё одну задачу?", "Продолжаем решать задачи?"};
    private final static ProblemTextBuilder[] DONT_GIVE_ANOTHER = new ProblemTextBuilder[]{
            new ProblemTextBuilder().append("Сначала надо решить эту задачу. Я верю, у вас получится, если хорошенько подумать. "),
            new ProblemTextBuilder().append("Нет, сначала давайте решим эту задачу. Я могу помочь. "),
            new ProblemTextBuilder().append("Сначала надо разобраться с моей задачей. Я могу её повторить или дать подсказку. "),
            new ProblemTextBuilder().append("Боюсь, что я не могу дать вам другую задачу, пока вы не решите эту. Могу дать вам подсказку. "),
            new ProblemTextBuilder().append("К сожалению, мои программисты запретили мне давать новую задачу, пока вы не решите ").append("эту", "+эту").append(". ")};
    private final static Set<String> ASK_HINT = Sets.newHashSet("подсказка", "подсказку", "сказать подсказку", "помощь",
            "дать подсказку", "дай", "давай", "дай подсказку", "подскажи", "есть еще подсказка", "помоги");
    private final static Set<String> ASK_TO_REPEAT = Sets.newHashSet("повтори", "повторить", "повтори задачу", "повтори условие",
            "еще раз", "прочитай еще раз", "расскажи еще раз", "повтори еще раз", "задачу повтори", "еще раз повтори",
            "можешь повторить");
    private final static String[] ALL_HINT_ARE_GIVEN = {"Я уже давала вам подсказку. ", "У меня закончились подсказки, но могу повторить ещё раз. ",
            "Больше подсказок нет, но могу повторить. ", "Подсказка была такая. ", "К сожалению, больше подсказок нет, но могу повторить. "};
    private final static String[] NEXT_HINT = {"Следующая подсказка. ", "У меня есть ещё одна подсказка. ",
            "Одну подсказку я вам уже давала, но могу подсказать ещё. ", "Слушайте следующую подсказку. ",
            "Вам повезло, для этой задачи у меня есть ещё одна подсказка. "};
    private final static Set<String> ASK_ANSWER = Sets.newHashSet("ответ", "сдаюсь", "сказать ответ", "скажи ответ",
            "скажи решение", "можно решение", "расскажи мне какой ответ", "какой ответ", "скажи ответ пожалуйста",
            "можно ответ сказать", "дай ответ", "скажи мне ответ", "я хочу узнать ответ", "хочу узнать ответ");
    private final static String[] PRAISES = {"Да, верно!", "Это правильный ответ.", "Ну конечно! Так и есть.",
            "Я не сомневалась, что у вы справитесь.", "Правильно!", "Верно! ", "Точно!", "Абсолютно верно! "};
    private final static String[] PRAISE_SHORT = {"Отлично! ", "Здорово! ", "Отличный результат! ", "Молодец! ", "Класс! ",
            "Поздравляю! ", "У вас отлично получается! "};
    private final static String[] SOUND_PRAISES = {"<speaker audio=\"alice-sounds-game-win-1.opus\">",
            "<speaker audio=\"alice-sounds-game-win-2.opus\">",
            "<speaker audio=\"alice-sounds-game-win-3.opus\">"};
    private final static String[] SESSION_SCORE = {"У вас на счету ", "У вас уже ", /*"Вы набрали уже ", */"Ваш счет: ", "У вас "};
    private final static String[] ONE_MORE_QUESTION = {"Хотите ещё задачу?", "Решаем дальше?", "Давайте решим еще одну!", "Предлагаю решить ещё одну."};
    private final static String[] NOT_A_NUMBER = {"Это точно не ответ на задачу. ", "Даже не знаю, как на это реагировать. ",
            "Я почти уверена, что в ответе на задачу должно быть число. ", "Всё ещё жду ответа на задачу. "};
    private final static String[] ALMOST = {"Почти.", "Почти верно.", "Близко, но нет."};
    private final static String[] PROPOSE_ANSWER_OR_HINT = {"Хотите скажу подсказку или повторю задачу?", "Я могу повторить задачу.", "Могу дать вам подсказку.",
            "Задача не из лёгких, но я могу помочь", "Я могу подсказать."};
    private final static String[] WRONG_ANSWER = {"Нет, это неверно.", "Неверно.", "Это неправильный ответ.", "Нет.", "Нет, это точно неправильно."};

    private final ProblemTextBuilder text;
    private final boolean showProblemText;
    private final boolean sayHint;
    private final boolean sayAnswer;
    private final boolean problemSolved;
    private final Problem solvedProblem;

    public static SolvingProblemSessionState sayAnswerState(Problem solvedProblem) {
        return new SolvingProblemSessionState(false, null, null, false, false, true, false, solvedProblem);
    }

    public static SolvingProblemSessionState problemSolvedState(Problem solvedProblem) {
        return new SolvingProblemSessionState(false, null, null, false, false, false, true, solvedProblem);
    }

    public static SolvingProblemSessionState sayHintState(String prefix) {
        return new SolvingProblemSessionState(false, null, prefix == null ? null : new ProblemTextBuilder().append(prefix), false, true, false, false, null);
    }

    public static SolvingProblemSessionState rejectToSayAnotherState() {
        return new SolvingProblemSessionState(false, null, GeneratorUtils.chooseRandomElement(DONT_GIVE_ANOTHER), true, false, false, false, null);
    }

    public static SolvingProblemSessionState repeatProblemState() {
        return new SolvingProblemSessionState(false, null, null, true, false, false, false, null);
    }

    public SolvingProblemSessionState(boolean newProblem, Session session, String text, boolean showProblemText) {
        this(newProblem, session, text == null ? null : new ProblemTextBuilder().append(text), showProblemText, false, false, false, null);
    }

    private SolvingProblemSessionState(boolean newProblem, Session session, ProblemTextBuilder text, boolean showProblemText, boolean sayHint, boolean sayAnswer, boolean problemSolved, Problem solvedProblem) {
        this.sayHint = sayHint;
        this.sayAnswer = sayAnswer;
        this.problemSolved = problemSolved;
        this.solvedProblem = solvedProblem;
        if (newProblem) {
            final Problem problem;
            if (session.getCurrentProblem() == null) {
                problem = session.getNextProblem();
            } else {
                problem = session.getCurrentProblem();
            }
            session.setCurrentProblem(problem);
            this.showProblemText = true;
        } else {
            this.showProblemText = showProblemText;
        }

        this.text = text;
    }

    public SolvingProblemSessionState(String text, Session session, boolean showProblemText) {
        this(false, session, text, showProblemText);
    }

    public SolvingProblemSessionState(boolean newProblem, Session session, boolean showProblemText) {
        this(newProblem, session, null, showProblemText, false, false, false, null);
    }

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session) {
        if (session.getNextProblem() == null) {
            return new ChooseDifficultySessionState();
        } else if (checkAnswer(command, EASIER, YES_ANSWERS) || checkAnswer(command, EASIER, continueAnswers)) {
            if (session.getCurrentDifficulty() == Problem.Difficulty.EASY) {
                return new SolvingProblemSessionState(chooseRandomElement(DONT_HAVE_EASIER), session, showProblemText);
            } else {
                session.setCurrentDifficulty(session.getCurrentDifficulty().getPrevious());
                return new SolvingProblemSessionState(chooseRandomElement(EASIER_PROBLEM), session, showProblemText);
            }
        } else {
            final Problem currentProblem = session.getCurrentProblem();
            if (currentProblem == null && (checkAnswer(command, HARDER, YES_ANSWERS) || checkAnswer(command, HARDER, continueAnswers))) {
                if (session.getCurrentDifficulty() == Problem.Difficulty.EXPERT) {
                    final String text = chooseRandomElement(DONT_HAVE_HARDER);
                    return new SolvingProblemSessionState(true, session, text, showProblemText);
                } else {
                    session.setCurrentDifficulty(session.getCurrentDifficulty().getNext());
                    return new SolvingProblemSessionState(true, session, showProblemText);
                }
            } else if (currentProblem == null && (checkAnswer(command, YES_ANSWERS) || checkAnswer(command, continueAnswers, YES_ANSWERS))) {
                return new SolvingProblemSessionState(true, session, showProblemText);
            } else if (currentProblem == null) {
                DataBaseService.INSTANCE.updateMiscAnswersTable(command, "", session.getLastServerResponse());

                final String responseText = chooseRandomElement(DID_NOT_UNDERSTAND) + chooseRandomElement(WANT_A_PROBLEM_QUESTION);
                return new SolvingProblemSessionState(false, session, responseText, false);
            } else if (checkAnswer(command, ANOTHER_PROBLEM) || checkAnswer(command, ANOTHER_PROBLEM, YES_ANSWERS)) {
                return rejectToSayAnotherState();
            } else if (checkAnswer(command, ASK_HINT, YES_ANSWERS)) {
                return sayHintState(null);
            } else if (checkAnswer(command, ASK_TO_REPEAT, YES_ANSWERS)) {
                return repeatProblemState();
            } else if (checkAnswer(command, ASK_ANSWER, YES_ANSWERS)) {
                if (currentProblem.hasHint()) {
                    return sayHintState("Давайте дам вам подсказку. ");
                } else {
                    finishWithProblem(session, currentProblem, Problem.State.ANSWER_GIVEN, session.getClientId());
                    if (session.getNextProblem() == null) {
                        return new ChooseDifficultySessionState(getProblemEndText(session, currentProblem) + "Поздравляю! Вы решили все задачи на этом уровне сложности. Порешаем другие задачи?");
                    }
                    return sayAnswerState(currentProblem);
                }
            } else {
                boolean correctAnswer = currentProblem.checkAnswer(command);
                boolean almostCorrect = false;
                boolean numberFound = false;

                if (!correctAnswer && currentProblem.isNumericAnswer()) {
                    for (JsonElement jsonElement : entitiesArray) {
                        final String type = jsonElement.getAsJsonObject().get("type").getAsString();
                        if (type.equalsIgnoreCase("YANDEX.NUMBER")) {
                            final int value = jsonElement.getAsJsonObject().get("value").getAsInt();
                            if (currentProblem.getNumericAnswer() == value) {
                                correctAnswer = true;
                            }
                            if (Math.abs(value - currentProblem.getNumericAnswer()) <= Math.ceil(currentProblem.getNumericAnswer() * 0.05)) {
                                almostCorrect = true;
                            }
                            numberFound = true;
                        }
                    }

                }

                if (correctAnswer) {
                    finishWithProblem(session, currentProblem, currentProblem.wasHintGiven() ? Problem.State.SOLVED_WITH_HINT : Problem.State.SOLVED, session.getClientId());
                    if (session.getNextProblem() == null) {
                        return new ChooseDifficultySessionState(getProblemEndText(session, currentProblem) + "Поздравляю! Вы решили все задачи на этом уровне сложности. Порешаем другие задачи?");
                    }
                    return problemSolvedState(currentProblem);
                } else {
                    DataBaseService.INSTANCE.updateMiscAnswersTable(command, currentProblem.getText(), session.getLastServerResponse());

                    final String text;
                    if (!numberFound) {
                        text = chooseRandomElement(NOT_A_NUMBER) + " " + chooseRandomElement(PROPOSE_ANSWER_OR_HINT);
                    } else {
                        text = chooseRandomElement(almostCorrect ? ALMOST : WRONG_ANSWER) + " " + chooseRandomElement(PROPOSE_ANSWER_OR_HINT);
                    }
                    return new SolvingProblemSessionState(text, session, false);
                }
            }
        }
    }

    @Override
    public void processRequest(String command, JsonObject request, Session session) {
        final Problem currentProblem = session.getCurrentProblem();
        if (showProblemText) {
            addProblemTextToResponse(request, session, text == null ? null : text.getText());
        } else if (sayHint) {
            if (!currentProblem.hasHint()) {
                giveHint(request, session, currentProblem, chooseRandomElement(ALL_HINT_ARE_GIVEN), currentProblem.getLastHint());
            } else {
                if (text != null && text.getText() != null) {
                    giveHint(request, session, currentProblem, text.getText(), currentProblem.getNextHint());
                } else if (currentProblem.wasHintGiven()) {
                    giveHint(request, session, currentProblem, chooseRandomElement(NEXT_HINT), currentProblem.getNextHint());
                } else {
                    giveHint(request, session, currentProblem, "", currentProblem.getNextHint());
                }
            }
        } else if (sayAnswer || problemSolved) {
            processProblemEnding(request, session, solvedProblem);
        } else {
            request.addProperty("text", text == null ? "" :  text.getText());
            if (text != null && text.getTTS() != null) {
                request.addProperty("text", text.getTTS());
            }

            if (session.getCurrentProblem() != null) {
                addProblemButtons(request, session.getCurrentProblem());
            } else {
                request.add("buttons", createNewProblemButtons(session, true));
            }
        }
    }

    private void addProblemTextToResponse(JsonObject responseJson, Session session, @Nullable String prefix) {
        Problem problem = session.getCurrentProblem();
        if (problem == null) {
            // should never come here
            new ChooseDifficultySessionState().processRequest(null, responseJson, session);
            final String text = "Извините, но вы уже решили все мои задачи на этом уровне сложности. Может быть, порешаем задачи другой сложности?";
            responseJson.addProperty("text", text);
        } else {
            final String text = (prefix != null ? prefix : "") +
                    (problem.getComment() != null ? (problem.getComment() + " ") : "") + problem.getText();
            responseJson.addProperty("text", text);
            addProblemButtons(responseJson, problem);
            if (problem.getTTS() != null || problem.getCommentTTS() != null) {
                responseJson.addProperty("tts", (prefix != null ? prefix : "") +
                        (problem.getCommentTTS() != null ? problem.getCommentTTS() :
                                (problem.getComment() != null ? (problem.getComment() + " ") : ""))
                        + (problem.getTTS() != null ? problem.getTTS() : problem.getText()));
            }
            session.setLastServerResponse(text);
        }
    }

    private void addProblemButtons(JsonObject responseJson, Problem problem) {
        final JsonArray buttons = new JsonArray();
        buttons.add(createButton("Повторить", true));
        buttons.add(createButton("Подсказка", true));
        if (!problem.hasHint()) {
            buttons.add(createButton("Сказать ответ", true));
        }
        responseJson.add("buttons", buttons);
    }

    private JsonArray createNewProblemButtons(Session session, boolean lastProblemSolved) {
        final JsonArray buttons = new JsonArray();
        final Problem.Difficulty currentDifficulty = session.getCurrentDifficulty();

        buttons.add(createButton("новая задача", true));

        if (currentDifficulty != Problem.Difficulty.EASY) {
            buttons.add(createButton("попроще", true));
        }
        if (currentDifficulty != Problem.Difficulty.EXPERT && lastProblemSolved) {
            buttons.add(createButton("посложнее", true));
        }

        buttons.add(createLeaderboardButton(session.getUserInfo(), true));

        return buttons;
    }

    private void giveHint(JsonObject responseJson, Session session, Problem problem, @Nonnull String prefix, String hint) {
        responseJson.addProperty("text", prefix + hint);
        session.setLastServerResponse(prefix + hint);
        addProblemButtons(responseJson, problem);
    }

    private void finishWithProblem(Session session, Problem problem, Problem.State problemState, String clientId) {
        problem.setState(problemState);
        final int points = session.updateScore(problem);
        session.setCurrentProblem(null);

        DataBaseService.INSTANCE.insertSessionInfo(session.getUserInfo().getDeviceId(), clientId, problem.getText(),
                problem.getDifficulty().toString(), session.getUserInfo().getName(), problem.getProblemScenario().getScenarioId(),
                problem.getTheme(), points, problem.wasHintGiven());
    }

    private void processProblemEnding(JsonObject responseJson, Session session, Problem problem) {
        Problem.State problemState = problem.getState();
        String text = getProblemEndText(session, problem);

        text += chooseRandomElement(ONE_MORE_QUESTION);
        responseJson.add("buttons", createNewProblemButtons(session, problemState != Problem.State.ANSWER_GIVEN));
        responseJson.addProperty("text", text);
        if (problemState != Problem.State.ANSWER_GIVEN) {
            responseJson.addProperty("tts", chooseRandomElement(SOUND_PRAISES) + " " + responseJson.get("text"));
        }
    }

    private String getProblemEndText(Session session, Problem problem) {
        Problem.State problemState = problem.getState();

        String text = problemState == Problem.State.ANSWER_GIVEN ? "Правильный ответ " + problem.getTextAnswer() + ". "
                : chooseRandomElement(PRAISES) + " ";

        if (problemState != Problem.State.ANSWER_GIVEN && session.getNextProblem() != null) {
            final int solvedInARow = session.getSessionResult().getSolvedInARow();
            if (solvedInARow > 0 && solvedInARow % 3 == 0) {
                text += "Вы решили " + getNumWithString(solvedInARow, PROBLEM) + " подряд. " + chooseRandomElement(PRAISE_SHORT);
            } else /*if (randomInt(0, 3) == 0)*/ {
                text += chooseRandomElement(SESSION_SCORE) + getNumWithString(session.getUserInfo().getTotalScore(), SCORE) + ". " + chooseRandomElement(PRAISE_SHORT);
            }
        }
        return text;
    }


}
