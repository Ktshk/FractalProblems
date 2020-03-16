package com.dinoproblems.server.session;

import com.dinoproblems.server.DataBaseService;
import com.dinoproblems.server.Problem;
import com.dinoproblems.server.generators.QuestProblems;
import com.dinoproblems.server.generators.QuestProblemsLoader;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import static com.dinoproblems.server.session.Session.*;
import static com.dinoproblems.server.utils.Dictionary.SCORE;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;

/**
 * Created by Katushka
 * on 11.06.2019.
 */
public class ProblemOfTheDaySessionState extends AbstractSolvingProblemState {
    private final static String[] HAS_NO_PROBLEM_OF_THE_DAY = {"Сегодняшнюю задачу вы уже решили. Завтра я придумаю для вас что-нибудь новенькое. ",
            "Я очень рада, что вам нравится решать сложные задачи, я попрошу моих программистов придумать для вас новую завтра. ",
    };
    private final static String[] ASK_TO_BE_BACK_TOMORROW = {"Заходите завтра за новой задачей. ",
            "Буду ждать вас завтра. ", "Буду рада слышать вас завтра. ", "Завтра постараюсь придумать для вас что-нибудь посложнее. "
    };
    private final static String[] ASK_TO_BE_BACK_TOMORROW_WHEN_NOT_SOLVED = {"Уверена, что завтра вы решите мою задачу дня. ",
            "Не сомневаюсь, что завтра у вас получится решить задачу дня. "
    };
    private final static String[] ASK_TO_BE_BACK = {"Заходите еще. Эта задача будет ждать вас до завтра. ",
            "Задача непростая, подумайте над ней и заходите попозже, я буду ждать. ",
            "Да, чтобы решить эту задачу нужно время. С нетерпением буду ждать вас сегодня. "
    };

    private final static Set<String> ASK_SOLUTION = Sets.newHashSet("решение", "сказать решение", "скажи решение",
            "можно решение", "расскажи мне какое решение", "какое решение", "расскажи решение",
            "можно решение сказать", "расскажи мне решение", "я хочу узнать решение", "хочу узнать решение");


    private final boolean start;
    private final boolean saySolution;

    ProblemOfTheDaySessionState(boolean start) {
        super(null, true, false, false, false, null);
        this.start = start;
        this.saySolution = false;
    }

    private ProblemOfTheDaySessionState(TextWithTTSBuilder text, boolean showProblemText, boolean sayHint, boolean sayAnswer, Problem solvedProblem, boolean saySolution) {
        super(text, showProblemText, sayHint, sayAnswer, false, solvedProblem);
        this.saySolution = saySolution;
        this.start = false;
    }

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone) {
        final Problem problemOfTheDay = session.getUserInfo().getProblemOfTheDay(timeZone);
        if (checkAnswer(command, BACK_TO_MENU)) {
            final String text;
            if (problemOfTheDay.getState() == null) {
                text = chooseRandomElement(ASK_TO_BE_BACK);
            } else {
                if (problemOfTheDay.getState() == Problem.State.ANSWER_GIVEN) {
                    text = chooseRandomElement(ASK_TO_BE_BACK_TOMORROW_WHEN_NOT_SOLVED);
                } else {
                    text = chooseRandomElement(ASK_TO_BE_BACK_TOMORROW);
                }
            }
            return new MenuSessionState(new TextWithTTSBuilder().append(text));
        } else if (problemOfTheDay.getState() == null) {
            return super.getNextState(problemOfTheDay, command, entitiesArray, session, timeZone);
        } else {
            if (checkAnswer(command, ASK_TO_REPEAT, YES_ANSWERS)) {
                return repeatProblemState();
            } else if (checkAnswer(command, ASK_SOLUTION, YES_ANSWERS)) {
                return saySolutionState();
            } else if (checkAnswer(command, ASK_ANSWER, YES_ANSWERS)) {
                return sayAnswerState(problemOfTheDay);
            } else if (checkAnswer(command, ASK_HINT, YES_ANSWERS)) {
                return sayHintState(null);
            } else {
                final CorrectAnswer correctAnswer = checkProblemAnswer(problemOfTheDay, command, entitiesArray);
                final String text;

                if (correctAnswer == CorrectAnswer.CORRECT) {
                    System.out.println("Correct problem of the day");
                    text = chooseRandomElement(PRAISES);
                } else {
                    if (correctAnswer == CorrectAnswer.NOT_A_NUMBER) {
                        DataBaseService.INSTANCE.updateMiscAnswersTable(command, problemOfTheDay.getText(), session.getLastServerResponse());
                        text = chooseRandomElement(DID_NOT_UNDERSTAND) + " Я могу повторить задачу, сказать ответ или вернуться в меню. Чтобы посмотреть таблицу рекордов, нажмите на кнопку Рекорды.";
                    } else {
                        text = chooseRandomElement(correctAnswer == CorrectAnswer.ALMOST_CORRECT ? ALMOST : WRONG_ANSWER);
                    }
                }
                return new ProblemOfTheDaySessionState(new TextWithTTSBuilder().append(text),
                        false, false, false, null, false);

            }
        }
    }

    @Override
    public void processRequest(JsonObject request, Session session, String timeZone) {
        if (start) {
            final Problem problemOfTheDay = session.getUserInfo().getProblemOfTheDay(timeZone);
            if (problemOfTheDay.getState() == null) {
                sayProblemText(request, session, "", problemOfTheDay);
            } else {
                sayProblemText(request, session, chooseRandomElement(HAS_NO_PROBLEM_OF_THE_DAY) + "Задача была такая. ", problemOfTheDay);
            }
        } else {
            final Problem problemOfTheDay = session.getUserInfo().getProblemOfTheDay(timeZone);
            if (problemOfTheDay.getState() == null) {
                super.processRequest(problemOfTheDay, request, session, timeZone);
            } else {
                if (isShowProblemText()) {
                    addProblemTextToResponse(request, session, getText() == null ? null : getText().getText(), timeZone, problemOfTheDay);
                } else if (saySolution) {
                    if (problemOfTheDay.hasSolution()) {
                        request.addProperty("text", problemOfTheDay.getSolution().getText());
                        request.addProperty("tts", problemOfTheDay.getSolution().getTTS());
                    } else {
                        final String text = "Правильный ответ " + problemOfTheDay.getTextAnswer() + ". ";
                        request.addProperty("text", text);
                    }
                } else if (isSayHint()) {
                    if (problemOfTheDay.hasHint()) {
                        giveHint(request, session, problemOfTheDay, "", problemOfTheDay.getNextHint());
                    } else {
                        giveHint(request, session, problemOfTheDay, "", problemOfTheDay.getLastHint());
                    }
                } else if (isSayAnswer()) {
                    final String text = "Правильный ответ " + problemOfTheDay.getTextAnswer() + ". ";
                    request.addProperty("text", text);
                } else {
                    request.addProperty("text", getText() == null ? "" : getText().getText());
                    if (getText() != null && getText().getTTS() != null) {
                        request.addProperty("tts", getText().getTTS());
                    }
                }

                request.add("buttons", createSolvedProblemButtons(session, timeZone));
            }
        }
    }

    @Nonnull
    @Override
    public SessionState addTextPrefix(String text) {
        return new ProblemOfTheDaySessionState(this.getText() == null || this.getText().getText() == null ? new TextWithTTSBuilder().append(text) : this.getText().append(text),
                isShowProblemText(), isSayHint(), isSayAnswer(), getSolvedProblem(), false);
    }

    @Override
    protected SessionState getNextStateWhenCorrectAnswerIsGiven(Problem currentProblem, Session session) {
        finishWithProblem(session, currentProblem, currentProblem.wasHintGiven() ? Problem.State.SOLVED_WITH_HINT : Problem.State.SOLVED, session.getClientId());
        final TextWithTTSBuilder text = new TextWithTTSBuilder()
                .append("", chooseRandomElement(SOUND_PRAISES))
                .append(chooseRandomElement(PRAISES))
                .append("У вас ")
                .append(getNumWithString(session.getUserInfo().getExpertScore(), SCORE))
                .append(" на уровне Эксперт. ")
                .append(chooseRandomElement(PRAISE_SHORT))
                .append("Вернемся в меню?");
        return new ProblemOfTheDaySessionState(text, false, false, false, currentProblem, false);
    }

    @Override
    protected SessionState getStateWhenAnswerIsAsked(Problem currentProblem, Session session) {
        finishWithProblem(session, currentProblem, Problem.State.ANSWER_GIVEN, session.getClientId());
        return sayAnswerState(currentProblem);
    }

    @Nonnull
    @Override
    protected SessionState sayAnswerState(Problem currentProblem) {
        return new ProblemOfTheDaySessionState(null, false, false, true, currentProblem, false);
    }

    @Nonnull
    private SessionState saySolutionState() {
        return new ProblemOfTheDaySessionState(null, false, false, true, null, true);
    }

    @Nonnull
    @Override
    protected SessionState repeatProblemState() {
        return new ProblemOfTheDaySessionState(null, true, false, false, null, false);
    }

    @Nonnull
    @Override
    protected SessionState sayHintState(String prefix) {
        return new ProblemOfTheDaySessionState(null, false, true, false, null, false);
    }

    @Nonnull
    @Override
    protected SessionState rejectToSayAnotherState() {
        return new ProblemOfTheDaySessionState(chooseRandomElement(DONT_GIVE_ANOTHER), true, false, false, null, false);

    }

    @Override
    SessionState incorrectAnswerSessionState(Session session, String text) {
        return new ProblemOfTheDaySessionState(text == null ? null : new TextWithTTSBuilder().append(text), false, false, false, null, false);
    }

    @Override
    protected void processProblemEnding(JsonObject responseJson, Session session, Problem solvedProblem, String timeZone) {
        Problem.State problemState = solvedProblem.getState();

        String text = problemState == Problem.State.ANSWER_GIVEN ? "Правильный ответ " + solvedProblem.getTextAnswer() + ". "
                : chooseRandomElement(PRAISES) + " ";

        if (problemState != Problem.State.ANSWER_GIVEN && session.getNextProblem() != null) {
            text += "Вы заработали " + getNumWithString(session.getUserInfo().getTotalScore(), SCORE) + " на уровне Эксперт. " + chooseRandomElement(PRAISE_SHORT);
        }

        responseJson.add("buttons", createSolvedProblemButtons(session, timeZone));
        responseJson.addProperty("text", text);
        if (problemState != Problem.State.ANSWER_GIVEN) {
            responseJson.addProperty("tts", chooseRandomElement(SOUND_PRAISES) + " " + responseJson.get("text"));
        }
    }

    @Nonnull
    private JsonElement createSolvedProblemButtons(Session session, String timeZone) {
        final JsonArray buttons = new JsonArray();
        final Problem problemOfTheDay = session.getUserInfo().getProblemOfTheDay(timeZone);

        buttons.add(createButton("Повторить задачу", false));
        if (problemOfTheDay.hasSolution()) {
            buttons.add(createButton("Решение", false));
        }
        QuestProblems questProblems = QuestProblemsLoader.INSTANCE.getCurrentQuestProblems(Calendar.getInstance(TimeZone.getTimeZone(timeZone)));
        buttons.add(createLeaderboardButton(session.getUserInfo(), false, questProblems == null, questProblems != null));
        buttons.add(createButton("Вернуться в меню", false));

        return buttons;
    }


}
