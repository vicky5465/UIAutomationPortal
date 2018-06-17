package utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.movoto.fixtures.impl.util.db.DBContext;

public class Database {
	private String databaseName; // mysql, oracle, mssql
	private String driver;
	private String conURL;
	private String uname;
	private String pwd;
	private Connection con;
	private String databaseType;
	
	public Database(String dataBase, String connectionURL, String userName,	String password) {
		setConnectionParams(dataBase,connectionURL,userName,password);
		DBContext.getDBContext().addDBConnection(this);
	}
	
	public Database(String dataBase, String connectionURL, String userName,String password, String name) {
		setConnectionParams(dataBase,connectionURL,userName,password);
		this.setDatabaseName(name);
		DBContext.getDBContext().addDBConnection(this);

	}
	
	public void setConnectionParams(String dataBaseType, String connectionURL, String userName,String password){
		this.setDatabaseType(dataBaseType);
		this.setConURL(connectionURL);
		this.setUname(userName);
		this.setPwd(password);
		if(dataBaseType!=null&&connectionURL!=null&&userName!=null&&password!=null)
		setConnectionParam();
	}
	
	/**
	 * Method to decorate the DB connection parameters
	 */
	private synchronized void setConnectionParam(){
		
		if (this.databaseType.equalsIgnoreCase("mysql")) {
			driver = "com.mysql.jdbc.Driver";
			conURL = "jdbc:mysql://"+ this.conURL;	// jdbc:mysql://localhost:3306/EMPLOYEE
		}
		else if(this.databaseType.equalsIgnoreCase("informix")){
			driver = "com.informix.jdbc.IfxDriver";
			conURL = "jdbc:informix-sqli://" + this.conURL; 
		}
		else if(this.databaseType.equalsIgnoreCase("oracle")){
			driver = "oracle.jdbc.driver.OracleDriver";
			conURL = "jdbc:oracle:thin" + ":@" + this.conURL; // "jdbc:oracle:thin:@localhost:3306:Oracle
		}
		else if (this.databaseType.equalsIgnoreCase("mssql")) {
			driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			conURL = "jdbc:sqlserver" + "://" + this.conURL; // com.microsoft.sqlserver.jdbc.SQLServerDriver
		}
		createConnection();
	}

	/**
	 * Method to create a DB connection
	 */
	private synchronized void createConnection() {
		
		try {
				Class.forName(driver);
				this.con = DriverManager.getConnection(conURL, uname, pwd);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
		    }
			

		
	}
	
	/**
	 * Method to execute a query on the current connection
	 * @param query query string
	 * @return query result
	 */
	public String execute(String query) {
		StringBuffer sf = new StringBuffer();
		Statement stmt;
		try{
			stmt = getCon().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int count = rs.getMetaData().getColumnCount();
			System.out.println("result count is: " + count);
			while (rs.next()) {

				for (int i = 1; i <= count; i++) {
					 sf.append(rs.getString(i)).append(",");
				}
			}
		}catch(SQLException e){
			
		}
		
		return sf.toString().substring(0, sf.toString().length()-1);
	}
	
	/**
     * Method to execute a query on the current connection
     * @param query query string
     * @return query result
     */
    public HashMap<String, List<String>> executeQuery(String query) {
           StringBuffer sf = new StringBuffer();
           Statement stmt;
           HashMap<String, List<String>> datamap = new HashMap<String, List<String>>();
           try{
                  stmt = getCon().createStatement();
                  ResultSet rs = stmt.executeQuery(query);
                  int count = rs.getMetaData().getColumnCount();
                  
                  
                  while (rs.next()) {

                        for (int i = 1; i <= count; i++) {
                               if(datamap.containsKey(rs.getMetaData().getColumnLabel(i))){
                                      datamap.get(rs.getMetaData().getColumnLabel(i)).add(rs.getString(i));
                               }
                               else{
                                      datamap.put(rs.getMetaData().getColumnLabel(i),new ArrayList<>());      
                                      datamap.get(rs.getMetaData().getColumnLabel(i)).add(rs.getString(i));

                               }
                    }
                  }
           }catch(SQLException e){
                 
           }
           
           return datamap;
    }

	
	private void closeDBResources() {
		try {

			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			
		}
	}

	public void close() {
		closeDBResources();
		DBContext.getDBContext().setDatabase(null);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		databaseName = databaseName;
	}

	public String getDriver() {
		return driver;
	}

	// Access modified
	private void setDriver(String driver) {
		driver = driver;
	}

	public String getConURL() {
		return conURL;
	}

	private void setConURL(String conURL) {
		this.conURL = conURL;
	}

	public String getUname() {
		return uname;
	}

	private void setUname(String uname) {
		this.uname = uname;
	}

	public String getPwd() {
		return pwd;

	}

	private void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}

