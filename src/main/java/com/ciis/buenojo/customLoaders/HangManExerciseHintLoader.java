package com.ciis.buenojo.customLoaders;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciis.buenojo.customLoaders.helpers.CSVFormatHelper;
import com.ciis.buenojo.customLoaders.helpers.CustomLoaderMessagesHelper;
import com.ciis.buenojo.customLoaders.helpers.FileEncodingDetectorHelper;
import com.ciis.buenojo.customLoaders.helpers.ResourceHelper;
import com.ciis.buenojo.customLoaders.helpers.SQLStatementHelper;
import com.ciis.buenojo.domain.util.BuenOjoFileUtils;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

public class HangManExerciseHintLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;

	private String gamePath;

	private String setPath;

	private boolean fromGameResourceInput= false;
	
	private ResourceAccessor resourceAccessor;
	
	final private Logger log = LoggerFactory.getLogger(this.getClass());

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getConfirmationMessage() {
		return String.format(CustomLoaderMessagesHelper.SUCCESS_MESSAGE, this.getClass().getName());
	}

	@Override
	public void setFileOpener(ResourceAccessor arg0) {
	}

	@Override
	public void setUp() throws SetupException {
		
	}

	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Map<String, String>> parse()  {
		final ArrayList<Map<String, String>> list;
		final CSVParser parser;
		final URL resource;
		final CSVFormat csvFormat;
		final Charset charset;
		list = new ArrayList<Map<String, String>> ();
		try {
			resource = ResourceHelper.getResource(isFromGameResourceInput(),fileName);
			charset= FileEncodingDetectorHelper.guessEncodingAndGetCharset(resource);
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser )
				list.add((Map<String, String>)record.toMap());
		}
		catch (IOException e) {
			log.error("Fail", e);
		}
		return list;
		
	}
	

	private HashMap<String, Map<String, String>> parseHintFile(Integer exercise, Integer exercise_order)  {

		final HashMap<String, Map<String, String>> list;
		final CSVParser parser;
		final URL resource;
		final CSVFormat csvFormat;
		final Charset charset;
		list = new HashMap<String, Map<String, String>>  ();
		try {
			resource = ResourceHelper.getResource(BuenOjoFileUtils.GAME_RESOURCES_INPUT_DIR ,this.gamePath,this.setPath,exercise.toString(),exercise_order.toString(),"xy_pista.csv");
			charset= FileEncodingDetectorHelper.guessEncodingAndGetCharset(resource);
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser )
				list.put(record.get("id").trim() , (Map<String, String>)record.toMap());
		}
		catch (IOException e) {
			log.error("Fail",e);
		}
		return list;
		
	}

	public boolean hintsFound(Map<String, String> element)
	{
		boolean output=false;
		for (int i=1;output==false&&i<=5;i++)
		{
			if (!element.get("Pista."+String.valueOf(i)).equalsIgnoreCase("NA")) 
			{
			log.info(element.get("Nro.Ejercicio") +" "+element.get("Pista."+String.valueOf(i)));
				output=true;
			}	
		
	}
		return output;
	}

	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
	
		ArrayList<SqlStatement> sqlStatements = new ArrayList<SqlStatement>();
		ArrayList<Map<String, String>>  records = parse();
		HashMap<String, Map<String, String>> hints;
		RawSqlStatement rawSqlStatement=null;
		for (Map<String, String> element : records){
			if (hintsFound(element)) 
			{
				hints=parseHintFile(Integer.parseInt(element.get("Nro.Ejercicio")), Integer.parseInt(element.get("Nro.consigna")));	
				
				log.info(String.valueOf(hints.size()));
				log.info(String.valueOf(hints.keySet().toString()));
				for (int i=1;(i<=hints.size()) ;i++)
					{ 
						
					if (!element.get("Pista."+String.valueOf(i)).equalsIgnoreCase("NA"))
						{
							StringBuilder sb= new StringBuilder();
							sb.append("INSERT INTO public.hang_man_exercise_hint (ord,text, hang_man_exercise_id,x,y)");
							sb.append("SELECT ");
							sb.append(i+ ",");
							sb.append("'"+element.get("Pista."+String.valueOf(i))+"',");
							sb.append("id,"+((hints.get(String.valueOf(i)).get("col").equalsIgnoreCase("NA"))?"null":hints.get(String.valueOf(i)).get("col"))+",");
							sb.append(((hints.get(String.valueOf(i)).get("col").equalsIgnoreCase("NA"))?"null":hints.get(String.valueOf(i)).get("row")));
							sb.append(" from public.hang_man_exercise where hangman_game_container_id="+ element.get("Nro.Ejercicio")+ " and exercise_order="+ element.get("Nro.consigna")+ " ;");
								
							rawSqlStatement = 
								new RawSqlStatement(sb.toString());
						sqlStatements.add(rawSqlStatement);
				
						}
					
				}
			}
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
