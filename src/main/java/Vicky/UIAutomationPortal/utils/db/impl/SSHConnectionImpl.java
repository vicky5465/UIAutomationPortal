package utils.db.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import utils.db.DBConnection;

public class SSHConnectionImpl implements DBConnection{
    private static Connection connection = null;
    private static Session session = null;
    private static Properties prop = null;
    private static int availablePort = 0;
    
    public static void connectToServer(String dataBaseName) throws SQLException, IOException {
        connectSSH();
        connectToDataBase(dataBaseName);
    }

    public static void connectSSH() throws SQLException {
        InputStream in;
        prop = new Properties();
        try {
            in = new FileInputStream("resources/dbconfig.properties");
            prop.load(in);
            System.out.println();
            String sshHost = prop.getProperty("sshhost");
            String sshUser = prop.getProperty("sshuser");
            String sshKeyFilepath = System.getProperty("user.dir") + prop.getProperty("sshheypath");

            availablePort = getAvailablePort(); // any free port can be used
            String remoteHost = prop.getProperty("mysqlhost");
            int remotePort = Integer.parseInt(prop.getProperty("mysqlport"));
            int sshPort = Integer.parseInt(prop.getProperty("sshport"));
       
            String driverName = prop.getProperty("mysqldriver");
            
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);
            jsch.addIdentity(sshKeyFilepath);
            prop.put("StrictHostKeyChecking", "no");
            prop.put("ConnectionAttempts", "3");
            session.setConfig(prop);
            session.connect();

            System.out.println("SSH Connected");

            Class.forName(driverName).newInstance();

            int assinged_port = session.setPortForwardingL(availablePort, remoteHost, remotePort);

            System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
            System.out.println("Port Forwarded");
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    private static int getAvailablePort() {
        ServerSocket socket;
        int port = 0;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
        System.out.println(port+"--------");
        return port;
    }

    private static void connectToDataBase(String dataBaseName) throws SQLException {
        String dbuserName = prop.getProperty("mysqluser");
        String dbpassword = prop.getProperty("mysqlpassword");
        String localSSHUrl = prop.getProperty("localsshurl");
        try {
            //mysql database connectivity
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(availablePort);
            dataSource.setUser(dbuserName);
            dataSource.setAllowMultiQueries(true);

            dataSource.setPassword(dbpassword);
            dataSource.setDatabaseName(dataBaseName);

            connection = dataSource.getConnection();

            System.out.print("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
    }

    private static void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    private static ResultSet executeMyQuery(String query, String dataBaseName) throws IOException {
        ResultSet resultSet = null;

        try {
            connectToServer(dataBaseName);
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            System.out.println("Database connection success");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void DeleteOrganisationReferencesFromDB(String organisationsLike) throws IOException {
        try {
            connectToServer("ServerName");
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("select * from DB1");

            String organisationsToDelete = "";
            List<String> organisationsIds = new ArrayList<String>();

            // create string with id`s values to delete organisations references
            while (resultSet.next()) {
                String actualValue = resultSet.getString("id");
                organisationsIds.add(actualValue);
            }

            for (int i = 0; i < organisationsIds.size(); i++) {
                organisationsToDelete = " " + organisationsToDelete + organisationsIds.get(i);
                if (i != organisationsIds.size() - 1) {
                    organisationsToDelete = organisationsToDelete + ", ";
                }
            }

            stmt.executeUpdate(" DELETE FROM `DB1`.`table1` WHERE `DB1`.`table1`.`organisation_id` in ( " + organisationsToDelete + " );");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    public static List<String> getOrganisationsDBNamesBySubdomain(String organisationsLike) throws IOException {
        List<String> organisationDbNames = new ArrayList<String>();
        ResultSet resultSet = executeMyQuery("select `DB`.organisation.dbname from `DB1`.organisation where subdomain like '" + organisationsLike + "%'", "DB1");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("dbname");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return organisationDbNames;
    }

     public static List<String> getAllDBNames() throws IOException {
        // get all live db names incentral DB
        List<String> organisationDbNames = new ArrayList<String>();
        ResultSet resultSet = executeMyQuery("show databases", "DB1");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("Database");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return organisationDbNames;
    }

      public static void deleteDataBasesByName(List<String> DataBasesNamesList) throws IOException {
        try {
            connectSSH();
            int dataBasesAmount = DataBasesNamesList.size();
            for (int i = 0; i < dataBasesAmount; i++) {
                connectToDataBase(DataBasesNamesList.get(i));

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("DROP database `" + DataBasesNamesList.get(i) + "`");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseDataBaseConnection();
            closeConnections();
        }
    }

}
    

    
