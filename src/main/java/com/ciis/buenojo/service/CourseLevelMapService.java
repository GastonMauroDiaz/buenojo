package com.ciis.buenojo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.Level;
import com.ciis.buenojo.repository.CourseLevelMapRepository;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class CourseLevelMapService {

    private final Logger log = LoggerFactory.getLogger(CourseLevelMapService.class);

   
    
    @Inject
    private CourseLevelMapRepository courseLevelMapRepository;
  
  private Integer getWeight(Map<Long, Optional<Long>> graph,Long id)
  {
	  if (id==null) log.info("NULL");
	  Optional<Long> currentParent= Optional.of(id);
	  Integer currentWeight=0;
	  while (currentParent!=null&&currentParent.isPresent())
	  {
		  log.info("CurrentParent:"+ currentParent );
		  currentParent= graph.get(currentParent.get());
		  currentWeight++;
		  
	  }
	  return currentWeight;
  }
  
  
    public Integer getMaxStages(Long courseId)
    {
    	List<CourseLevelMap> courseLevelMapList= courseLevelMapRepository.findByCourse_Id(courseId);
    	
    	Map<Long, Optional<Long>> graph = new HashMap<Long, Optional<Long>>() ;
    	Map<Long, Integer> weights = new HashMap<Long, Integer>();
    	for (CourseLevelMap courseLevelMap : courseLevelMapList){
    		Long levelId=courseLevelMap.getLevel().getId();
    		Level parent =courseLevelMap.getParent();
    		Optional<Long> parentId;
    		if (parent==null) parentId= Optional.empty();
    		else parentId= Optional.of(parent.getId());
    		graph.put(levelId, parentId );
    		weights.put(levelId, 0);
    	}	
    	for (Long id: weights.keySet()) weights.put(id, getWeight(graph,  id));
    	Map.Entry<Long, Integer> maxEntry=null;
    	for (Map.Entry<Long, Integer> entry : weights.entrySet())
    	{
    	    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
    	    {
    	        maxEntry = entry;
    	    }
    	}
    	return maxEntry.getValue();
    	
    }

    public Integer getStage(Long courseId, Long levelIdRequested )
    {
    	List<CourseLevelMap> courseLevelMapList= courseLevelMapRepository.findByCourse_Id(courseId);
    	
    	Map<Long, Optional<Long>> graph = new HashMap<Long, Optional<Long>>() ;
    	Map<Long, Integer> weights = new HashMap<Long, Integer>();
    	for (CourseLevelMap courseLevelMap : courseLevelMapList){
    		Long levelId=courseLevelMap.getLevel().getId();
    		Level parent =courseLevelMap.getParent();
    		Optional<Long> parentId;
    		if (parent==null) parentId= Optional.empty();
    		else parentId= Optional.of(parent.getId());
    		graph.put(levelId, parentId );
    		weights.put(levelId, 0);
    	}	
    	weights.put(levelIdRequested, getWeight(graph, levelIdRequested));
    	return weights.get(levelIdRequested);	
    }
    
}
