package pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.CDATA;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pt.ulisboa.tecnico.socialsoftware.tutor.config.DateHandler;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*;

import java.util.List;

public class XMLQuestionExportVisitor implements Visitor {
    private Element rootElement;
    private Element currentElement;

    public String export(List<Question> questions) {
        createHeader();

        exportQuestions(questions);

        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());

        return xml.outputString(this.rootElement);
    }

    public void createHeader() {
        Document jdomDoc = new Document();
        rootElement = new Element("questions");

        jdomDoc.setRootElement(rootElement);
        this.currentElement = rootElement;
    }

    private void exportQuestions(List<Question> questions) {
        for (Question question : questions) {
            question.accept(this);
        }
    }

    private void exportQuestion(Question question, String questionType){
        Element questionElement = new Element("question");
        questionElement.setAttribute("courseType", question.getCourse().getType().name());
        questionElement.setAttribute("courseName", question.getCourse().getName());
        questionElement.setAttribute("key", String.valueOf(question.getKey()));
        questionElement.setAttribute("content", question.getContent());
        questionElement.setAttribute("title", question.getTitle());
        questionElement.setAttribute("status", question.getStatus().name());
        questionElement.setAttribute("type", questionType);
        if (question.getCreationDate() != null)
            questionElement.setAttribute("creationDate", DateHandler.toISOString(question.getCreationDate()));
        this.currentElement.addContent(questionElement);

        this.currentElement = questionElement;

        if (question.getImage() != null)
            question.getImage().accept(this);
    }

    @Override
    public void visitQuestion(MultipleChoiceQuestion question) {
        this.exportQuestion(question, Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION);

        Element optionsElement = new Element("options");
        this.currentElement.addContent(optionsElement);

        this.currentElement = optionsElement;
        question.visitOptions(this);

        this.currentElement = this.rootElement;
    }

    @Override
    public void visitQuestion(CodeFillInQuestion question) {
        this.exportQuestion(question, Question.QuestionTypes.CODE_FILL_IN);

        Element codeElement = new Element("code");
        codeElement.addContent(new CDATA(question.getCode()));
        codeElement.setAttribute("language", question.getLanguage().name());
        this.currentElement.addContent(codeElement);

        Element fillInOptionsElement = new Element("fillInSpots");
        this.currentElement.addContent(fillInOptionsElement);

        this.currentElement = fillInOptionsElement;
        question.visitOptions(this);

        this.currentElement = this.rootElement;
    }

    @Override
    public void visitImage(Image image) {
        Element imageElement = new Element("image");
        if (image.getWidth() != null) {
            imageElement.setAttribute("width",String.valueOf(image.getWidth()));
        }
        imageElement.setAttribute("url", image.getUrl());

        this.currentElement.addContent(imageElement);
    }

    @Override
    public void visitOption(Option option) {
        Element optionElement = new Element("option");

        optionElement.setAttribute("sequence", String.valueOf(option.getSequence()));
        optionElement.setAttribute("content", option.getContent());
        optionElement.setAttribute("correct", String.valueOf(option.getCorrect()));

        this.currentElement.addContent(optionElement);
    }


    @Override
    public void visitFillInSpot(FillInSpot fillInSpot) {
        Element fillInSpotElement = new Element("fillInSpot");
        fillInSpotElement.setAttribute("sequence", String.valueOf(fillInSpot.getSequence()));

        Element tmp = this.currentElement;

        this.currentElement = fillInSpotElement;
        fillInSpot.visitOptions(this);
        this.currentElement = tmp;

        this.currentElement.addContent(fillInSpotElement);
    }

    @Override
    public void visitFillInOption(FillInOption fillInOption) {
        Element optionElement = new Element("option");

        optionElement.setAttribute("sequence", String.valueOf(fillInOption.getSequence()));
        optionElement.setAttribute("content", fillInOption.getContent());
        optionElement.setAttribute("correct", String.valueOf(fillInOption.isCorrect()));

        this.currentElement.addContent(optionElement);
    }
}
