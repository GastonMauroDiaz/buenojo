package com.ciis.buenojo.domain.enumeration;

/**
 * The MultipleChoiceInteractionTypeEnum enumeration.
 */
public enum MultipleChoiceInteractionTypeEnum {
    Type1, Type2;

static public MultipleChoiceInteractionTypeEnum tryParse(String value)
{
	switch (value){
		case "Forma 2-Opción-imagen": case "Forma 2 - Opción-imagen": return Type2; 
		case "Forma 1-Imagen ilustrativa": case "Forma 1 - Imagen ilustrativa": return Type1; 
		 
		
	}
	return null;
}
}

