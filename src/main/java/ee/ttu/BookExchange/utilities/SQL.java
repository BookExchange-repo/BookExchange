package ee.ttu.BookExchange.utilities;

import ee.ttu.BookExchange.Application;

import java.sql.*;

public class SQL {
    public static final String dbHostname = "localhost";
    public static final String dbUsername = "root";
    public static final String dbPassword = "toor";

    private Connection connection;
    private ResultSet queryResult;
    private String queryResultName = "";

    public SQL() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mariadb://" + dbHostname + "/", dbUsername, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isQueryCompleted() {
        return queryResult != null;
    }

    public void assertQueryCompletion() {
        if (!isQueryCompleted())
            throw new RuntimeException("Exception in query operation: Query is not completed");
    }

    public String getQueryResultName() {
        return this.queryResultName;
    }

    public void cleanUpQuery() {
        try {
            if (this.queryResult != null) {
                if (this.queryResult.getStatement() != null)
                    this.queryResult.getStatement().close();
                this.queryResult.close();
            }
        } catch (SQLException e) {
            Logger.logWarning("Could not close SQL query");
        }
        this.queryResult = null;
        this.queryResultName = "";
    }

    public void executeQuery(String query) {
        try {
            cleanUpQuery();
            Statement statement = connection.createStatement();
            this.queryResult = statement.executeQuery(query);
            this.queryResultName = query;
            this.queryResult.first();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getQueryRows() {
        assertQueryCompletion();

        int rows = 0;
        try {
            this.queryResult.last();
            rows = this.queryResult.getRow();
            this.queryResult.first();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return rows;
    }

    public int getQueryColumns(int rowIndex) {
        assertQueryCompletion();

        int columns = 0;
        try {
            this.queryResult.absolute(rowIndex + 1);
            columns = this.queryResult.getMetaData().getColumnCount();
            this.queryResult.first();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return columns;
    }

    public String getQueryCell(int rowIndex, int columnIndex) {
        assertQueryCompletion();

        String cell = "";
        try {
            this.queryResult.absolute(rowIndex + 1);
            cell += this.queryResult.getString(columnIndex + 1);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return cell;
    }

    private void printSeparator() {
        for (int i = 0; i < 40; i++) {
            System.out.print('=');
        }
        System.out.println();
    }

    public void printQueryResults() {
        assertQueryCompletion();

        int rows = getQueryRows();
        printSeparator();
        System.out.println("Query string: " + getQueryResultName());
        System.out.println("Query rows: " + rows + "\n");
        for (int i = 0; i < rows; i++) {
            int columns = getQueryColumns(i);
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < columns; j++) {
                String cell = getQueryCell(i, j);
                if (j == columns - 1)
                    System.out.print(cell);
                else
                    System.out.print(cell + " | ");
            }
            System.out.println();
        }
        printSeparator();
    }

    public String escapeString(boolean enableSemicolons, String... toEscape) {
        String resultingString = "";

        for (int i = 0; i < toEscape.length; i++) {
            for (int j = 0; j < toEscape[i].length(); j++) {
                if (enableSemicolons) {
                    if (j == 0)
                        resultingString += '\'';
                }
                char currentChar = toEscape[i].charAt(j);
                switch (currentChar) {
                    case '\u0000':
                    case '\n':
                    case '\r':
                    case '\\':
                    case '\'':
                    case '"':
                    case '\u001a':
                        resultingString += "\\" + currentChar;
                        break;
                    default:
                        resultingString += currentChar;
                        break;
                }
                if (enableSemicolons) {
                    if (j == toEscape[i].length() - 1)
                        resultingString += '\'';
                }
            }
            if (i != toEscape.length - 1)
                resultingString += ", ";
        }

        return resultingString;
    }

    public String escapeString(String... toEscape) {
        return escapeString(false, toEscape);
    }

    public static SQL queryAllFromTable(String tableName, String[] values) {
        SQL sql = new SQL();
        sql.executeQuery("use " + Application.databaseName + ";");
        sql.executeQuery("SELECT " + sql.escapeString(values) + " FROM " + tableName + ";");
        sql.printQueryResults();
        return sql;
    }
}
