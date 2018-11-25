/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogging {
    
  static Logger logger;
  public Handler fileHandler;
  Formatter plainText;
  private static InetAddress ip = null;
  private static String hIP = "";
  private static final String OS = System.getProperty("os.name").toLowerCase();
  String prop_file_path = "";
  String propfilepath;
  String vlogFile = "";
  String logFile = "";
  
  private MyLogging()throws IOException{
    ip = InetAddress.getLocalHost();
    hIP = ip.getHostAddress();
    if ((OS.contains("nix")) || (OS.contains("nux"))){
      this.prop_file_path = "/usr/app/siebel/intg/intg.properties";
      this.propfilepath = this.prop_file_path;
      this.vlogFile = "nix_logfile";      
    }
    else if (OS.contains("win")){
      this.propfilepath = "C:\\temp\\intg\\intg.properties";
      this.vlogFile = "win_logfile";      
    }
    getProperties();
    Date date = new Date();
    SimpleDateFormat app = new SimpleDateFormat("dd-MM-yyyy");
    String dateApp = app.format(date);
    logFile = this.logFile + "Siebel_Xentry_"+dateApp + ".log";
    logger = Logger.getLogger(MyLogging.class.getName());
    
    this.fileHandler = new FileHandler(logFile, true);
    
    this.plainText = new SimpleFormatter();
    this.fileHandler.setFormatter(this.plainText);
    logger.addHandler(this.fileHandler);
  }
  
  private void getProperties() throws FileNotFoundException, IOException
  {
    Properties prop = new Properties();
    
    //MyLogging.log(Level.INFO, "propfilepath is {0}", propfilepath);
    FileInputStream input = new FileInputStream(propfilepath);
    prop.load(input);   
    logFile = prop.getProperty(vlogFile);    
    //MyLogging.log(Level.INFO, "logFile is {0}", logFile);
            
  }
  
  private static Logger getLogger(){
    if(logger == null){
        try {
            new MyLogging();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return logger;
  }
  
  public static void log(Level level, String msg){
    getLogger().log(level, msg);
  }
  
  public static void log(Level level, String msg, Object obj){
    getLogger().log(level, msg, obj);
  }
  
    public static void main(String[] args) {
        MyLogging.log(Level.INFO, "TRUE");
    }
}

