package utils;
/**
 * JDBC������(��װ�����������ɾ�Ĳ����)
 * @author Administrator
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class JDBCHelp {
	static {
		try {
			Class.forName(ConfigManager.getInstance().getProperty("driverClass"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//�������Ӳ���ȡ���Ӷ���
	public Connection getConnection() throws SQLException {
		String url=ConfigManager.getInstance().getProperty("url");
//		String user=ConfigManager.getInstance().getProperty("user");
//		String password=ConfigManager.getInstance().getProperty("password");
		return DriverManager.getConnection(url,ConfigManager.getInstance());
	}
	/**
	 * ��Ԥ��������е�ռλ�����ò���
	 * @param ps
	 * @param params
	 * @throws SQLException
	 */
	public void setParams(PreparedStatement ps,Object...params) throws SQLException {
		if(params!=null) {
			for(int i=0;i<params.length;i++) {
				ps.setObject(i+1, params[i]);
			}
		}
	}
	public void setParams(PreparedStatement ps,List<Object> params) throws SQLException {
		if(params!=null) {
			for(int i=0;i<params.size();i++) {
				ps.setObject(i+1, params.get(i));
			}
		}
	}
	/**
	 * �ͷ���Դ
	 */
	public void close(Connection con,PreparedStatement ps) {
		if(ps!=null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void close(Connection con,PreparedStatement ps,ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps!=null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ͨ�õ���ɾ�ķ���
	 */
	public  int executeUpdate(String sql,Object...params) {
		Connection con=null;
		PreparedStatement ps=null;
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			setParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con,ps);
		}
		return 0;
	}
	
	public  int executeUpdate(String sql,List<Object> params) {
		Connection con=null;
		PreparedStatement ps=null;
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			setParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con,ps);
		}
		return 0;
	}
	/**
	 * ͨ�õĲ��Ҳ���
	 */
	public List<Map<String,Object>> executeQuery(String sql,Object...params){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		//����������map�ṹ��ŵļ�¼
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			setParams(ps, params);
			rs = ps.executeQuery();
			//��ȡ���ݿ��ṹ��Ԫ����(�������ݿ��ṹ�����ݣ����ǿ��Ի�ȡ����ṹ�������Ϣ)
			ResultSetMetaData metaData = rs.getMetaData();
			//��ȡÿһ����¼������(�ֶ���)
			int count=metaData.getColumnCount();
			
			while(rs.next()) {
				//������ż�¼��map���ϣ�һ����¼���ֶεķ�ʽ�����map���棬����Ϊkey��ֵΪvalue
				Map<String,Object> map=new HashMap<>();
				for(int i=0;i<count;i++) {
					//getColumnName():��ȡ����
					map.put(metaData.getColumnName(i+1), rs.getObject(metaData.getColumnName(i+1)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con,ps,rs);
		}
		return list;
	}
	public List<Map<String,Object>> executeQuery(String sql,List<Object> params){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			setParams(ps, params);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int count=metaData.getColumnCount();
			while(rs.next()) {
				Map<String,Object> map=new HashMap<>();
				for(int i=0;i<count;i++) {
					map.put(metaData.getColumnName(i+1), rs.getObject(metaData.getColumnName(i+1)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con,ps,rs);
		}
		return list;
	}
}
