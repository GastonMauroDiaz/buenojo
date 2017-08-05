package com.ciis.buenojo.web.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;

import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.Authority;
import com.ciis.buenojo.repository.AuthorityRepository;
import com.ciis.buenojo.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
@RestController
@RequestMapping("/api")


public class AuthorityResource {

	
	@Inject 
	private AuthorityRepository authorityRepository;
	
	 @RequestMapping(value = "/authorities",
		        method = RequestMethod.GET,
		        produces = MediaType.APPLICATION_JSON_VALUE)
	 @Secured(AuthoritiesConstants.ADMIN)
	 @Timed
	 @Transactional(readOnly = true)
	 public List<String> getAllAuthorities() {
		List<Authority> a = authorityRepository.findAll();
		List<String> auths = new ArrayList<>(a.size());
		for (Authority auth : a) {
			auths.add(auth.getName());
		}
		return auths;
		 
	 }

	
}
