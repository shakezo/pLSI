package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class PropertiesManager {

	public static String INDIR;
	public static String OUTDIR;

	public static int N_CLASS;
	public static int N_USER;
	public static int N_ITEM;
	public static int N_LIFESTYLE;

	public PropertiesManager() throws IOException{
		FileInputStream fis = new FileInputStream(new File("parameter.properties"));
		Properties props = new Properties();
		props.load(fis);
		fis.close();
		INDIR = props.getProperty("indier");
		OUTDIR = props.getProperty("outdir");
		N_CLASS = Integer.parseInt(props.getProperty("num_class"));
		N_ITEM  = Integer.parseInt(props.getProperty("num_item"));
		N_USER  = Integer.parseInt(props.getProperty("num_user"));
		N_LIFESTYLE = Integer.parseInt(props.getProperty("num_lifstyle"));
	}
}
