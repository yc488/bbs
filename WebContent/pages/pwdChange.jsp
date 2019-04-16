<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="header.jsp"%>
<%@ include file="../adminPages/links.jsp"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.9.1.js"></script>
<script type="text/javascript">
	
	var path="<%=request.getContextPath()%>"
	function checkUserInfo() {
		if (document.pwdForm.upass.value == "") {
			alert("密码不能为空");
			return false;
		}
		if (document.getElementById("samePwd").innerHTML == "密码不一致") {
			alert("密码不一致");
			return false;
		}
	}
	
	function isSamePwd() {
		if(document.pwdForm.newpass.value!=document.pwdForm.newpass1.value){
			document.getElementById("samePwd").innerHTML="密码不一致";
		}
		if(document.pwdForm.newpass.value==document.pwdForm.newpass1.value){
			document.getElementById("samePwd").innerHTML="";
		}
	}
	</script>
	
	<style type="text/css">
	.show a:hover {
		color: blue;
	}
	#pwdchange {
	width: 50%;
	float: left;
	display: inline;
}
</style>

<DIV>
	&gt;&gt;<B><a href="<%=request.getContextPath() %>/index.jsp">论坛首页</a></B>&gt;&gt;
	<a href="<%=request.getContextPath() %>/pages/personal.jsp">个人中心</a>&gt;&gt;
	密码修改
</DIV>
<table width="89%"  align="center" cellpadding="0"cellspacing="0">
	<tr>
		<td class="show" width="268" height="50" align="center" bgcolor="#E0F0F9">
				<h3 id="pwd">
					<a href="javascript:void(0)">修改密码</a>
				</h3>
		</td> 
	</tr>
</table>

<div id="pwdchange" style="width: 88.5%; border: 2px solid #E0F0F9;margin-left: 5.6%; height: 200px">
		<div class="container" style="margin-left: 40%">
			<div class="row clearfix">
				<div class="col-md-12 column">
					<FORM onSubmit="return checkUserInfo()" name="pwdForm"
						action="<%=request.getContextPath()%>/bbsUser" method="post">
						<input type="hidden" name="flag" value="pwdchange"> 
						<br />原&nbsp;密&nbsp;码
						&nbsp; 
						<INPUT class="input" tabIndex="1" type="text" name="upass">
						<font id="info"></font>
						 <br />新&nbsp;密&nbsp;码 &nbsp;
						  <INPUT class="input" tabIndex="2" type="password" name="newpass" > 
						<br />重复密码&nbsp; 
						<INPUT class="input" tabIndex="3" type="password"  name="newpass1" onblur="isSamePwd()"> <font
							style="color: red;" id="samePwd"></font> 
							<br /> <br /> 
							<INPUT style="width: 200px;height: 25px;" class="easyui-linkbutton" tabIndex="4" type="submit" value="修改"> <br />
					</FORM>
				</div>
					</div>
		</div>
				<%
			String msg = (String) request.getAttribute("msg");
			if (msg == null) {
				msg = "";
			}
			if (msg.equals("修改成功,请重新登录")) {
		%>

		<script type="text/javascript" language="javascript">
					alert("<%=msg%>");
					
					window.location='<%=request.getContextPath()%>/bbsUser?flag=logAgain';
		</script>
		<%
			}else if(!msg.equals("")){
		%>
			<script type="text/javascript" language="javascript">
					alert("<%=msg%>");
			</script>
				
		<%
			}
		%>
		</div>
	