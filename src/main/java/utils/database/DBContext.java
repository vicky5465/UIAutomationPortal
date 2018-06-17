package utils.database;

import java.util.Vector;

public class DBContext {
	private Vector<Database> _database;
	private static DBContext _dbcontext;
	private Database _currentdatabase;
	
	private DBContext()
	{
		_database = new Vector<Database>(2,2);	
	}
	/**
	 * Getter method to get the database context object 
	 * @return present _dcontext
	 */
	public static DBContext getDBContext() {
		if (_dbcontext == null)
			_dbcontext = new DBContext();
		return _dbcontext;
	}
	/**
	 * Getter method to get the database object
	 * 
	 * @return _currentdatabase
	 */
	public Database getDatabase() {
		
		return _currentdatabase;
	}	
	/**
	 * Setter method to set the database object
	 * 
	 * 
	 */
	public void setDatabase(Database database) {
		_currentdatabase = database;
	}
	
	/*public void addDatabase(Database database){
		_database.add(database);
		 setDatabase(database);
	}*/
	
	public void selectDatabase(String dbName)
	{
		_currentdatabase = getDatabase(dbName);
	}	
	
	public Database getDatabase(String databaseName)
	{
		for(int i = 0 ; i < _database.size(); i++)
		{
			if(_database.get(i).getDatabaseName().equalsIgnoreCase(databaseName))
			{
				return _database.get(i);
			}
		}	
		return null;
	}
	
	public void closeDatabase(String databasename)
	{
	getDatabase(databasename).close();
	_database.remove(getDatabaseIndex(databasename));
	
	_currentdatabase = (_database.isEmpty() ? null : _database.lastElement());
	
	}
	
	public int getDatabaseIndex(String databaseName) {
		for(int i=0 ; i < _database.size(); i++){
			if(_database.get(i).getDatabaseName().equalsIgnoreCase(databaseName))
				return i;
		}
		
		return -1;
	}
	
	public int getDatabaseIndex(Database database) {
		for(int i=0 ; i < _database.size(); i++){
			if(_database.get(i).equals(database));
				return i;
		}
		return -1;
	}
	

	/**
	 * Method to close the present database
	 */
	public void closeDatabase(){
		_currentdatabase.close();
		_database.remove(getDatabaseIndex(_currentdatabase));
		_currentdatabase = (_database.isEmpty() ? null : _database.lastElement());
	}
	
	
	public void closeAllDatabases(){
		for(Database database : _database){
			database.close();
		}
	}
	
	public void addDBConnection(Database connection){
		this._database.add(connection);
		this._currentdatabase =connection;
	}
}
