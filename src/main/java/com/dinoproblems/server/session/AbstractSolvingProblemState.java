package com.dinoproblems.server.session;

import com.dinoproblems.server.DataBaseService;
import com.dinoproblems.server.Problem;
import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import static com.dinoproblems.server.session.Session.YES_ANSWERS;
import static com.dinoproblems.server.session.Session.checkAnswer;
import static com.dinoproblems.server.session.Session.createButton;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;

/**
 * Created by Katushka
 * on 13.06.2019.
 */
public abstract class AbstractSolvingProblemState implements SessionState {
    final static Set<String> ASK_HINT = Sets.newHashSet("подсказка", "подсказку", "сказать подсказку", "помощь",
            "дать подсказку", "дай", "давай", "дай подсказку", "подскажи", "есть еще подсказка", "помоги");
    final static Set<String> ASK_TO_REPEAT = Sets.newHashSet("повтори", "повторить", "повтори задачу", "повтори условие",
            "еще раз", "прочитай еще раз", "расскажи еще раз", "повтори еще раз", "задачу повтори", "еще раз повтори",
            "можешь повторить", "повторить задачу");
    final static Set<String> ASK_ANSWER = Sets.newHashSet("ответ", "сдаюсь", "сказать ответ", "скажи ответ",
            "скажи решение", "можно решение", "расскажи мне какой ответ", "какой ответ", "скажи ответ пожалуйста",
            "можно ответ сказать", "дай ответ", "скажи мне ответ", "я хочу узнать ответ", "хочу узнать ответ");
    private static final HashSet<String> ANOTHER_PROBLEM = Sets.newHashSet("другую", "другая", "другую задачу",
            "другая задача", "новую задачу", "дай другую задачу", "дай мне новую задачу", "дай другую", "дай другую задачу");
    final static Set<String> BACK_TO_MENU = Sets.newHashSet("вернуться в меню", "меню", "назад в меню", "назад",
            "в меню", "вернись в меню", "вернись меню", "вернемся в меню");
    final static String[] NOT_A_NUMBER = {"Это точно не ответ на задачу. ", "Даже не знаю, как на это реагировать. ",
            "Я почти уверена, что в ответе на задачу должно быть число. ", "Всё ещё жду ответа на задачу. "};
    final static String[] ALMOST = {"Почти.", "Почти верно.", "Близко, но нет."};
    private final static String[] PROPOSE_ANSWER_OR_HINT = {"Хотите скажу подсказку или повторю задачу?", "Я могу повторить задачу.", "Могу дать вам подсказку.",
            "Задача не из лёгких, но я могу помочь", "Я могу подсказать."};
    final static String[] WRONG_ANSWER = {"Нет, это неверно.", "Неверно.", "Это неправильный ответ.", "Нет.", "Нет, это точно неправильно."};
    final static TextWithTTSBuilder[] DONT_GIVE_ANOTHER = new TextWithTTSBuilder[]{
            new TextWithTTSBuilder().append("Сначала надо решить эту задачу. Я верю, у вас получится, если хорошенько подумать. "),
            new TextWithTTSBuilder().append("Нет, сначала давайте решим эту задачу. Я могу помочь. "),
            new TextWithTTSBuilder().append("Сначала надо разобраться с моей задачей. Я могу её повторить или дать подсказку. "),
            new TextWithTTSBuilder().append("Боюсь, что я не могу дать вам другую задачу, пока вы не решите эту. Могу дать вам подсказку. "),
            new TextWithTTSBuilder().append("К сожалению, мои программисты запретили мне давать новую задачу, пока вы не решите ").append("эту", "+эту").append(". ")};
    private final static String[] ALL_HINT_ARE_GIVEN = {"Я уже давала вам подсказку. ", "У меня закончились подсказки, но могу повторить ещё раз. ",
            "Больше подсказок нет, но могу повторить. ", "Подсказка была такая. ", "К сожалению, больше подсказок нет, но могу повторить. "};
    private final static String[] NEXT_HINT = {"Следующая подсказка. ", "У меня есть ещё одна подсказка. ",
            "Одну подсказку я вам уже давала, но могу подсказать ещё. ", "Слушайте следующую подсказку. ",
            "Вам повезло, для этой задачи у меня есть ещё одна подсказка. "};
    final static String[] PRAISES = {"Да, верно!", "Это правильный ответ.", "Ну конечно! Так и есть.",
            "Правильно!", "Верно! ", "Точно!", "Абсолютно верно! ", "Без сомнений, так и есть!"};
    final static String[] PRAISE_SHORT = {"Отлично! ", "Здорово! ", "Отличный результат! ", "Молодец! ", "Класс! ",
            "Поздравляю! ", "У вас отлично получается! ", "Я не сомневалась, что вы справитесь.", };
    final static String[] SOUND_PRAISES = {"<speaker audio=\"alice-sounds-game-win-1.opus\">",
            "<speaker audio=\"alice-sounds-game-win-2.opus\">",
            "<speaker audio=\"alice-sounds-game-win-3.opus\">"};

    @Nullable
    private final TextWithTTSBuilder text;
    private final boolean showProblemText;
    private final boolean sayHint;
    private final boolean sayAnswer;
    private final boolean problemSolved;
    private final Problem solvedProblem;
    private final boolean sayComment;

    AbstractSolvingProblemState(TextWithTTSBuilder text, boolean showProblemText, boolean sayHint, boolean sayAnswer, boolean problemSolved, Problem solvedProblem, boolean sayComment) {
        this.text = text;
        this.showProblemText = showProblemText;
        this.sayHint = sayHint;
        this.sayAnswer = sayAnswer;
        this.problemSolved = problemSolved;
        this.solvedProblem = solvedProblem;
        this.sayComment = sayComment;
    }

    @Nonnull
    SessionState getNextState(Problem currentProblem, String command, JsonArray entitiesArray, Session session, String timeZone) {
        if (checkAnswer(command, ANOTHER_PROBLEM) || checkAnswer(command, ANOTHER_PROBLEM, YES_ANSWERS)) {
            return rejectToSayAnotherState();
        } else if (checkAnswer(command, ASK_HINT, YES_ANSWERS)) {
            return sayHintState(null);
        } else if (checkAnswer(command, ASK_TO_REPEAT, YES_ANSWERS)) {
            return repeatProblemState();
        } else if (checkAnswer(command, ASK_ANSWER, YES_ANSWERS)) {
            if (session.getUserInfo().hasHint(currentProblem)) {
                return sayHintState("Давайте дам вам подсказку. ");
            } else {
                return getStateWhenAnswerIsAsked(currentProblem, session);
            }
        } else {
            CorrectAnswer correctAnswer = checkProblemAnswer(currentProblem, command, entitiesArray);

            if (correctAnswer == CorrectAnswer.CORRECT) {
                System.out.println("Correct simple problem");
                return getNextStateWhenCorrectAnswerIsGiven(currentProblem, session, timeZone);
            } else {
                final String text;
                if (correctAnswer == CorrectAnswer.NOT_A_NUMBER) {
                    text = chooseRandomElement(NOT_A_NUMBER) + " " + chooseRandomElement(PROPOSE_ANSWER_OR_HINT);
                } else {
                    text = chooseRandomElement(correctAnswer == CorrectAnswer.ALMOST_CORRECT ? ALMOST : WRONG_ANSWER) + " " + chooseRandomElement(PROPOSE_ANSWER_OR_HINT);
                }
                return incorrectAnswerSessionState(session, text);
            }
        }
    }

    abstract SessionState incorrectAnswerSessionState(Session session, String text);

    CorrectAnswer checkProblemAnswer(Problem currentProblem, String command, JsonArray entitiesArray) {
        CorrectAnswer correctAnswer = currentProblem.checkAnswer(command) ? CorrectAnswer.CORRECT : CorrectAnswer.NOT_A_NUMBER;

        if (correctAnswer != CorrectAnswer.CORRECT && currentProblem.isNumericAnswer()) {
            for (JsonElement jsonElement : entitiesArray) {
                final String type = jsonElement.getAsJsonObject().get("type").getAsString();
                if (type.equalsIgnoreCase("YANDEX.NUMBER")) {
                    final int value = jsonElement.getAsJsonObject().get("value").getAsInt();
                    if (currentProblem.getNumericAnswer() == value) {
                        correctAnswer = CorrectAnswer.CORRECT;
                        break;
                    }
                    if (Math.abs(value - currentProblem.getNumericAnswer()) <= Math.ceil(currentProblem.getNumericAnswer() * 0.05)) {
                        correctAnswer = CorrectAnswer.ALMOST_CORRECT;
                    }
                    if (correctAnswer == CorrectAnswer.NOT_A_NUMBER) {
                        correctAnswer = CorrectAnswer.INCORRECT_NUMBER;
                    }
                }
            }

        }
        return correctAnswer;
    }

    enum CorrectAnswer {
        CORRECT, ALMOST_CORRECT, INCORRECT_NUMBER, NOT_A_NUMBER
    }

    void processRequest(Problem currentProblem, JsonObject request, Session session, String timeZone) {
        if (showProblemText) {
            addProblemTextToResponse(request, session, text == null ? null : text.getText(), timeZone, currentProblem);
        } else if (sayHint) {
            if (!session.getUserInfo().hasHint(currentProblem)) {
                giveHint(request, session, currentProblem, chooseRandomElement(ALL_HINT_ARE_GIVEN), session.getUserInfo().getLastHint(currentProblem));
            } else {
                if (text != null && text.getText() != null) {
                    giveHint(request, session, currentProblem, text.getText(), session.getUserInfo().getNextHint(currentProblem));
                } else if (session.getUserInfo().wasHintGiven(currentProblem)) {
                    giveHint(request, session, currentProblem, chooseRandomElement(NEXT_HINT), session.getUserInfo().getNextHint(currentProblem));
                } else {
                    giveHint(request, session, currentProblem, "", session.getUserInfo().getNextHint(currentProblem));
                }
            }
        } else if (sayAnswer || problemSolved) {
            processProblemEnding(request, session, solvedProblem, timeZone);
        } else {
            request.addProperty("text", text == null ? "" : text.getText());
            if (text != null && text.getTTS() != null) {
                request.addProperty("tts", text.getTTS());
            }

            if (currentProblem != null) {
                addProblemButtons(request, currentProblem, session);
            }
        }
    }

    protected abstract void processProblemEnding(JsonObject request, Session session, Problem solvedProblem, String timeZone);

    void giveHint(JsonObject responseJson, Session session, Problem problem, @Nonnull String prefix, String hint) {
        responseJson.addProperty("text", prefix + hint);
        session.setLastServerResponse(prefix + hint);
        addProblemButtons(responseJson, problem, session);
    }

    void addProblemTextToResponse(JsonObject responseJson, Session session, @Nullable String prefix, String timeZone, Problem problem) {
        if (problem == null) {
            // should never come here
            new ChooseDifficultySessionState(session.getCurrentDifficulty()).processRequest(responseJson, session, timeZone);
            final String text = "Извините, но вы уже решили все мои задачи на этом уровне сложности. Может быть, порешаем задачи другой сложности?";
            responseJson.addProperty("text", text);
        } else {
            sayProblemText(responseJson, session, prefix, problem);
        }
    }

    void sayProblemText(JsonObject responseJson, Session session, @Nullable String prefix, Problem problem) {
        final String text = (prefix != null ? prefix : "") +
                (sayComment && problem.getComment() != null ? (problem.getComment() + " ") : "") + problem.getText();
        responseJson.addProperty("text", text);
        addProblemButtons(responseJson, problem, session);
        if (problem.getTTS() != null || (sayComment && problem.getCommentTTS() != null)) {
            responseJson.addProperty("tts", (prefix != null ? prefix : "") +
                    (sayComment && problem.getCommentTTS() != null ? problem.getCommentTTS() :
                            (sayComment && problem.getComment() != null ? (problem.getComment() + " ") : ""))
                    + (problem.getTTS() != null ? problem.getTTS() : problem.getText()));
        }
        session.setLastServerResponse(text);
    }

    private void addProblemButtons(JsonObject responseJson, Problem problem, Session session) {
        final JsonArray buttons = new JsonArray();
        buttons.add(createButton("Повторить", true));
        buttons.add(createButton("Подсказка", true));
        if (!session.getUserInfo().hasHint(problem)) {
            buttons.add(createButton("Сказать ответ", true));
        }
        responseJson.add("buttons", buttons);
    }

    protected abstract SessionState getNextStateWhenCorrectAnswerIsGiven(Problem currentProblem, Session session, String timeZone);

    protected abstract SessionState getStateWhenAnswerIsAsked(Problem currentProblem, Session session);

    protected void finishWithProblem(Session session, Problem problem, UserInfo.ProblemState state, String clientId) {
        System.out.println("Finish with problem: " + session.getUserInfo().getProblemState(problem));
        System.out.println("Problem text: " + problem.getText());
        if (session.getUserInfo().getProblemState(problem) == null) {
            session.getUserInfo().setProblemState(problem, state);
            final int points = session.updateScore(problem);

            DataBaseService.INSTANCE.insertSessionInfo(session.getUserInfo().getDeviceId(), clientId, problem,
                    session.getUserInfo().getName(),
                    points, session.getUserInfo().wasHintGiven(problem));
        }
    }

    @Nonnull
    protected abstract SessionState sayAnswerState(Problem currentProblem);

    @Nonnull
    protected abstract SessionState repeatProblemState();

    @Nonnull
    protected abstract SessionState sayHintState(String prefix);

    @Nonnull
    protected abstract SessionState rejectToSayAnotherState();

    public TextWithTTSBuilder getText() {
        return text;
    }

    public boolean isShowProblemText() {
        return showProblemText;
    }

    public boolean isSayHint() {
        return sayHint;
    }

    public boolean isSayAnswer() {
        return sayAnswer;
    }

    public boolean isProblemSolved() {
        return problemSolved;
    }

    public Problem getSolvedProblem() {
        return solvedProblem;
    }
}
