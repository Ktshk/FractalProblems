import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Katushka
 * on 26.04.2019.
 */
public class XMLProblemParser {

    private static void saveXML(String textFile, String xmlFileName) throws ParserConfigurationException, FileNotFoundException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element root = document.createElement("problems");
        document.appendChild(root);

        final BufferedReader difficultProblems = new BufferedReader(new FileReader(textFile));
        try {
            String line;

            while ((line = difficultProblems.readLine()) != null) {
                final String[] tokens = line.split("\t");
                final String id = tokens[0];
                final String text = tokens[1];
                final String tts = tokens[2].isEmpty() ? null : tokens[2];
                final String hint = tokens[3];
                final int answer = Integer.valueOf(tokens[4]);
                final String textAnswer = tokens[5];
                final Set<String> possibleTextAnswers = new HashSet<>();
                possibleTextAnswers.add(textAnswer);
                for (int i = 6; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        possibleTextAnswers.add(tokens[i]);
                    }
                }

                Element problemElement = document.createElement("problem");
                root.appendChild(problemElement);

                Attr scenarioAttr = document.createAttribute("scenarioId");
                scenarioAttr.setValue(id);
                problemElement.setAttributeNode(scenarioAttr);

                Attr answerAttr = document.createAttribute("answer");
                answerAttr.setValue(Integer.toString(answer));
                problemElement.setAttributeNode(answerAttr);

                Element textElement = document.createElement("text");
                problemElement.appendChild(textElement);
                textElement.appendChild(document.createTextNode(text));

                if (tts != null) {
                    Element ttsElement = document.createElement("tts");
                    problemElement.appendChild(ttsElement);
                    ttsElement.appendChild(document.createTextNode(tts));
                }

                Element hintsElement = document.createElement("hints");
                problemElement.appendChild(hintsElement);
                Element hintElement = document.createElement("hint");
                hintsElement.appendChild(hintElement);
                Element hintTextElement = document.createElement("text");
                hintElement.appendChild(hintTextElement);
                hintTextElement.appendChild(document.createTextNode(hint));

                Element textAnswersElement = document.createElement("text_answers");
                problemElement.appendChild(textAnswersElement);
                for (String possibleTextAnswer : possibleTextAnswers) {
                    Element textAnswerElement = document.createElement("answer");
                    textAnswersElement.appendChild(textAnswerElement);
                    Attr textAnswerAttr = document.createAttribute("text");
                    textAnswerAttr.setValue(possibleTextAnswer);
                    textAnswerElement.setAttributeNode(textAnswerAttr);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                difficultProblems.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFileName));

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(domSource, streamResult);

    }

    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, TransformerException {
        saveXML("ProblemEditor/easy.txt", "ProblemEditor/easy.xml");
        saveXML("ProblemEditor/medium.txt", "ProblemEditor/medium.xml");
        saveXML("ProblemEditor/difficult.txt", "ProblemEditor/difficult.xml");
        saveXML("ProblemEditor/expert.txt", "ProblemEditor/expert.xml");
    }
}
