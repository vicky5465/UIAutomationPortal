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
    private static final Logger logger =
            LoggerFactory.getLogger(SSHConnection.class);
    
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
            String sshHost = prop.getProperty("sshhost");
            String sshUser = prop.getProperty("sshuser");
            String sshKeyFilepath = System.getProperty("user.dir") + prop.getProperty("sshkeypath");

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
            logger.info("SSH Connected");
            Class.forName(driverName).newInstance();

            int assinged_port = session.setPortForwardingL(availablePort, remoteHost, remotePort);

            logger.info("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
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
            
            logger.info("Connection to server successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
        logger.info("Closing Database Connection");
    }

    private static void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
    

