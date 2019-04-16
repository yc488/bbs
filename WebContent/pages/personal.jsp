<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="header.jsp"%>
<%@ include file="../adminPages/links.jsp"%>
<style type="text/css">
.show a:hover {
	color: blue;
}

#usermessage {
	width: 50%;
	float: left;
	display: inline;
}
</style>

<DIV>
	&gt;&gt;<B><a href="<%=request.getContextPath() %>/index.jsp">论坛首页</a></B>&gt;&gt;个人中心
	
</DIV>
<table width="89%"  align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td class="show" width="243" height="50" align="center"
			valign="middle" bgcolor="#E0F0F9"><h3 id="showMsg">
				<a href="javascript:void(0)">信息展示</a>
			</h3></td>
	</tr>
</table>
	<div id="usermessage" style="width: 88.5%; border: 2px solid #E0F0F9;margin-left: 5.6%;height: 280px" align="center">
		<table >
			<tr>
				<td height="10" colspan="1"><img
					src="image/head/${user.head }"></td>
			</tr>
			<tr>
				<td height="20" colspan="2"><p class="style3">姓名：${user.uname }</p></td>
			</tr>
			<tr>
				<td height="20" colspan="3">
					<p class="style3">
						性別：
						<c:if test="${user.gender==2 }">
							男
						</c:if>
						<c:if test="${user.gender == 1 }">

							女
						</c:if>
					</p>
				</td>
			</tr>
			<tr>
				<td height="20" colspan="4"><p class="style3">注册时间：${user.regtime }</p></td></br>
			</tr>
			
			<tr>
				<td height="20" colspan="5"><a href="<%=request.getContextPath() %>/topic?flag=myTopic&uid=${user.uid}" style="width: 200px;height: 25px;" class="easyui-linkbutton">点击查看我的帖子</a></td>
				<td height="20" colspan="5"><a href="<%=request.getContextPath() %>/pages/pwdChange.jsp" style="width: 200px;height: 25px;" class="easyui-linkbutton">点击修改用户密码</a></td>
			
			</tr>
		</table>
</div>