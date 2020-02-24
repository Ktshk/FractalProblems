package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.dinoproblems.server.generators.VariousProblems.parseProblemXML;

/**
 * Created by Katushka on 06.02.2020.
 */
public class QuestProblemsLoader {
    public static final QuestProblemsLoader INSTANCE = new QuestProblemsLoader();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

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
                Node problemNode = nodeList.item(i);

                if (problemNode.getNodeType() == Node.ELEMENT_NODE) {
                    final String name = problemNode.getAttributes().getNamedItem("name").getNodeValue();
                    final Date start = DATE_FORMAT.parse(problemNode.getAttributes().getNamedItem("start").getNodeValue());
                    final Date end = DATE_FORMAT.parse(problemNode.getAttributes().getNamedItem("end").getNodeValue());
                    final String description = problemNode.getAttributes().getNamedItem("description").getNodeValue();

                    final NodeList childNodes = problemNode.getChildNodes();
                    final List<Problem> problems = new ArrayList<>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        final Node questChildNode = childNodes.item(j);
                        if (questChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            Problem problem = parseProblemXML(Problem.Difficulty.EXPERT, problemNode);
                            int index = Integer.parseInt(questChildNode.getAttributes().getNamedItem("name").getNodeValue()) - 1;
                            problems.add(index, problem);
                        }
                    }

                    QuestProblems quest = new QuestProblems(name, description, start, end, problems);
                    allQuests.add(quest);
                }
            }

            allQuests.sort(Comparator.comparing(QuestProblems::getStart));
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            e.printStackTrace();
        }
    }
}
