package com.ciis.buenojo.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.MultipleChoiceAnswer;
import com.ciis.buenojo.domain.MultipleChoiceQuestion;
import com.ciis.buenojo.domain.factories.MultipleChoiceGamePlayContainerSimpleFactory;
import com.ciis.buenojo.domain.nonPersistent.MultipleChoiceGamePlayContainer;
import com.ciis.buenojo.repository.MultipleChoiceAnswerRepository;
import com.ciis.buenojo.repository.MultipleChoiceQuestionRepository;
/**
 * Service class for managing users.
 */
@Service
@Transactional
public class MultipleChoiceAnswerService {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceAnswerService.class);

  @Inject
  private MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;
  
  private MultipleChoiceGamePlayContainerSimpleFactory multipleChoiceGamePlayContainerSimpleFactory=new MultipleChoiceGamePlayContainerSimpleFactory();
  @Inject
  private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
  
  public MultipleChoiceGamePlayContainer getMultipleChoiceGamePlayContainer(Long query_id)
  {
	  MultipleChoiceGamePlayContainer multipleChoiceGamePlayContainer;
	  List <MultipleChoiceAnswer> answers;
	  answers = multipleChoiceAnswerRepository.findByMultipleChoiceQuestion_id(query_id);
	   multipleChoiceGamePlayContainer = multipleChoiceGamePlayContainerSimpleFactory.getMultipleChoiceGamePlayContainer(answers);
	  return multipleChoiceGamePlayContainer;
  }

  public MultipleChoiceGamePlayContainer getMultipleChoiceGamePlayContainerRandom()
  {
	  MultipleChoiceGamePlayContainer multipleChoiceGamePlayContainer;
	  List <MultipleChoiceAnswer> answers;
	  MultipleChoiceQuestion multipleChoiceQuestion;
	  multipleChoiceQuestion = multipleChoiceQuestionRepository.findAll().get(0);
	  answers = multipleChoiceAnswerRepository.findByMultipleChoiceQuestion_id(multipleChoiceQuestion.getId());
	  
	  multipleChoiceGamePlayContainer = multipleChoiceGamePlayContainerSimpleFactory.getMultipleChoiceGamePlayContainer(answers);
	  multipleChoiceGamePlayContainer.setMultipleChoiceQuestion(multipleChoiceQuestion);
	  return multipleChoiceGamePlayContainer;
  }
}
