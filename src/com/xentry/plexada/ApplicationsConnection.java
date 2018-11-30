/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efosa
 */
public class ApplicationsConnection {
    
    private static final Logger LOG = Logger.getLogger(ApplicationsConnection.class.getName());
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static String prop_file_path = "";
    private static String ebs_database = "";
    private static String ebs_dbuser = "";
    private static String ebs_dbpassword = "";
    public static String propfilepath = "";
    private static String logFile = "";
    private static String vlogFile = "";   
    private static String entrpr_name = "";
    private static String gateway_server = "";
    private static String username = "";
    private static String password = "";
    private static String sieb_database = "";
    private static String sieb_objmgr = "";
    private static String sieb_username = "";
    private static String sieb_password = "";
    private static final String gateway_port = "2321";
    private static StringWriter errors = new StringWriter();
    
    public ApplicationsConnection() {
        initializePropertyValues();
        //LOG = vLOG;
    }
    
    
    private static void initializePropertyValues(){        
        LOG.log(Level.INFO,"Initializing connection properties .... ");
        if (OS.contains("nix") || OS.contains("nux")) {
                prop_file_path = "/usr/app/siebel/intg/intg.properties";
                vlogFile = "nix_connect_logfile";
        } else if (OS.contains("win")) {
                prop_file_path =  "C:\\temp\\intg\\intg.properties";
                vlogFile = "win_connect_logfile";
        }
        Properties prop = new Properties();
        FileInputStream input;        
        try {
            input = new FileInputStream(prop_file_path);
            prop.load(input);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            LOG.log(Level.SEVERE, "initializePropertyValues:FileNotFoundException", errors.toString());
        } catch (IOException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            LOG.log(Level.SEVERE, "initializePropertyValues:IOException", errors.toString());
        }
        
        ebs_database = prop.getProperty("ebs_database");
        ebs_dbuser = prop.getProperty("ebs_dbuser");
        ebs_dbpassword = prop.getProperty("ebs_dbpassword");
        entrpr_name = prop.getProperty("entrpr_name");
        gateway_server = prop.getProperty("gateway_server");
        sieb_objmgr = prop.getProperty("sieb_objmgr");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        sieb_database = prop.getProperty("sieb_database");
        sieb_username = prop.getProperty("sieb_dbuser");
        sieb_password = prop.getProperty("sieb_dbpassword");
        MyLogging.log(Level.INFO, "Values are entrpr_name:{0},gateway_server:{1},username:{2},password{3}"+entrpr_name+""+ gateway_server+""+username+""+password);
        MyLogging.log(Level.INFO, "Siebel database:{0},sieb_username:{1},sieb_password:{2}"+ sieb_database+""+sieb_username+""+sieb_password);
        logFile = prop.getProperty(vlogFile);
        MyLogging.log(Level.INFO, "EBS database:{0},username:{1},password{2}"+ebs_database+""+ ebs_dbuser+""+ebs_dbpassword);
    }
    
    public static Connection connectToEBSDatabase(){
        initializePropertyValues();
        Connection connection = null;             
        try {                
            MyLogging.log(Level.INFO,"Connection to EBS begin ....");
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver"); 
            }catch (ClassNotFoundException e) {
                e.printStackTrace(new PrintWriter(errors));
		MyLogging.log(Level.SEVERE,"Error in driver class:"+errors.toString());
            }            
            MyLogging.log(Level.INFO,"Driver found ....");
            MyLogging.log(Level.INFO, "Values ..{0}:{1}:{2}" + ebs_database+":"+ ebs_dbuser+":"+ ebs_dbpassword);
            connection = DriverManager.getConnection(ebs_database, ebs_dbuser, ebs_dbpassword);
            MyLogging.log(Level.INFO,"Connected");
            return connection;
        }catch (SQLException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN connecting To EBS Database Method:"+errors.toString());
        }
        
        return connection;
    }
    
    public static Connection connectToSiebelDatabase(){
        initializePropertyValues();
        Connection connection = null;             
        try {                
            MyLogging.log(Level.SEVERE,"Connection to Siebel begin ....");
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver"); 
            }catch (ClassNotFoundException e) {
                e.printStackTrace(new PrintWriter(errors));
		MyLogging.log(Level.SEVERE,"Error in driver class:",errors.toString());
            }            
            MyLogging.log(Level.INFO,"Driver found ....");
            MyLogging.log(Level.INFO, "Values ..{0}:{1}:{2}"+sieb_database+":"+ sieb_username+":"+ sieb_password);
            connection = DriverManager.getConnection(sieb_database, sieb_username, sieb_password);
            MyLogging.log(Level.INFO,"Connected");
            return connection;
        }catch (SQLException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN connecting To Siebel Database Method:"+errors.toString());
        }
        
        return connection;
    }
    
    public static SiebelDataBean connectSiebelServer() throws FileNotFoundException, IOException{
        initializePropertyValues();
        MyLogging.log(Level.INFO,"Connecting to Siebel .... ");
        SiebelDataBean dataBean = null;
        String connectString = String.format("Siebel://"+gateway_server+":"+gateway_port+"/"+entrpr_name+"/"+sieb_objmgr);
        MyLogging.log(Level.INFO,"Connection string is connectString:{0} ",connectString);
        try {
            dataBean = new SiebelDataBean();
            MyLogging.log(Level.INFO,"Username is:"+username+" Password is:"+password);
            dataBean.login(connectString, username, password, "enu");
            MyLogging.log(Level.INFO,"Connection SUCCESSFUL");
        }
        catch (SiebelException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN ConnectSiebelServer Method:"+errors.toString());
        }
        return dataBean;
    }
    
    public static void main(String[] args) throws SQLException, IOException{        
        try {
            //ApplicationsConnection adc = new ApplicationsConnection();
            SiebelDataBean ds = ApplicationsConnection.connectSiebelServer();
            Connection ebs_conn = ApplicationsConnection.connectToEBSDatabase();
            Connection sieb_conn = ApplicationsConnection.connectToSiebelDatabase();
            ds.logoff();
            ebs_conn.close();
            sieb_conn.close();
        } catch (SiebelException ex) {
            Logger.getLogger(ApplicationsConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
