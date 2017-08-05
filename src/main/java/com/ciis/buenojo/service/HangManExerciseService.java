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
import com.ciis.buenojo.domain.HangManExerciseOption;
import com.ciis.buenojo.domain.HangManOptionListItem;
import com.ciis.buenojo.repository.HangManExerciseOptionRepository;
import com.ciis.buenojo.repository.HangManExerciseRepository;
import com.ciis.buenojo.repository.HangManOptionListItemRepository;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class HangManExerciseService {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseService.class);

   
    
    @Inject
    private HangManExerciseOptionRepository hangManExerciseOptionRepository;
  
    @Inject
    private HangManOptionListItemRepository hangManOptionListItemRepository;
  
    @Inject
    private HangManExerciseRepository hangManExerciseRepository;
  
    
    public Boolean getIsCorrect(Long idExercise, String responses)
    {	
    	List<Long> resp;
    	List<Long> trueOptions;
    	String[] responseComponents;
    	resp = new ArrayList<Long>();
    	trueOptions  = new ArrayList<Long>();
    	responseComponents = responses.split(",");
    	for (String r : responseComponents)
    	{
    		resp.add(Long.valueOf(r));
    	}
    	List<HangManExerciseOption> listOfOptions;
    	listOfOptions = hangManExerciseOptionRepository.findByHangManExerciseId(idExercise);
    	for (HangManExerciseOption hmeo :  listOfOptions)
    		if (hmeo.getIsCorrect()) trueOptions.add(hmeo.getId());
    	
    	return (resp.containsAll(trueOptions)&&trueOptions.containsAll(resp));
    	
        }

    public List<Long> whichAreInCorrect(Long idExercise, String responses)
    {	
    	List<Long> resp;
    	List<Long> trueOptions;
    	String[] responseComponents;
    	resp = new ArrayList<Long>();
    	trueOptions  = new ArrayList<Long>();
    	responseComponents = responses.split(",");
    	for (String r : responseComponents)
    	{
    		if (r!="")
    			resp.add(Long.valueOf(r));
    	}
    	List<HangManExerciseOption> listOfOptions;
    	listOfOptions = hangManExerciseOptionRepository.findByHangManExerciseId(idExercise);
    	for (HangManExerciseOption hmeo :  listOfOptions)
    		if (hmeo.getIsCorrect()) trueOptions.add(hmeo.getId());
    	
    	resp.removeAll(trueOptions);
    	
    	return resp;
    	
        }

    
}
