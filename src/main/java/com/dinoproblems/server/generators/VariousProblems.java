package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Katushka on 21.04.2019.
 */
public class VariousProblems {
    public static final VariousProblems INSTANCE = new VariousProblems();
    public static final String THEME = "VARIOUS";

    private Multimap<Difficulty, Problem> problems = HashMultimap.create();

    private VariousProblems() {
        loadProblems();
    }

    private void loadProblems() {
        loadProblemsByDifficulty("easy.xml", Difficulty.EASY);
        loadProblemsByDifficulty("medium.xml", Difficulty.MEDIUM);
        loadProblemsByDifficulty("difficult.xml", Difficulty.DIFFICULT);
        loadProblemsByDifficulty("expert.xml", Difficulty.EXPERT);
    }

    private void loadProblemsByDifficulty(String fileName, Difficulty difficulty) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(getClass().getResourceAsStream(fileName));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("problem");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node problemNode = nodeList.item(i);

                if (problemNode.getNodeType() == Node.ELEMENT_NODE) {
                    final String id = problemNode.getAttributes().getNamedItem("scenarioId").getNodeValue();
                    final int answer = Integer.valueOf(problemNode.getAttributes().getNamedItem("answer").getNodeValue());
                    String text = null;
                    String tts = null;
                    List<String> hints = new ArrayList<>();
                    final HashSet<String> possibleTextAnswers = Sets.newHashSet();

                    final NodeList childNodes = problemNode.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        final Node problemChildNode = childNodes.item(j);
                        if (problemChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            if (problemChildNode.getNodeName().equals("text")) {
                                text = problemChildNode.getTextContent();
                            } else if (problemChildNode.getNodeName().equals("tts")) {
                                tts = problemChildNode.getTextContent();
                            } else if (problemChildNode.getNodeName().equals("hints")) {
                                final NodeList hintsNodes = problemChildNode.getChildNodes();
                                for (int k = 0; k < hintsNodes.getLength(); k++) {
                                    final Node hintNode = hintsNodes.item(k);
                                    if (hintNode.getNodeType() == Node.ELEMENT_NODE) {
                                        final NodeList hintChildren = hintNode.getChildNodes();
                                        for (int m = 0; m < hintChildren.getLength(); m++) {
                                            final Node hintChild = hintChildren.item(m);
                                            if (hintChild.getNodeType() == Node.ELEMENT_NODE) {
                                                if (hintChild.getNodeName().equals("text")) {
                                                    hints.add(hintChild.getTextContent());
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (problemChildNode.getNodeName().equals("text_answers")) {
                                final NodeList textAnswerNodes = problemChildNode.getChildNodes();
                                for (int k = 0; k < textAnswerNodes.getLength(); k++) {
                                    final Node textAnswerNode = textAnswerNodes.item(k);
                                    if (textAnswerNode.getNodeType() == Node.ELEMENT_NODE) {
                                        possibleTextAnswers.add(textAnswerNode.getAttributes().getNamedItem("text").getNodeValue());
                                    }
                                }
                            }
                        }
                    }

                    problems.put(difficulty, new ProblemWithPossibleTextAnswers(text, tts, answer, THEME,
                            possibleTextAnswers, hints, new ProblemScenarioImpl(THEME + "_" + id, true),
                            difficulty));
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Collection<Problem> getProblems(Difficulty difficulty) {
        return problems.get(difficulty);
    }
}
