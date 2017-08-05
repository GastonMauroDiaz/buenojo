package com.ciis.buenojo.customLoaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

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
import liquibase.statement.core.RawSqlStatement;

public class HangManExerciseOptionLoader implements liquibase.change.custom.CustomTaskChange {

	private String fileName;

	private String gamePath;

	private String setPath;

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

	private ArrayList<Map<String, String>> parse() {

		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		CSVParser parser;
		URL resource;
		CSVFormat csvFormat;
		Charset charset;
		try {
			resource = ResourceHelper.getResource(isFromGameResourceInput(), fileName);
			charset= FileEncodingDetectorHelper.guessEncodingAndGetCharset(resource);
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser) {
				list.add(record.toMap());
			}

		} catch (FileNotFoundException e) {
	log.error("Fail", e);
	} catch (IOException e) {
		log.error("Fail", e);	}

		return list;

	}

	@Override
	public void execute(Database arg0) throws CustomChangeException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		log.info(this.getClass().getName());

		ArrayList<SqlStatement> sqlStatements = new ArrayList<SqlStatement>();
		ArrayList<Map<String, String>> records = parse();
		// JdbcConnection dbConn = (JdbcConnection) arg0.getConnection();

		for (Map<String, String> element : records) {
			RawSqlStatement rawSqlStatement;
			for (int i = 1; i <= 5; i++) {
				if (!(element.get("correcta." + String.valueOf(i)).trim().equalsIgnoreCase("NA"))) {

					if (element.get("correcta." + String.valueOf(i)).trim().length() > 30) {
						log.warn("Opcion excedida " + element.get("correcta." + String.valueOf(i)).trim().length()
								+ "/30");
						log.warn("Opcion completa: " + element.get("correcta." + String.valueOf(i)));
						log.warn("Opcion  acotada: "
								+ element.get("correcta." + String.valueOf(i)).trim().substring(0, 29));

					}
					StringBuilder sb = new StringBuilder();
					sb.append("INSERT INTO public.hang_man_exercise_option ( hang_man_exercise_id,text, is_correct)");

					sb.append("SELECT id,");
					sb.append("'" + element.get("correcta." + String.valueOf(i)).trim().substring(0,
							Math.min(element.get("correcta." + String.valueOf(i)).trim().length(), 29)) + "',");
					sb.append("true");
					sb.append(" from public.hang_man_exercise where hangman_game_container_id="
							+ element.get("Nro.Ejercicio") + " and exercise_order=" + element.get("Nro.consigna")
							+ " ;");

					rawSqlStatement = new RawSqlStatement(sb.toString());
					sqlStatements.add(rawSqlStatement);
				}

			}
			StringTokenizer str = new StringTokenizer(element.get("Otras.opciones"), "-");
			while (str.hasMoreTokens()) {

				StringBuilder sb = new StringBuilder();
				String opcion = str.nextToken();
				if (opcion.trim().equalsIgnoreCase("NA")) {
					log.warn("Opcion False NA:" + element.get("Otras.opciones") + " " + element.get("Nro.Ejercicio")
							+ " " + element.get("Nro.consigna"));

				} else {
					if (opcion.trim().length() > 30) {
						log.warn("Opcion excedida " + opcion.trim().length() + "/30");
						log.warn("Opcion completa: " + opcion);
						log.warn("Opcion  acotada: " + opcion.trim().substring(0, 29));

					}

					sb.append("INSERT INTO public.hang_man_exercise_option ( hang_man_exercise_id,text, is_correct)");

					sb.append("SELECT id,");
					sb.append("'" + opcion.trim().substring(0, Math.min(opcion.trim().length(), 29)) + "',");
					sb.append("false");
					sb.append(" from public.hang_man_exercise where hangman_game_container_id="
							+ element.get("Nro.Ejercicio") + " and exercise_order=" + element.get("Nro.consigna")
							+ " ;");

					rawSqlStatement = new RawSqlStatement(sb.toString());
					sqlStatements.add(rawSqlStatement);
				}
			}

		}
		SQLStatementHelper.defaultExecuteAndCommit(arg0, sqlStatements, log);
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

	public boolean isFromGameResourceInput() {
		return fromGameResourceInput;
	}

	public void setFromGameResourceInput(boolean fromGameResourceInput) {
		this.fromGameResourceInput = fromGameResourceInput;
	}

}
