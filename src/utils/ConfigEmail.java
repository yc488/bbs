package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigEmail extends Properties{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1987957090214352013L;
	public static ConfigEmail ce = new ConfigEmail();
	private ConfigEmail() {
		InputStream is = ConfigEmail.class.getClassLoader().getResourceAsStream("email.properties");
		try {
			this.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if( is != null ) {
				try {
					is.close();//¹Ø±ÕÁ÷
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static ConfigEmail getInstance() {
		return ce;
	}

}
