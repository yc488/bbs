package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bean.Topic;
import dao.StopDao;
import dao.UserDao;

public class Myutil {
	private static StopDao sd=new StopDao();
	private static UserDao ud=new UserDao();
	/**
	 * ����fastJSON���߰�List<Map>תΪList<javabase>����
	 * 
	 * @param mList
	 * @param t
	 * @return
	 */	
	public static <T> List<T> ListMapToJavaBean(List<Map<String, Object>> mList, Class<T> t) {
		List<T> tList = new ArrayList<T>();
		for (Map<String, Object> map : mList) {
			// ��map����תΪjson�ַ���
			String strJson = JSON.toJSONString(map);
			// ��json�ַ���תΪjsonobject����
			JSONObject json = JSONObject.parseObject(strJson);
			// ��jsonobject����ת��javabase����
			tList.add(JSON.toJavaObject(json, t));
		}
		return tList;
	}

	/**
	 * ����fastJSON���߰�Map<String,String[]>תΪjavabase����
	 * 
	 * @param mList �����������������map����
	 * @param t     JavaBean����
	 * @return
	 */
	public static <T> T MapToJavaBean(Map<String, String[]> mMap, Class<T> t) {
		// ��Map<String, String[]>תΪMap<String, String>
		Set<Entry<String, String[]>> entrySet = mMap.entrySet();
		Map<String, String> map = new HashMap<>();
		for (Entry<String, String[]> entry : entrySet) {
			map.put(entry.getKey(), entry.getValue()[0]);
		}
		// ��map����תΪjson�ַ���
		String strJson = JSON.toJSONString(map);
		// ��json�ַ���תΪjsonobject����
		JSONObject json = JSONObject.parseObject(strJson);

		return JSON.toJavaObject(json, t);
	}

	/**
	 * ��֤����
	 */

	public static BufferedImage createImgCode(HttpSession session) {
		// ����ͼƬ�������
		BufferedImage bi = new BufferedImage(60, 22, BufferedImage.TYPE_INT_RGB);
		// ��ȡ����
		Graphics g = bi.getGraphics();
		// ���û�����ɫ
		g.setColor(new Color(187, 255, 255));
		// ����ɫ��䵽ָ��λ�úʹ�С
		g.fillRect(0, 0, 60, 22);

		// �����������
		Random r = new Random();

		g.setColor(Color.BLACK);
		// ������������ߣ�ʹͼ���е���֤�벻�ױ���������̽��
		/*
		 * for(int i=0;i<20;i++) { int x=r.nextInt(68); int y=r.nextInt(22); int
		 * x1=r.nextInt(5); int y1=r.nextInt(5); g.setColor(new Color(r.nextInt(255),
		 * r.nextInt(255), r.nextInt(255))); //x��yΪ�������꣬x1��y1Ϊ�յ������ g.drawLine(x, y,
		 * x+x1, y+y1); }
		 */

		// ׼��һ������ַ�������������ɵ���֤�룩
		String a = "";
		for (char i = 'A'; i < 'Z'; i++) {
			a += i;
		}
		for (char i = 'a'; i < 'z'; i++) {
			a += i;
		}
		for (char i = '0'; i < '9'; i++) {
			a += i;
		}
		char[] c = a.toCharArray();
		// ���ڴ�����ɵ���֤���ַ�
		StringBuffer s = new StringBuffer();
		// �������4���ַ�
		for (int i = 0, len = c.length; i < 4; i++) {
			// ��ȡ����±�
			int index = r.nextInt(len);
			// ���û�����ɫ
			g.setColor(new Color(r.nextInt(80), r.nextInt(150), r.nextInt(200)));
			// ���ַ���������ָ����λ��
			g.drawString(c[index] + "", 4 + i * 15, 18);
			// �����ɵ��ַ��浽�ַ�������������
			s.append(c[index]);
		}
		session.setAttribute("code", s.toString());
		return bi;
	}
/**
 * �����ʼ�
 * @param email
 * @return
 */
	public static String sendemail(String email) {
		// ���������֤��
		String code = String.valueOf((int)(Math.random()*99999)+10000);
		// ���±���Ϊ�û������Լ����������
		String smtphost = ConfigEmail.getInstance().getProperty("smtphost"); // �����ʼ�������
		String user = ConfigEmail.getInstance().getProperty("user"); // �ʼ���������¼�û���
		String password = ConfigEmail.getInstance().getProperty("password"); // �ʼ���������¼����
		String from = ConfigEmail.getInstance().getProperty("from"); // �������ʼ���ַ
		String to = email; // �������ʼ���ַ
		String subject = "BBS"; // �ʼ�����
		// �ʼ�����
		String body = "�װ����û�����������BBS������������֤�룺" + code + "Ϊ��ֹ��Ϣй¶,��ע�Ᵽ���ø�����Ϣ��";
		// ����Ϊ���ͳ����û�����Ķ�
		try {
			// ��ʼ��Properties�����
			Properties props = new Properties();
			// ����mail.smtp.host����
			props.put("mail.smtp.host", smtphost);
			// ����ʹ����֤
			props.put("mail.smtp.auth", "true");
			// ��ȡ�ǹ����session����
			Session ssn = Session.getInstance(props, null);
			// ����һ��Ĭ�ϵ�MimeMessage����
			MimeMessage message = new MimeMessage(ssn);
			// ����InternetAddress����
			InternetAddress fromAddress = new InternetAddress(from);
			// ����From: ͷ����header�ֶ�
			message.setFrom(fromAddress);
			// ����InternetAddress����
			InternetAddress toAddress = new InternetAddress(to);
			// ���� To: ͷ����header�ֶ�
			message.addRecipient(Message.RecipientType.TO, toAddress);
			// ���� Subject: header�ֶ�
			message.setSubject(subject);
			// �������õ�ʵ����Ϣ
			message.setText(body);
			// ���巢��Э��
			Transport transport = ssn.getTransport("smtp");
			// �����������������
			transport.connect(smtphost, user, password);

			// �����ʼ�
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			// transport.send(message);
			// �ر��ʼ�����
			transport.close();
			System.out.println("�ʼ����ͳɹ�");
		} catch (Exception m) // �����쳣
		{
			System.out.println("�ʼ�ϵͳ�쳣");
			m.printStackTrace();
		}
		return code;
	}
	
	
	
	public static void sendemail(String email,Timestamp time) {
		// ���±���Ϊ�û������Լ����������
		String smtphost = ConfigEmail.getInstance().getProperty("smtphost"); // �����ʼ�������
		String user = ConfigEmail.getInstance().getProperty("user"); // �ʼ���������¼�û���
		String password = ConfigEmail.getInstance().getProperty("password"); // �ʼ���������¼����
		String from = ConfigEmail.getInstance().getProperty("from"); // �������ʼ���ַ
		String to = email; // �������ʼ���ַ
		String subject = "BBS"; // �ʼ�����
		// �ʼ�����
		String body = "�װ����û�,���ѱ�����,���Խ�ֹʱ��" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time.getTime())) + 
				"Ϊ��ֹ��Ϣй¶,��ע�Ᵽ���ø�����Ϣ��";
		// ����Ϊ���ͳ����û�����Ķ�
		try {
			// ��ʼ��Properties�����
			Properties props = new Properties();
			// ����mail.smtp.host����
			props.put("mail.smtp.host", smtphost);
			// ����ʹ����֤
			props.put("mail.smtp.auth", "true");
			// ��ȡ�ǹ����session����
			Session ssn = Session.getInstance(props, null);
			// ����һ��Ĭ�ϵ�MimeMessage����
			MimeMessage message = new MimeMessage(ssn);
			// ����InternetAddress����
			InternetAddress fromAddress = new InternetAddress(from);
			// ����From: ͷ����header�ֶ�
			message.setFrom(fromAddress);
			// ����InternetAddress����
			InternetAddress toAddress = new InternetAddress(to);
			// ���� To: ͷ����header�ֶ�
			message.addRecipient(Message.RecipientType.TO, toAddress);
			// ���� Subject: header�ֶ�
			message.setSubject(subject);
			// �������õ�ʵ����Ϣ
			message.setText(body);
			// ���巢��Э��
			Transport transport = ssn.getTransport("smtp");
			// �����������������
			transport.connect(smtphost, user, password);

			// �����ʼ�
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			// transport.send(message);
			// �ر��ʼ�����
			transport.close();
			System.out.println("�ʼ����ͳɹ�");
		} catch (Exception m) // �����쳣
		{
			System.out.println("�ʼ�ϵͳ�쳣");
			m.printStackTrace();
		}
	}
	
	/**
	 * �ж��������������Ƿ�������д�
	 * @param topic
	 * @return
	 */
	public static Topic filter(Topic topic) {
		String beforeTitle=topic.getTitle();
		String beforeContent=topic.getContent();
		String afterTitle=beforeTitle;
		String afterContent=beforeContent;
		List<Map<String,Object>> list=sd.query();
		for(int i=0;i<list.size();i++) {
			afterTitle=afterTitle.replace((String)list.get(i).get("sname"), "**");
			afterContent=afterContent.replace((String)list.get(i).get("sname"), "**");
		}
		if(!beforeTitle.equals(afterTitle)||!beforeContent.equals(afterContent)) {
			ud.addTime(topic.getUid());
		}
		topic.setTitle(afterTitle);
		topic.setContent(afterContent);
		return topic;
	}
}
