package com.ciis.buenojo.domain.nonPersistent;

import java.util.List;

import com.ciis.buenojo.domain.MultipleChoiceAnswer;
import com.ciis.buenojo.domain.MultipleChoiceQuestion;

public class MultipleChoiceGamePlayContainer {
private List <MultipleChoiceAnswer> answers;

private MultipleChoiceQuestion multipleChoiceQuestion;

private boolean isSingle;
public List <MultipleChoiceAnswer> getAnswers() {
	return answers;
}
public void setAnswers(List <MultipleChoiceAnswer> answers) {
	this.answers = answers;
}
public boolean isSingle() {
	return isSingle;
}
public void setSingle(boolean isSingle) {
	this.isSingle = isSingle;
}
public MultipleChoiceQuestion getMultipleChoiceQuestion() {
	return multipleChoiceQuestion;
}
public void setMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
	this.multipleChoiceQuestion = multipleChoiceQuestion;
}

}
