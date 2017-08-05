package com.ciis.buenojo.customLoaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciis.buenojo.customLoaders.helpers.CSVFormatHelper;
import com.ciis.buenojo.customLoaders.helpers.FileEncodingDetectorHelper;
import com.ciis.buenojo.customLoaders.helpers.ResourceHelper;
import com.ciis.buenojo.customLoaders.helpers.SQLStatementHelper;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

public class HangManExerciseLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;

	private ResourceAccessor resourceAccessor;
	
	private boolean fromGameResourceInput= false;
	
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

	private ArrayList<Map<String,String>> parse()  {

		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>> ();
		CSVParser parser;
		URL resource;
		CSVFormat csvFormat;
		Charset charset;
		try {

						resource =ResourceHelper.getResource(isFromGameResourceInput(),fileName);
						charset= FileEncodingDetectorHelper.guessEncodingAndGetCharset(resource);
											
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser ){
			
				list.add(record.toMap());
			}
			
		} catch (FileNotFoundException e) {
			log.error("Fail", e);
		}
		catch (IOException e) {
			log.error("Fail", e);
		}
		
		
		return list;
		
	}
	


	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		log.info(this.getClass().getName());

		ArrayList<SqlStatement> sqlStatements = new ArrayList<SqlStatement>();
		ArrayList<Map<String,String>>  records = parse();

		
		
		
		for (Map<String,String> element : records){
			if (element.get("Consigna").trim().length()>100)
			{
				log.warn("Consigna excedida "+element.get("Consigna").trim().length()+"/100");
				log.warn("Consigna completa: "+element.get("Consigna"));
				log.warn("Consigna  acotada: "+element.get("Consigna").trim().substring(0,99));
				
			}
		
			
			
			
			InsertStatement insertStatement =new InsertStatement(arg0.getDefaultSchema().getCatalogName(), arg0.getDefaultSchema().getSchemaName(), "hang_man_exercise"); 
	
					
					insertStatement.addColumnValue("hangman_game_container_id", element.get("Nro.Ejercicio"));
					insertStatement.addColumnValue("task", element.get("Consigna").trim().substring(0,Math.min(element.get("Consigna").trim().length(), 99)));
					insertStatement.addColumnValue("exercise_order", element.get("Nro.consigna"));
					insertStatement.addColumnValue("option_type", element.get("Tipos.de.opciones"));
					
					sqlStatements.add(insertStatement);
	
		
		}
SQLStatementHelper.defaultExecuteAndCommit(arg0, sqlStatements, log);		
		
	}

	public boolean isFromGameResourceInput() {
		return fromGameResourceInput;
	}

	public void setFromGameResourceInput(boolean fromGameResourceInput) {
		this.fromGameResourceInput = fromGameResourceInput;
	}

	public ResourceAccessor getResourceAccessor() {
		return resourceAccessor;
	}

	public void setResourceAccessor(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	
}
