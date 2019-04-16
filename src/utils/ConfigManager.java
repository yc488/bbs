package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;





/**
 * ������ȡ�����ļ���Ϣ
 * @author Administrator
 * ����ģʽ:�ڳ��������ڼ�һ����ֻ�ᴴ��һ��ʵ��
 * �ص�:
 * 1.���췽��˽��
 * 2.˽�о�̬�ĳ�Ա����(�����ǵ�ǰ�������,Ψһ������ʵ��)
 * 3.���⹫���Ļ�ȡʵ���ķ���
 * 4.����ģʽһ���Ϊ����:һ������ʽ,����һ���Ƕ���ʽ
 * ʹ�ó���:һ������������Դ
 */

public class ConfigManager extends Properties{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3996121909392986425L;
	public static ConfigManager cm=new ConfigManager();
	private ConfigManager() {
		InputStream is=ConfigManager.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			this.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static ConfigManager getInstance() {
		return cm;
	}
	
	//����ʽ�������һ��Ҫ��synchronized�ؼ��֣���ֹͬһʱ���ж���û�ȥ����ʵ�������ǾͲ��ǵ���
	/*public synchronized static ConfigManager getInstance() {
		if(cm==null) {
			cm=new ConfigManager();
		}else {
			return cm;
		}
	}*/
	
	
	/*public String getValue(String key) {
		//�ö���������ȡproperties�����ļ�
		Properties properties =new Properties();
		InputStream fis=null;		
		try {
			//Java��Ŀ���������ַ�ʽ��ȡ�ļ���web��Ŀ�����ַ�ʽ�Ļ��ص�������·����ȥ������ļ�
			//fis=new FileInputStream("src/db.properties");
			//web��Ŀ���������ַ�ʽ��ȡ��Ŀ·���µ��ļ�
			fis=ConfigManager.class.getClassLoader().getResourceAsStream("db.properties");
			//�ӵ�properties��������
			properties.load(fis);
			//������������ȡ��Ӧ������ֵ
			return properties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Test
	public void test() {
		System.out.println(getValue("driverClass"));
	}*/
}
