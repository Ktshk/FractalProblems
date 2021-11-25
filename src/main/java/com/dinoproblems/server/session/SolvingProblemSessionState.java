package com.dinoproblems.server.session;

import com.dinoproblems.server.DataBaseService;
import com.dinoproblems.server.Problem;
import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
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
public class SolvingProblemSessionState extends AbstractSolvingProblemState {

    private static final HashSet<String> EASIER = Sets.newHashSet("попроще", "проще", "еще проще", "еще попроще", "более простую задачу", "простую", "простую задачу");
    private static final HashSet<String> HARDER = Sets.newHashSet("сложнее", "посложнее", "более сложную задачу", "сложную", "сложную задачу");
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
    private final static String[] SESSION_SCORE = {"У вас на счету ", "У вас уже ", /*"Вы набрали уже ", */"Ваш счет: ", "У вас "};
    private final static String[] ONE_MORE_QUESTION = {"Хотите ещё задачу?", "Решаем дальше?", "Давайте решим еще одну!", "Предлагаю решить ещё одну."};

    private final boolean showProblemText;

    @Nonnull
    @Override
    public SolvingProblemSessionState sayAnswerState(Problem solvedProblem) {
        return new SolvingProblemSessionState(false, null, null, false, false, true, false, solvedProblem);
    }

    @Nonnull
    private SolvingProblemSessionState problemSolvedState(Problem solvedProblem) {
        return new SolvingProblemSessionState(false, null, null, false, false, false, true, solvedProblem);
    }

    @Nonnull
    @Override
    public SolvingProblemSessionState sayHintState(String prefix) {
        return new SolvingProblemSessionState(false, null, prefix == null ? null : new TextWithTTSBuilder().append(prefix), false, true, false, false, null);
    }

    @Nonnull
    @Override
    public SolvingProblemSessionState rejectToSayAnotherState() {
        return new SolvingProblemSessionState(false, null, GeneratorUtils.chooseRandomElement(DONT_GIVE_ANOTHER), true, false, false, false, null);
    }

    @Nonnull
    @Override
    public SolvingProblemSessionState repeatProblemState() {
        return new SolvingProblemSessionState(false, null, null, true, false, false, false, null);
    }

    private SolvingProblemSessionState(boolean newProblem, Session session, String text, boolean showProblemText) {
        this(newProblem, session, text == null ? null : new TextWithTTSBuilder().append(text), showProblemText, false, false, false, null);
    }

    private SolvingProblemSessionState(boolean newProblem, Session session, TextWithTTSBuilder text, boolean showProblemText, boolean sayHint, boolean sayAnswer, boolean problemSolved, Problem solvedProblem) {
        super(text, showProblemText, sayHint, sayAnswer, problemSolved, solvedProblem, true);
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
    }

    private SolvingProblemSessionState(String text, Session session, boolean showProblemText) {
        this(false, session, text, showProblemText);
    }

    SolvingProblemSessionState(boolean newProblem, Session session) {
        this(newProblem, session, null, true, false, false, false, null);
    }

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone) {
        if (checkAnswer(command, BACK_TO_MENU) || (session.getCurrentProblem() == null && checkAnswer(command, ENOUGH_ANSWERS))) {
            final TextWithTTSBuilder text = session.getSessionResult().getResult(session.getUserInfo().getTotalScore());
            return new MenuSessionState(text);
        } else if (session.getCurrentProblem() == null && session.getNextProblem() == null) {
            return new ChooseDifficultySessionState(null);
        } else if (checkAnswer(command, EASIER, YES_ANSWERS) || checkAnswer(command, EASIER, continueAnswers)) {
            if (session.getCurrentDifficulty() == Problem.Difficulty.EASY) {
                return new SolvingProblemSessionState(chooseRandomElement(DONT_HAVE_EASIER), session, showProblemText);
            } else {
                session.setCurrentDifficulty(session.getCurrentDifficulty().getPrevious());
                return new SolvingProblemSessionState(true, session, chooseRandomElement(EASIER_PROBLEM), true);
            }
        } else {
            final Problem currentProblem = session.getCurrentProblem();
            if (currentProblem == null && (checkAnswer(command, HARDER, YES_ANSWERS) || checkAnswer(command, HARDER, continueAnswers))) {
                if (session.getCurrentDifficulty() == Problem.Difficulty.HARD) {
                    final String text = chooseRandomElement(DONT_HAVE_HARDER);
                    return new SolvingProblemSessionState(text, session, false);
                } else {
                    session.setCurrentDifficulty(session.getCurrentDifficulty().getNext());
                    return new SolvingProblemSessionState(true, session);
                }
            } else if (currentProblem == null && (checkAnswer(command, YES_ANSWERS) || checkAnswer(command, continueAnswers, YES_ANSWERS))) {
                return new SolvingProblemSessionState(true, session);
            } else if (currentProblem == null) {
                final String responseText = chooseRandomElement(DID_NOT_UNDERSTAND) + chooseRandomElement(WANT_A_PROBLEM_QUESTION);
                return new SolvingProblemSessionState(false, session, responseText, false);
            } else {
                return super.getNextState(currentProblem, command, entitiesArray, session, timeZone);
            }
        }
    }

    @Override
    public void processRequest(JsonObject request, Session session, String timeZone) {
        final Problem currentProblem = session.getCurrentProblem();
        processRequest(currentProblem, request, session, timeZone);
        if (currentProblem == null) {
            request.add("buttons", createNewProblemButtons(session, true));
        }
    }

    @Nonnull
    @Override
    public SessionState addTextPrefix(String text) {
        return new SolvingProblemSessionState(false, null,
                this.getText() == null || this.getText().getText() == null ? new TextWithTTSBuilder().append(text) : this.getText().append(text),
                showProblemText, isSayHint(), isSayAnswer(), isProblemSolved(), getSolvedProblem());
    }

    protected SessionState getNextStateWhenCorrectAnswerIsGiven(Problem currentProblem, Session session, String timeZone) {
        finishWithProblem(session, currentProblem, session.getUserInfo().wasHintGiven(currentProblem) ? UserInfo.ProblemState.SOLVED_WITH_HINT : UserInfo.ProblemState.SOLVED, session.getClientId());
        if (session.getNextProblem() == null) {
            return new ChooseDifficultySessionState(getProblemEndText(session, currentProblem) + "Поздравляю! Вы решили все задачи на этом уровне сложности. Порешаем другие задачи?", session.getCurrentDifficulty());
        }
        return problemSolvedState(currentProblem);
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

        buttons.add(createLeaderboardButton(session.getUserInfo(), true, false, false));

        return buttons;
    }

    protected void finishWithProblem(Session session, Problem problem, UserInfo.ProblemState problemState, String clientId) {
        super.finishWithProblem(session, problem, problemState, clientId);
        session.setCurrentProblem(null);
    }

    @Override
    SessionState incorrectAnswerSessionState(Session session, String text)  {
        return new SolvingProblemSessionState(text, session, false);
    }

    protected void processProblemEnding(JsonObject responseJson, Session session, Problem problem, String timeZone) {
        UserInfo.ProblemState problemState = session.getUserInfo().getProblemState(problem);
        String text = getProblemEndText(session, problem);

        text += chooseRandomElement(ONE_MORE_QUESTION);
        responseJson.add("buttons", createNewProblemButtons(session, problemState != UserInfo.ProblemState.ANSWER_GIVEN));
        responseJson.addProperty("text", text);
        if (problemState != UserInfo.ProblemState.ANSWER_GIVEN) {
            responseJson.addProperty("tts", chooseRandomElement(SOUND_PRAISES) + " " + responseJson.get("text"));
        }
    }

    protected SessionState getStateWhenAnswerIsAsked(Problem currentProblem, Session session) {
        finishWithProblem(session, currentProblem, UserInfo.ProblemState.ANSWER_GIVEN, session.getClientId());
        if (session.getNextProblem() == null) {
            return new ChooseDifficultySessionState(getProblemEndText(session, currentProblem) + "Поздравляю! Вы решили все задачи на этом уровне сложности. Порешаем другие задачи?", session.getCurrentDifficulty());
        }
        return sayAnswerState(currentProblem);
    }

    private String getProblemEndText(Session session, Problem problem) {
        UserInfo.ProblemState problemState = session.getUserInfo().getProblemState(problem);

        String text = problemState == UserInfo.ProblemState.ANSWER_GIVEN ? "Правильный ответ " + problem.getTextAnswer() + ". "
                : chooseRandomElement(PRAISES) + " ";

        if (problemState != UserInfo.ProblemState.ANSWER_GIVEN && session.getNextProblem() != null) {
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
