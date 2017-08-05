package com.ciis.buenojo.customLoaders;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciis.buenojo.customLoaders.helpers.FieldValidationHelper;
import com.ciis.buenojo.customLoaders.helpers.ParserHelper;
import com.ciis.buenojo.customLoaders.helpers.ResourceHelper;
import com.ciis.buenojo.customLoaders.helpers.SQLStatementHelper;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

public class MultipleChoiceSubjectLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;

	private ResourceAccessor resourceAccessor;
	
	private String gamePath;
	
	private String setPath;
	
	final private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private boolean fromGameResourceInput= false;
	
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

	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		log.info(String.format("%s is being executed", this.getClass().getName()));
		final ArrayList<SqlStatement> sqlStatements;
		final ArrayList<Map<String,String>>  records;  
		InsertStatement insertStatement;
		sqlStatements = new ArrayList<SqlStatement>();
		records = ParserHelper.parse(isFromGameResourceInput(), getGamePath(), getSetPath(), getFileName());
		for (Map<String,String> element : records){
			FieldValidationHelper.testValidationUpperLimit(element.get("Temática"), "Temática", 100, log);
			insertStatement =SQLStatementHelper.getDefaultInsertStatement(arg0, "multiple_choice_subject");
			insertStatement.addColumnValue("text", FieldValidationHelper.getSubString(element.get("Temática"), 100));
			sqlStatements.add(insertStatement);
		}
		SQLStatementHelper.defaultDirtyExecuteAndCommit(arg0, sqlStatements, log);
	}

	public ResourceAccessor getResourceAccessor() {
		return resourceAccessor;
	}

	public void setResourceAccessor(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
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

	public boolean isFromGameResourceInput() {
		return fromGameResourceInput;
	}

	public void setFromGameResourceInput(boolean fromGameResourceInput) {
		this.fromGameResourceInput = fromGameResourceInput;
	}

	
}
