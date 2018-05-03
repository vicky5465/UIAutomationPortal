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
    private static int forwardPort = 0;
    private static final Logger logger =
            LoggerFactory.getLogger(SSHConnectionImpl.class);
    
    /**
     * connect DB via SSH
     * @param dataBaseName
     * @throws SQLException
     * @throws IOException
     */
    public static void connectToDBViaSSH(String dataBaseName) throws SQLException, IOException {
        InputStream in;
        prop = new Properties();
        try {
            in = new FileInputStream("resources/dbconfig.properties");
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectSSH();
        connectToDataBase(dataBaseName, forwardPort);
    }

    private static void connectSSH() throws SQLException {
        try {
            final String sshHost = prop.getProperty("sshhost");
            final String sshUser = prop.getProperty("sshuser");
            final String sshKeyFilepath = System.getProperty("user.dir") + prop.getProperty("sshkeypath");
            final int sshPort = Integer.parseInt(prop.getProperty("sshport"));
           
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);
            jsch.addIdentity(sshKeyFilepath);
            prop.put("StrictHostKeyChecking", "no");
            prop.put("ConnectionAttempts", "3");
            session.setConfig(prop);
            session.connect();
            logger.info("SSH Connected");
                       
            String remoteHost = prop.getProperty("remotehost");
            int remotePort = Integer.parseInt(prop.getProperty("remoteport"));
            forwardPort = getAvailablePort(); //need to forward to this port
            int assinged_port = session.setPortForwardingL(forwardPort, remoteHost, remotePort);
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

    public static void connectToDataBase(String dbName) throws SQLException {
        String mysqlUserName = prop.getProperty("mysqluser");
        String mysqlPassword = prop.getProperty("mysqlpassword");
        String mysqlPort = prop.getProperty("mysqlport");
        String mysqlurl = prop.getProperty("mysqlurl") + ":" + mysqlPort + "/" + dbName;
        try {
            //mysql database connectivity
            Class.forName(prop.getProperty("mysqldriver"));
            connection = DriverManager.getConnection(mysqlurl, mysqlUserName, mysqlPassword);
            
            logger.info("Connection to server successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void connectToDataBase(String dbName, int forwardPort) throws SQLException {
        String mysqlUserName = prop.getProperty("mysqluser");
        String mysqlPassword = prop.getProperty("mysqlpassword");
        String mysqlurl = prop.getProperty("mysqlurl") + ":" + forwardPort + "/" + dbName;
        try {
            //mysql database connectivity
            Class.forName(prop.getProperty("mysqldriver"));
            connection = DriverManager.getConnection(mysqlurl, mysqlUserName, mysqlPassword);
            
            logger.info("Connection to server successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeConnections() {
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

    public static ResultSet executeMyQuery(String query, String dataBaseName) throws IOException {
        ResultSet resultSet = null;

        try {
            connectToDBViaSSH(dataBaseName);
            PreparedStatement ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    public static List<String> resultSetHandler (ResultSet resultSet) {
        List<String> resultList = new ArrayList<String>();
        
        try {
            while(resultSet.next()) {
                resultList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultList;
    }
}

