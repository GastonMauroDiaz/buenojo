package com.ciis.buenojo.customLoaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;

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
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

public class HangManGameContainerLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;
	private String basePath;
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	private ResourceAccessor resourceAccessor;
	
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

	private HashSet<Integer> parse()  {
		final HashSet<Integer> list;
		final CSVParser parser;
		final URL resource;
		final CSVFormat csvFormat;
		final Charset charset;
		 list = new HashSet<Integer>();
		try {
			resource = new URL ("file:"+basePath+fileName);// ResourceHelper.getResource(isFromGameResourceInput(),fileName);
			charset=FileEncodingDetectorHelper.guessEncodingAndGetCharset(resource);
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser ){
				list.add(Integer.parseInt(record.get("Nro.Ejercicio")));
			}
			} catch (FileNotFoundException e) {
			log.error("Fail",e);
		}
		catch (IOException e) {
			log.error("Fail",e);
		}
		
		
		return list;
		
	}
	


	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		log.info(this.getClass().getName());
		ArrayList<SqlStatement> sqlStatements = new ArrayList<SqlStatement>();
		HashSet<Integer> records = parse();
		
		for (Integer exerciseId : records){
					InsertStatement insertStatement =new InsertStatement(arg0.getDefaultSchema().getCatalogName(), arg0.getDefaultSchema().getSchemaName(), "hang_man_game_container"); 
					insertStatement.addColumnValue("id", exerciseId);
					insertStatement.addColumnValue("name", exerciseId.toString());
					
					sqlStatements.add(insertStatement);
		}
		SQLStatementHelper.defaultExecuteAndCommit(arg0, sqlStatements, log);
		
	}

	public ResourceAccessor getResourceAccessor() {
		return resourceAccessor;
	}

	public void setResourceAccessor(ResourceAccessor resourceAccessor) {
		this.resourceAccessor = resourceAccessor;
	}

	public boolean isFromGameResourceInput() {
		return fromGameResourceInput;
	}

	public void setFromGameResourceInput(boolean fromGameResourceInput) {
		this.fromGameResourceInput = fromGameResourceInput;
	}

	
}
