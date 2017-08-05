package com.ciis.buenojo.customLoaders;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.ciis.buenojo.customLoaders.helpers.FileManagementHelper;
import com.ciis.buenojo.customLoaders.helpers.GlobFactoryHelper;
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
import liquibase.statement.core.RawSqlStatement;
import liquibase.util.file.FilenameUtils;

public class HangManExerciseImageLoader implements liquibase.change.custom.CustomTaskChange{

	@Inject
	private Environment env;
	private String fileName;

	private String gamePath;

	private String inputBasePath;

	private String outputBasePath;

	private String setPath;


	public String getSetPath() {
		return setPath;
	}

	public void setSetPath(String setPath) {
		this.setPath = setPath;
	}

	public String getInputBasePath() {
		return inputBasePath;
	}

	public void setInputBasePath(String inputBasePath) {
		this.inputBasePath = inputBasePath;
	}

	public String getOutputBasePath() {
		return outputBasePath;
	}

	public void setOutputBasePath(String outputBasePath) {
		this.outputBasePath = outputBasePath;
	}




	private boolean fromGameResourceInput= false;

	private boolean dropInGameResource= false;


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

	public String getGamePath() {
		return gamePath;
	}

	public void setGamePath(String gamePath) {
		this.gamePath = gamePath;
	}




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
	}

	@Override
	public void setUp() throws SetupException {
	}

	@Override
	public ValidationErrors validate(Database arg0) {
		return null;
	}



	public ArrayList<String> getHangManImageResources(){
		final URI uri;
		final ArrayList<String> resourceList;
		final PathMatcher matcher;
		final Path myPath;
		final Stream<Path> walk;
		resourceList = new ArrayList<String>();
		matcher = FileSystems.getDefault().getPathMatcher(GlobFactoryHelper.getCaseInsensitiveExtensionGlob("png"));
		try {
			uri = new URI("file:"+ inputBasePath+File.separator+ gamePath+ File.separator + setPath);//ResourceHelper.getResource(isFromGameResourceInput(), gamePath, setPath).toURI();
			myPath= Paths.get(uri);
			walk = Files.walk(myPath, 2, FileVisitOption.FOLLOW_LINKS);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();){
				Path currentPath =it.next();
				if (matcher.matches(currentPath))
				{		resourceList.add(currentPath.toString().substring(currentPath.toString().indexOf("game-resources-input/")));
				}
				}


		} catch (IOException | URISyntaxException e1) {
			log.error("Fail",e1);

		}
		return resourceList;


	}

	public boolean hintsFound(Map<String,String> element)
	{
		boolean output=false;
		for (int i=1;output==false&&i<=5;i++)
		{
			if (!element.get("Pista."+String.valueOf(i)).equalsIgnoreCase("NA"))
			{
				output=true;
			}

		}
		return output;
	}

	private ArrayList<SqlStatement> pathSplitter(String path, String resourcePath, Database arg0)
	{
		/* Resource and HangMan Link*/
		final ArrayList <SqlStatement> sqlStatements;
		final String[] subComponents;
		final Long exercise;
		final String fileName;
		Integer order;
		final InsertStatement insertStatement;
		sqlStatements=new ArrayList <SqlStatement>();
		log.info(Paths.get(path).getFileName().toString());

		exercise = Long.parseLong(Paths.get(path).getParent().getFileName().toString());
		fileName = FilenameUtils.getBaseName(path);
		subComponents = fileName.split("_");

		insertStatement =SQLStatementHelper.getDefaultInsertStatement(arg0, "image_resource");
		insertStatement.addColumnValue("name", resourcePath);
		insertStatement.addColumnValue("hi_res_image_content_type", "image/png");
		insertStatement.addColumnValue("hi_res_image_path", resourcePath);
		insertStatement.addColumnValue("lo_res_image_content_type", "image/png");
		insertStatement.addColumnValue("lo_res_image_path", resourcePath);
		sqlStatements.add(insertStatement);

		for (String subComponent: subComponents)
		{
			if (subComponent.toLowerCase().matches("c[0-9]{1,2}"))
			{
				order = Integer.parseInt(subComponent.substring(1));
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO public.hang_man_exercise_image_resource ( hang_man_exercise_id,image_resource_id) ");
				sb.append("Select e.id, i.id" );
				sb.append(" from public.hang_man_exercise as e, image_resource as i ");
				sb.append("where hangman_game_container_id="+ exercise+ " and exercise_order="+ order);
				sb.append(" and name='"+ resourcePath+ "';");
				RawSqlStatement rawSqlStatement = new RawSqlStatement(sb.toString());
				sqlStatements.add(rawSqlStatement);
			}
		}
		return sqlStatements;
	}

	private Map<String,String> inputToOutput(List<String> inputImages)
	{
	final Map<String,String> resourceMap;
	resourceMap= new HashMap<String, String>();
		for (String inputImage : inputImages)
	{
		final String inputFolder;
		final String outputFolder;
		/* For embebbed tomcat */

		inputFolder = ((env!=null)?env.getProperty("buenojo.gameResourcesInputFolder"):"game-resources-input");
		outputFolder = (env!=null)?env.getProperty("buenojo.gameResourcesFolder"):"game-resources";

			final String destFile = inputImage.replaceFirst(inputFolder, outputFolder);
		FileManagementHelper.copyTo("/"+inputImage,"/"+ destFile, log);
		resourceMap.put(inputImage, destFile);
		log.info(inputImage + " " + destFile);
	}
		return resourceMap;
	}
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		final List<String> imageList;
		final Map<String, String> inputOuputImagesMap;
		imageList= getHangManImageResources();
		final ArrayList<SqlStatement> sqlStatements;
		sqlStatements = new ArrayList<SqlStatement>();
		inputOuputImagesMap = inputToOutput(imageList);
		Iterator<Entry<String, String>> it = inputOuputImagesMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String,String> pair = it.next();
	        it.remove(); // avoids a ConcurrentModificationException
			sqlStatements.addAll(pathSplitter(pair.getKey(), pair.getValue(), arg0));
	    }
		try{
			arg0.execute(sqlStatements.toArray(new SqlStatement[sqlStatements.size()]), new ArrayList<SqlVisitor>());
		}
		catch (liquibase.exception.DatabaseException e)
		{
			log.info("Archivo previamente cargado al menos parcialmente.");

		}



		catch (LiquibaseException e) {
			// TODO Auto-generated catch block
			log.error("Problema en base", e);


		}

	}

	public boolean isDropInGameResource() {
		return dropInGameResource;
	}

	public void setDropInGameResource(boolean dropInGameResource) {
		this.dropInGameResource = dropInGameResource;
	}



}
