package com.ciis.buenojo.customLoaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciis.buenojo.customLoaders.helpers.FieldValidationHelper;
import com.ciis.buenojo.customLoaders.helpers.ParserHelper;
import com.ciis.buenojo.customLoaders.helpers.ResourceHelper;
import com.ciis.buenojo.customLoaders.helpers.SQLStatementHelper;
import com.ciis.buenojo.domain.enumeration.MultipleChoiceInteractionTypeEnum;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

public class MultipleChoiceExerciseContainerLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;
	
	private String ilustrativeImagesDirectory;
	
	private String optionImagesDirectory;
	
	private String gamePath;
	
	private String setPath;
	
	private boolean fromGameResourceInput= false;
	


	private ResourceAccessor resourceAccessor;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setFileOpener(ResourceAccessor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUp() throws SetupException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Long> getMultipleChoiceContainerIds(List<Map<String, String>> records)
	{
		final List<Long> output;
		final List<Long> duplicates;
		final Set<Long> singles;
		duplicates = new ArrayList<Long>();
		output = new ArrayList<Long>();
		for (Map<String,String> element : records){
			final Long multipleChoiceContainerId;
			multipleChoiceContainerId=Long.parseLong(element.get("N° de ejercicio"));
			duplicates.add(multipleChoiceContainerId);
		}
		singles =  new HashSet<Long> (duplicates);
		duplicates.clear();
		output.addAll(singles);
		return output;
	}

	public List<SqlStatement> insertContainers(List<Long> containers, Database database)
	{
		final List<SqlStatement> output;
		InsertStatement currentSqlStatement;
		output = new ArrayList<SqlStatement>();
		
		for (Long id : containers)
		{
			currentSqlStatement =
			SQLStatementHelper.getDefaultInsertStatement(database, "multiple_choice_exercise_container");
			currentSqlStatement.addColumnValue("id", id);
			currentSqlStatement.addColumnValue("name", String.valueOf(id));
			output.add(currentSqlStatement);
		}
		return output;
	}
	
	public List<SqlStatement> getInsertMultipleChoiceContainerIds(List<Map<String, String>> records, Database database)
	{
		final List<SqlStatement> output;
		final List<Long> containers;
		containers= getMultipleChoiceContainerIds(records);
		output=insertContainers(containers, database);
		return output;
	}
	

	public List<SqlStatement> getInsertMultipleChoiceQuestions(List<Map<String, String>> records, Database database)
	{
		final List<SqlStatement> output;
		output=insertMultipleChoiceQuestions(records, database);
		return output;
	}

	
	
	
	private List<SqlStatement> insertMultipleChoiceQuestions(List<Map<String, String>> records,
			Database database) {
		final List<SqlStatement> sqlStatements;
		sqlStatements = new ArrayList<SqlStatement>();
		final String tableNameInsert = "multiple_choice_question";
		final String tableNameSelectType1= "image_resource ir, multiple_choice_subject_specific mcss INNER JOIN multiple_choice_subject mcs ON (mcs.id = mcss.multiple_choice_subject_id)";
		final String tableNameSelectType2= "multiple_choice_subject_specific mcss INNER JOIN multiple_choice_subject mcs ON (mcs.id = mcss.multiple_choice_subject_id)";
		
		final String whereClauseTemplateType2= "mcs.text = %s and mcss.text=%s";		 
		final String whereClauseTemplateType1= "mcs.text = %s and mcss.text=%s and ir.name like '%%%s%%'";		 
		
		for (Map<String, String> record : records)
		{
			final SqlStatement sqlStatement;
			final String whereClause;
			final String subjectText;
			final String subjectSpecificText ; 
			final Long id;
			final String question;
			final String interactionType;
			final Long multipleChoiceExerciseContainerId;
			final String imageResourceName;
			final String source;
			final String tableNameSelect;
			final String[] fieldsAndValues;
			subjectText = String.format("'%s'",record.get("Temática"));
			subjectSpecificText = String.format("'%s'",record.get("Tema específico"));
			interactionType = String.format("'%s'", MultipleChoiceInteractionTypeEnum.tryParse(record.get("Interacción")).toString());
			id = Long.parseLong(record.get("N° de Consigna"));
			 multipleChoiceExerciseContainerId = Long.parseLong(record.get("N° de ejercicio"));
			FieldValidationHelper.testValidationUpperLimit("Consigna", record.get("Consigna"), 100, log);
			 question = String.format("'%s'",FieldValidationHelper.getSubString(record.get("Consigna"), 100) ); 
			source = String.format("'%s'",record.get("Fuente"));
	
			if (MultipleChoiceInteractionTypeEnum.tryParse(record.get("Interacción")).equals(MultipleChoiceInteractionTypeEnum.Type1))
			{
				final String rawResource;
				rawResource=record.get("Imagen Ilustrativa");
				imageResourceName = ResourceHelper.getResourceAddress(getSetPath(),getIlustrativeImagesDirectory(), rawResource);
				whereClause=String.format(whereClauseTemplateType1,subjectText, subjectSpecificText, imageResourceName+".");
				tableNameSelect = tableNameSelectType1; 
				final String[] currentFields = {"exercise_id", "question", "interaction_type", "multiple_choice_exercise_container_id", "multiple_choice_subject_specific_id", "source", "image_resource_id"};
				final String[] currentValues = {id.toString(), question, interactionType.toString(), multipleChoiceExerciseContainerId.toString(), "mcss.id", source, "ir.id"};
				fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
			//	ResourceHelper.validateResource(imageResourceName, log);
						
			}
			else
			{
				whereClause=String.format(whereClauseTemplateType2,subjectText, subjectSpecificText);
				tableNameSelect = tableNameSelectType2; 
				final String[] currentFields = {"exercise_id", "question", "interaction_type", "multiple_choice_exercise_container_id", "multiple_choice_subject_specific_id", "source"};
				final String[] currentValues = {id.toString(), question, interactionType.toString(), multipleChoiceExerciseContainerId.toString(), "mcss.id", source};
				fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
			}
			
			
			
		
			sqlStatement=SQLStatementHelper.getRawSqlStatementSelect(log, database, tableNameInsert, tableNameSelect 
					, whereClause, fieldsAndValues);
				
					sqlStatements.add(sqlStatement);			
			
			
			
		}
		return sqlStatements;
	}

	

	public List<SqlStatement> getInsertMultipleChoiceAnswers(List<Map<String, String>> records, Database database)
	{
		final List<SqlStatement> output;
		output=insertMultipleChoiceAnswers(records, database);
		return output;
	}


	
	private List<SqlStatement> insertMultipleChoiceAnswers(List<Map<String, String>> records,
			Database database) {
		final List<SqlStatement> sqlStatements;
		final String tableNameInsert = "multiple_choice_answer";
		String tableNameSelect;
		final String tableNameSelectType2= "multiple_choice_question mcq, image_resource ir";
		final String tableNameSelectType1= "multiple_choice_question mcq";
		final String whereClauseTemplateType1= "mcq.exercise_id = %s and mcq.multiple_choice_exercise_container_id=%s";		 
		final String whereClauseTemplateType2= "mcq.exercise_id = %s and mcq.multiple_choice_exercise_container_id=%s and ir.name like '%%%s%%'";		 
		sqlStatements = new ArrayList<SqlStatement>();

		for (Map<String, String> record : records)
		{
			final Long id;
			final Long multipleChoiceExerciseContainerId;
			String whereClause;
			id = Long.parseLong(record.get("N° de Consigna"));
			multipleChoiceExerciseContainerId = Long.parseLong(record.get("N° de ejercicio"));
		
			for (int i=1;i<=4;i++)
				{
				final String answer;
				answer = String.format("'%s'", record.get("Correcta."+String.valueOf(i)));
				final SqlStatement sqlStatement;
				if (!StringUtils.isEmpty(record.get("Correcta."+String.valueOf(i)))){
				if (MultipleChoiceInteractionTypeEnum.tryParse(record.get("Interacción")).equals(MultipleChoiceInteractionTypeEnum.Type1))
				{
					
					whereClause=String.format(whereClauseTemplateType1,	id, multipleChoiceExerciseContainerId);
					tableNameSelect = tableNameSelectType1; 
					final String[] currentFields = {"answer", "is_right","multiple_choice_question_id" };
					final String[] currentValues = {answer, "true", "mcq.id"};
					
					final String[] fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
					sqlStatement = SQLStatementHelper.getRawSqlStatementSelect(log, database, tableNameInsert, tableNameSelect, whereClause, fieldsAndValues);
				}
				else
				{
					final String rawResource = record.get("Correcta."+String.valueOf(i));
					final String imageResourceName = ResourceHelper.getResourceAddress(getSetPath(),getOptionImagesDirectory(), rawResource+".");
	
					whereClause=String.format(whereClauseTemplateType2,	id, multipleChoiceExerciseContainerId, imageResourceName);
					tableNameSelect = tableNameSelectType2; 
					final String[] currentFields = {"answer", "is_right","multiple_choice_question_id","image_resource_id" };
					final String[] currentValues = {answer, "true", "mcq.id",  "ir.id"};
					
					final String[] fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
					sqlStatement = SQLStatementHelper.getRawSqlStatementSelect(log, database, tableNameInsert, tableNameSelect, whereClause, fieldsAndValues);
				//	ResourceHelper.validateResource(imageResourceName, log);
				}
			sqlStatements.add(sqlStatement);
			}	
			}
			for (int i=1;i<=3;i++)
			{
				final String answer;
				answer = String.format("'%s'", record.get("Opción."+String.valueOf(i)));
				final SqlStatement sqlStatement;
				if (!StringUtils.isEmpty(record.get("Opción."+String.valueOf(i)))){
					
				if (MultipleChoiceInteractionTypeEnum.tryParse(record.get("Interacción")).equals(MultipleChoiceInteractionTypeEnum.Type1))
				{
					whereClause=String.format(whereClauseTemplateType1,	id, multipleChoiceExerciseContainerId);
					tableNameSelect = tableNameSelectType1; 
					final String[] currentFields = {"answer", "is_right","multiple_choice_question_id" };
					final String[] currentValues = {answer, "false", "mcq.id"};
				
					final String[] fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
					sqlStatement = SQLStatementHelper.getRawSqlStatementSelect(log, database, tableNameInsert, tableNameSelect, whereClause, fieldsAndValues);
				}
				else
				{
					final String rawResource = record.get("Opción."+String.valueOf(i));
					final String imageResourceName = ResourceHelper.getResourceAddress(getSetPath(),getOptionImagesDirectory(), rawResource+".");
					
					whereClause=String.format(whereClauseTemplateType2,	id, multipleChoiceExerciseContainerId, imageResourceName);
				
					tableNameSelect = tableNameSelectType2; 
					final String[] currentFields = {"answer", "is_right","multiple_choice_question_id","image_resource_id" };
					final String[] currentValues = {answer, "false", "mcq.id",  "ir.id"};
				
					final String[] fieldsAndValues  =  (String[])ArrayUtils.addAll(currentFields, currentValues); 
				
					sqlStatement = SQLStatementHelper.getRawSqlStatementSelect(log, database, tableNameInsert, tableNameSelect, whereClause, fieldsAndValues);
				//	ResourceHelper.validateResource(imageResourceName, log);
					
				}
				
				sqlStatements.add(sqlStatement);
			}
			}
			
		}
		return sqlStatements;
	}

	
	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		log.info(String.format("%s is being executed", this.getClass().getName()));
		final ArrayList<SqlStatement> sqlStatements;
		final ArrayList<Map<String,String>>  records;  
		sqlStatements = new ArrayList<SqlStatement>();
		records = ParserHelper.parse(isFromGameResourceInput(), getGamePath(), getSetPath(), getFileName());
		
		sqlStatements.addAll(getInsertMultipleChoiceContainerIds(records, arg0));
		sqlStatements.addAll(getInsertMultipleChoiceQuestions(records, arg0));
		sqlStatements.addAll(getInsertMultipleChoiceAnswers(records, arg0));
		
		/*for (Map<String,String> element : records){
			FieldValidationHelper.showField(element.get("Temática"), "Temática",log);
			FieldValidationHelper.showField(element.get("Tema específico"), "Tema específico",log);
			FieldValidationHelper.showField(element.get("N° de ejercicio"), "N° de ejercicio",  log);
			FieldValidationHelper.showField(element.get("N° de Consigna"), "N° de Consigna",  log);
			FieldValidationHelper.showField(element.get("Consigna"), "Consigna", log);
			FieldValidationHelper.showField(element.get("Interacción"), "Interacción",  log);
			for (int i=1;i<=4;i++)
				FieldValidationHelper.showField(element.get("Correcta."+String.valueOf(i)), "Correcta."+String.valueOf(i),  log);
			for (int i=1;i<=3;i++)
				FieldValidationHelper.showField(element.get("Opción."+String.valueOf(i)), "Opción."+String.valueOf(i),  log);
			FieldValidationHelper.showField(element.get("Imagen Ilustrativa"), "Imagen Ilustrativa",  log);
			FieldValidationHelper.showField(element.get("Fuente"), "Fuente",  log);
				
			
			
		}*/
		SQLStatementHelper.defaultDirtyExecuteAndCommit(arg0, sqlStatements, log);
		
		
		
			
			
			
	
		
	}

	public String getGamePath() {
		return gamePath;
	}

	public void setGamePath(String gamePath) {
		this.gamePath = gamePath;
	}

	public String getSetPath() {
		return setPath;
	}

	public void setSetPath(String setPath) {
		this.setPath = setPath;
	}

	public ResourceAccessor getResourceAccessor() {
		return resourceAccessor;
	}

	public void setResourceAccessor(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	public String getIlustrativeImagesDirectory() {
		return ilustrativeImagesDirectory;
	}

	public void setIlustrativeImagesDirectory(String ilustrativeImagesDirectory) {
		this.ilustrativeImagesDirectory = ilustrativeImagesDirectory;
	}

	public String getOptionImagesDirectory() {
		return optionImagesDirectory;
	}

	public void setOptionImagesDirectory(String optionImagesDirectory) {
		this.optionImagesDirectory = optionImagesDirectory;
	}

	public boolean isFromGameResourceInput() {
		return fromGameResourceInput;
	}

	public void setFromGameResourceInput(boolean fromGameResourceInput) {
		this.fromGameResourceInput = fromGameResourceInput;
	}

	
}
