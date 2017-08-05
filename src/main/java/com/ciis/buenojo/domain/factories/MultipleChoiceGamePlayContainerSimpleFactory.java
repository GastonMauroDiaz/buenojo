package com.ciis.buenojo.domain.factories;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ciis.buenojo.domain.MultipleChoiceAnswer;
import com.ciis.buenojo.domain.nonPersistent.MultipleChoiceGamePlayContainer;
@Component
public class MultipleChoiceGamePlayContainerSimpleFactory {
public MultipleChoiceGamePlayContainer getMultipleChoiceGamePlayContainer(List <MultipleChoiceAnswer> answers)
{
	MultipleChoiceGamePlayContainer multipleChoiceGamePlayContainer = new MultipleChoiceGamePlayContainer();
	multipleChoiceGamePlayContainer.setSingle(setClearAndGetSingle(answers));
	multipleChoiceGamePlayContainer.setAnswers(answers);
	return multipleChoiceGamePlayContainer;
}


private boolean setClearAndGetSingle(List<MultipleChoiceAnswer> answers)
{
	Integer count= 0;
	boolean isSingle;
	for (MultipleChoiceAnswer answer : answers)
	{
		if (answer.getIsRight()) count++;
	}
	isSingle= (count==1);
	return isSingle;
}

}
