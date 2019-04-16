<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<%@ include file="../adminPages/links.jsp"%>
<!--      导航        -->
<DIV>
	&gt;&gt;<B><a href="<%=request.getContextPath() %>/index.jsp">论坛首页</a></B>
</DIV>
<!--      用户登录表单        -->
<DIV class="t" style="MARGIN-TOP: 15px" align="center">
	<FORM name="loginForm" action="<%=request.getContextPath() %>/bbsUser" method="post">
		<input type="hidden" name="flag" value="userLogin">
		<br/>
		用户名 &nbsp;
		<INPUT  tabIndex="1"  type="text"  class="easyui-textbox" data-options="iconCls:'icon-man'"  name="uName" value="${param.uName}">
		<br/>
		密　码 &nbsp;
		<INPUT  tabIndex="2"  type="password"   class="easyui-textbox" data-options="iconCls:'icon-lock'" name="uPass" value="${param.uPass}">
		<br/>
		验证码 &nbsp;
		<input class="easyui-textbox" tabindex="3" type="text" name="code">	
		<br/>
		<img id="code" src="<%=request.getContextPath() %>/createCode.jsp" onclick="updateCode()" style="vertical-align: bottom;">
		<a href="javascript:updateCode()">换一张</a>
		<script type="text/javascript">
			function updateCode() {
				var img=document.getElementById("code");
				//相同的url请求，浏览器会在缓存里加载数据并不会往服务器重新发送，所以后面加一个随机数
				img.src="<%=request.getContextPath() %>/createCode.jsp?"+Math.random();
			}
		</script>
		<br/>
		<INPUT style="width: 200px;height: 25px;" class="easyui-linkbutton" tabIndex="6" type="submit" value="登 录">
		<br/>
		<a href="<%=request.getContextPath() %>/pages/resetpwd.jsp">忘记密码</a>
		<br>
		<font style="color: red;">${msg}</font>
	</FORM>
</DIV>
 <%@ include file="bottom.jsp"%>