package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.TblAdmin;
import utils.JDBCHelp;
import utils.Myutil;

public class AdminDao {
	
	private JDBCHelp db=new JDBCHelp();
	
	
	/**
	 * ¹ÜÀíÔ±µÇÂ¼
	 * @param admin
	 * @return
	 */
	public TblAdmin login(TblAdmin admin) {
		String sql="select * from tbl_admin where raname=? and rapwd=?";
		List<Object> params=new ArrayList<>();
		params.add(admin.getRaname());
		params.add(admin.getRapwd());

		List<Map<String,Object>> list = db.executeQuery(sql, params);
		List<TblAdmin> loginAdmin = Myutil.ListMapToJavaBean(list, TblAdmin.class);

		if(loginAdmin.size()>0) {
			return loginAdmin.get(0);
		}else {
			return null;			
		}
	}
}
