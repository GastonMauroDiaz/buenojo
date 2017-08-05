package com.ciis.buenojo.customLoaders.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;
import liquibase.statement.core.RawSqlStatement;

public class SQLStatementHelper {

	private static final String selectKeyword = "SELECT";
	private static final String insertKeyword = "INSERT";
	private static final String fromKeyword = "FROM";
	private static final String intoKeyword = "INTO";
	private static final String selectIntoKeyword = String.format("%s %s", selectKeyword, intoKeyword);
	private static final String insertIntoKeyword = String.format("%s %s", insertKeyword, intoKeyword);

	private static final String schemaCatalogFormat = "%s.%s";
	private static final String schemaTableFormat = "%s.%s";
	private static final String insertIntoTemplate= String.format("%s %s", insertIntoKeyword, schemaTableFormat);
	private static final String singleFromTemplate = String.format("%s %s", fromKeyword, schemaTableFormat);
	
	private static String getSchemaFromDatabase(Database database)
	{
		return database.getDefaultSchema().getSchemaName();
	}
	
	private static String getCatalogFromDatabase(Database database)
	{
		return database.getDefaultSchema().getCatalogName();
	}
	
	
	public static InsertStatement getDefaultInsertStatement(final Database database, final String tableName) {
		final InsertStatement output;
		final String catalog;
		final String schema;
		catalog = getCatalogFromDatabase(database);
		schema = getSchemaFromDatabase(database);
		output = new InsertStatement(catalog, schema, tableName);
		return output;
	}

	public static RawSqlStatement getRawSqlStatementSelect(Logger log, Database database, String tableNameInsert,
			String tableNameSelect, String whereClause, String... fieldNamesAndValues) {
		final RawSqlStatement output;
		final String schema;
		final StringBuilder sb;
		final String[] fieldNames, fieldValues;

		schema = getSchemaFromDatabase(database);

		if (fieldNamesAndValues.length % 2 != 0) {
			log.error("Parity Check Field/Value fail");
			output = new RawSqlStatement("");
			return output;
		}
		fieldNames = (String[]) ArrayUtils.subarray(fieldNamesAndValues, 0, fieldNamesAndValues.length / 2);
		fieldValues = (String[]) ArrayUtils.subarray(fieldNamesAndValues, fieldNamesAndValues.length / 2,
				fieldNamesAndValues.length);
		sb = new StringBuilder();
		sb.append(String.format(insertIntoTemplate, schema, tableNameInsert));
		sb.append(" (");
		sb.append(Arrays.toString(fieldNames).subSequence(1, Arrays.toString(fieldNames).length() - 1));
		sb.append(")");
		sb.append(" SELECT ");
		sb.append(Arrays.toString(fieldValues).subSequence(1, Arrays.toString(fieldValues).length() - 1));
		sb.append(String.format(" FROM %s.%s ", schema, tableNameSelect));
		sb.append(String.format(" WHERE %s", whereClause));

		output = new RawSqlStatement(sb.toString());
		log.info(sb.toString());
		return output;
	}
   /**
    * Allows execute a set of transactions and commit
    * @param database
    * @param sqlStatements
    * @param log
    */
	public static void defaultExecuteAndCommit(final Database database, List<SqlStatement> sqlStatements,
			final Logger log) {
		final SqlStatement[] sqlStatementsArray;
		final List<SqlVisitor> sqlVisitors;
		sqlStatementsArray = sqlStatements.toArray(new SqlStatement[sqlStatements.size()]);
		sqlVisitors = new ArrayList<SqlVisitor>();
		try {
			database.execute(sqlStatementsArray, sqlVisitors);
			database.commit();
		} catch (DatabaseException e) {
			log.error("Commit error", e);
			quietRollBack(database, log);

		} catch (LiquibaseException e) {
			log.error("Execute error", e);

		}

	}
	/**
	 * Allow rollback a set of transactions without the need of inner try catch
	 * 
	 * @param database
	 * @param log
	 */
	private static void quietRollBack(Database database, Logger log)
	{
		try
		{
			database.rollback();
		}
		catch (DatabaseException e)
		{
			log.error("Rollback error",e);
		}
	}

	public static void defaultDirtyExecuteAndCommit(final Database database, List<SqlStatement> sqlStatements,
			final Logger log) {
		final SqlStatement[] sqlStatementsArray;
		final List<SqlVisitor> sqlVisitors;
		sqlStatementsArray = sqlStatements.toArray(new SqlStatement[sqlStatements.size()]);
		final SqlStatement[] sqlStatementCurrentArray;
		sqlVisitors = new ArrayList<SqlVisitor>();
		sqlStatementCurrentArray = new SqlStatement[1];
		
		for (SqlStatement s : sqlStatementsArray) {
			sqlStatementCurrentArray[0] = s;
			try {
				database.setAutoCommit(false);
				database.execute(sqlStatementCurrentArray, sqlVisitors);
		    	database.commit();
			} catch (DatabaseException e) {
				if (!e.getMessage().contains("duplicate key value violates unique constraint"))
						log.warn("As is dirty, check if the exception is due contraint", e);
				quietRollBack(database, log);
			
			} catch (LiquibaseException e) {
				log.error("Execute error", e);
				quietRollBack(database, log);
				
			}
		}

	}

}
