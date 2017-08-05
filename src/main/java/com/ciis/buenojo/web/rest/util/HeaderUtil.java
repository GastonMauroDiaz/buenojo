package com.ciis.buenojo.web.rest.util;

import org.springframework.http.HttpHeaders;

import com.ciis.buenojo.domain.ActivityTransition;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

	
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-buenOjoApp-alert", message);
        headers.add("X-buenOjoApp-params", param);
        return headers;
    }
    public static HttpHeaders createEntitiesCreationAlert(String entityName, String param) {
        return createAlert("Se crearon "+ param + " " + entityName + " exitosamente.", param);
    }
    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("A new " + entityName + " is created with identifier " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("Un/a " + entityName + " se actualizó con el identificador" + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("Un" + entityName + " se borró con el identificador" + param, param);
    }
    public static HttpHeaders createAllEntityDeletionAlert(String entityName) {
        return createAlert(" los " + entityName + " han sido eliminados", "Todos");
    }
    public static HttpHeaders createBadRequestHeaderAlert(String message) {
        return createAlert(message, "");
    }
    public static HttpHeaders createBadCSVRequestAlert(String fileName) {
        return createAlert("El archivo " + fileName + " no es un CSV Válido", fileName);
    }
    
    public static HttpHeaders createActivityTransitionAlert(ActivityTransition transition) {
    	
    	if (transition.getCourseFinished()) {
    		
    		return createAlert("Finalizaste el Curso: ", transition.getCourse().getName());
    	}
    	if (transition.getLevelUp()) {
    		return createAlert("Pasaste al nivel:", transition.getNext().getLevel().getName());
    	}
    	return new HttpHeaders();
    }
    
}
