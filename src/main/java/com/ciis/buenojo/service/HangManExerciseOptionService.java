package com.ciis.buenojo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.domain.HangManOptionListItem;
import com.ciis.buenojo.repository.HangManExerciseOptionRepository;
import com.ciis.buenojo.repository.HangManExerciseRepository;
import com.ciis.buenojo.repository.HangManOptionListItemRepository;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class HangManExerciseOptionService {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseOptionService.class);

   
    
    @Inject
    private HangManExerciseOptionRepository hangManExerciseOptionRepository;
  
    @Inject
    private HangManOptionListItemRepository hangManOptionListItemRepository;
  
    @Inject
    private HangManExerciseRepository hangManExerciseRepository;
  
    
    public List<Object> getFixedOptionsRandomOrder(Long idContainer, Integer count)
    {	
    	List<Object> list;
    	List<HangManOptionListItem> listOption;
    	List <Long> exercises;
    	list =  hangManExerciseOptionRepository.findByHangManContainer(idContainer);
    	exercises=getExercises(list);
    	for (Long idExercise : exercises)
    	{
    		log.info("["+idExercise+"] cat"+getCategories(idExercise).toString());
    		listOption = hangManOptionListItemRepository.findByOptionTypeIn(getCategories(idExercise));
    		List<String> duplicates= new ArrayList<String>();
    		List<HangManOptionListItem> toRemove= new ArrayList<HangManOptionListItem>();
    		
    		for (HangManOptionListItem h : listOption){
    			if (duplicates.contains(h.getOptionText().toUpperCase().trim()))
    			{
    				toRemove.add(h);
    			}
    			else
    				duplicates.add(h.getOptionText().toUpperCase().trim());
    		}
    		
    		listOption.removeAll(toRemove);
    		int c=countOptionByExercise(list, idExercise);
    		List<String> currentOptions = getCurrentOption(list,idExercise);
    		if (listOption==null)
    		{
    			log.info("Categoria vacia "+ idExercise);
    		}
    		else{
    			List<Object> toRemoveAgain = new ArrayList<Object>();
    			for (HangManOptionListItem h : listOption)
    			{
    			
    			if (currentOptions.contains(h.getOptionText().toUpperCase().trim()))
    				toRemoveAgain.add(h);
    			}
    			listOption.removeAll(toRemoveAgain);
    			Collections.shuffle(listOption);
    			for (int j=0;(j<count-c)&&(j<listOption.size());j++)
    			{
    				Object[] objects= new Object[3];
    				objects[0]= listOption.get(j).getId()+12345;
    				objects[1]= listOption.get(j).getOptionText();
    				objects[2]= idExercise;
    				list.add(objects);
    			}
    		
    		}
    		
    	}
    	Collections.shuffle(list);
        return list;
    }
    
    private Integer countOptionByExercise(List<Object>list, Long idExercise)
    {
    	Integer options=0;
    	for (Object o : list)
    	if (Long.parseLong( ((Object[])o)[2].toString()) ==idExercise) options++;
    	return options;
    }
  
    private List<String> getCurrentOption(List<Object>list, Long idExercise)
    {
    	List<String> currentOptions= new ArrayList<String>();
    	for (Object o : list)
    	if (Long.parseLong( ((Object[])o)[2].toString()) ==idExercise) currentOptions.add(((Object[])o)[1].toString().toUpperCase().trim());
    	return currentOptions;
    }
  
    private List<Long> getExercises(List<Object>list)
    {
    	List<Long> exercises = new ArrayList<Long>();
    	for (Object o : list)
    	{
    		Long currentExercise=Long.parseLong( ((Object[])o)[2].toString());
    		if (!exercises.contains(currentExercise))
    			exercises.add(currentExercise);
    	}
    	return exercises;
    }
   
    private List<String> getCategories(Long idExercise)
    {
    	HangManExercise hangManExercise;
    	hangManExercise = hangManExerciseRepository.getOne(idExercise);
    	List <String> categories;
    	categories = Arrays.asList(hangManExercise.getOptionType().split("-|,"));
    	
    	return categories;
    }
  
}
