package liquibase.database.core;

import liquibase.CatalogAndSchema;
import liquibase.GlobalConfiguration;
import liquibase.Scope;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.executor.ExecutorService;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SequenceCurrentValueFunction;
import liquibase.statement.SequenceNextValueFunction;
import liquibase.statement.core.RawCallStatement;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.util.JdbcUtil;
import liquibase.util.StringUtil;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.ResourceBundle.getBundle;
public class DmDatabase extends AbstractJdbcDatabase {
	
	public DmDatabase() {
		super.unquotedObjectsAreUppercased=true;
        super.setCurrentDateTimeFunction("SYSTIMESTAMP");
        // Setting list of Oracle's native functions
        dateFunctions.add(new DatabaseFunction("SYSDATE"));
        dateFunctions.add(new DatabaseFunction("SYSTIMESTAMP"));
        dateFunctions.add(new DatabaseFunction("CURRENT_TIMESTAMP"));
        super.sequenceNextValueFunction = "%s.nextval";
        super.sequenceCurrentValueFunction = "%s.currval";		
	}

	@Override
	public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
		// TODO Auto-generated method stub
		return "DM DBMS".equalsIgnoreCase(conn.getDatabaseProductName());
	}

	@Override
	public String getDefaultDriver(String url) {
		return "dm.jdbc.driver.DmDriver";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "dm";
	}

	@Override
	public Integer getDefaultPort() {
		// TODO Auto-generated method stub
		return 5236;
	}

	@Override
	public boolean supportsInitiallyDeferrableColumns() {
		 return true;
	}

	@Override
	public boolean supportsTablespaces() {
		return true;
	}

	@Override
	public int getPriority() {
		return 6;
	}

	@Override
	protected String getDefaultDatabaseProductName() {
		return "dm";
	}

}















