package com.ciis.buenojo.domain;

import java.util.Set;

public interface IKeywordAnnotated {

	public void setKeywords(Set<PhotoLocationKeyword> photoLocationKeywords);
	
	public Set<PhotoLocationKeyword> getKeywords();
	
}
