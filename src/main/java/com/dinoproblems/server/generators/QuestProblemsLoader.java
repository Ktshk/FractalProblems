package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dinoproblems.server.generators.VariousProblems.parseProblemXML;

/**
 * Created by Katushka on 06.02.2020.
 */
public class QuestProblemsLoader {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final Comparator<QuestProblems> QUEST_START_COMPARATOR = Comparator.comparing(QuestProblems::getStart);

    public static final QuestProblemsLoader INSTANCE = new QuestProblemsLoader();

    private List<QuestProblems> allQuests = new ArrayList<>();

    private QuestProblemsLoader() {
        loadProblems();
    }

    private void loadProblems() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(getClass().getResourceAsStream("quest.xml"));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("quest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node questNode = nodeList.item(i);

                if (questNode.getNodeType() == Node.ELEMENT_NODE) {
                    final NamedNodeMap attributes = questNode.getAttributes();
                    final String name = attributes.getNamedItem("name").getNodeValue();
                    final Date start = DATE_FORMAT.parse(attributes.getNamedItem("start").getNodeValue());
                    final Date end = DATE_FORMAT.parse(attributes.getNamedItem("end").getNodeValue());
                    final String description = attributes.getNamedItem("description").getNodeValue();

                    final NodeList childNodes = questNode.getChildNodes();
                    final List<Problem> problems = new ArrayList<>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        final Node questChildNode = childNodes.item(j);
                        if (questChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            Problem problem = parseProblemXML(Problem.Difficulty.EXPERT, questChildNode);
                            int index = Integer.parseInt(questChildNode.getAttributes().getNamedItem("num").getNodeValue()) - 1;
                            problems.add(index, problem);
                        }
                    }

                    QuestProblems quest = new QuestProblems(name, description, start, end, problems);
                    allQuests.add(quest);
                }
            }

            allQuests.sort(QUEST_START_COMPARATOR);
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Problem getTodayProblem(Calendar date) {
        final QuestProblems questProblems = getCurrentQuestProblems(date);
        if (questProblems != null) {
            return questProblems.getProblem(date);
        }

        return null;
    }

    @Nullable
    public QuestProblems getCurrentQuestProblems(Calendar date) {
        final QuestProblems key = new QuestProblems("", "", date.getTime(), null, null);
        int ind = Collections.binarySearch(allQuests, key, QUEST_START_COMPARATOR);
        if (ind < 0) {
            ind = -ind - 1 - 1;
        }
        if (ind < 0) {
            return null;
        }

        final Calendar questEnd = Calendar.getInstance();
        questEnd.setTime(allQuests.get(ind).getEnd());
        questEnd.add(Calendar.DAY_OF_MONTH, 1);

        if (date.before(questEnd)) {
            return allQuests.get(ind);
        }

        return null;
    }
}
