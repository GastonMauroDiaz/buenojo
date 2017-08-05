package com.ciis.buenojo.customLoaders;

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

import com.ciis.buenojo.customLoaders.helpers.CustomLoaderMessagesHelper;
import com.ciis.buenojo.customLoaders.helpers.FileManagementHelper;
import com.ciis.buenojo.customLoaders.helpers.GlobFactoryHelper;
import com.ciis.buenojo.customLoaders.helpers.ResourceHelper;
import com.ciis.buenojo.customLoaders.helpers.SQLStatementHelper;

import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;

public class MultipleChoiceImageLoader implements liquibase.change.custom.CustomTaskChange{

	private String fileName;

	private String gamePath;

	private String setPath;
	
	@Inject
	private Environment env;
	private boolean fromGameResourceInput= false;

	private boolean dropInGameResource= false;

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
		return null;
	}


	
	public List<String> getMultipleChoiceImageResources(){
		final URI uri;
		final ArrayList<String> resourceList;
		final PathMatcher matcher;
		final Path myPath;
		final Stream<Path> walk;
		resourceList = new ArrayList<String>();
		matcher = FileSystems.getDefault().getPathMatcher(GlobFactoryHelper.getCaseInsensitiveExtensionGlob("PNG","JPG"));
		try {
			uri = ResourceHelper.getResource(isFromGameResourceInput(), gamePath, setPath).toURI();
			myPath= Paths.get(uri);
			walk = Files.walk(myPath, 3, FileVisitOption.FOLLOW_LINKS);
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


		
		
		
		
/*		final String pattern;
		final String resourcePath;
		final List<String> resourceList; 
		pattern=GlobFactoryHelper.getCaseInsensitiveExtensionGlob("PNG","JPG");
		resourcePath= ResourceHelper.getResourceAddress( isFromGameResourceInput(),getGamePath(), getSetPath());
		resourceList= ImageFilesLoaderHelper.getImageResources(pattern, resourcePath, 3, log);
		return resourceList;
	*/
	}

private String getContentType(String resourceName)
{
	final String contentType;
	final String extension;
	extension = resourceName.substring(resourceName.length()-3).toLowerCase();
	contentType = String.format("image/%s", extension);
	return contentType;
}
	
	private List<SqlStatement> resourceInsert( String resourcePath, Database database)
	{
		final List <SqlStatement> sqlStatements;
		final String tableName;
		final InsertStatement insertStatement;
		final String contentType;
		//resourcePath = ResourceHelper.getResourceAddress(isFromGameResourceInput() ,getSetPath(), resourcePath.substring(1));
		tableName= "image_resource";
		contentType = getContentType(resourcePath);
		sqlStatements = new ArrayList <SqlStatement>();
		insertStatement =SQLStatementHelper.getDefaultInsertStatement(database, tableName); 
		insertStatement.addColumnValue("name", resourcePath);
		insertStatement.addColumnValue("hi_res_image_content_type", contentType);
		insertStatement.addColumnValue("hi_res_image_path", resourcePath);
		insertStatement.addColumnValue("lo_res_image_content_type", contentType);
		insertStatement.addColumnValue("lo_res_image_path", resourcePath);
		sqlStatements.add(insertStatement);
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
	
	}
		return resourceMap;
	}
	
	@Override
	public void execute(Database arg0) throws CustomChangeException {
		log.info(this.getClass() + " is being executed");
		final List<String> list;
		final List<SqlStatement> transactions;
		list =getMultipleChoiceImageResources();
	
		Map<String, String> inputOuputImagesMap = inputToOutput(list);
			
		transactions = new ArrayList<SqlStatement>();
		Iterator<Entry<String, String>> it = inputOuputImagesMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String,String> pair = it.next();
	        it.remove(); // avoids a ConcurrentModificationException
			transactions.addAll(resourceInsert(pair.getValue(),arg0));
	        
	    }
			SQLStatementHelper.defaultDirtyExecuteAndCommit(arg0, transactions, log);
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
