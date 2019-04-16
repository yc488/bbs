<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ include file="header.jsp"%>
<%@ include file="../adminPages/links.jsp"%>


<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript">
var path="<%=request.getContextPath()%>"
	function sendcode(){
		if( $("#email").val() != null ){				
			$.ajax({
				url:"<%=request.getContextPath()%>/bbsUser?flag=sendcode",
				method:"post",
				dataType:"text",
				data:{"email":$("#email").val()},
				async:true,
				success:function(data){
					if(data!=null){
						alert("发送成功");
					}		
				},
				error:function(){
					alert("服务器异常！！！");
				}
			})
			}else{
				alert("请输入您的邮箱地址！！");
			}
	}


function checkUserInfo() {
		if (document.getElementById("samePwd").innerHTML == "密码不一致") {
			alert("密码不一致");
			return false;
		}
	}
	
	function isSamePwd() {
		if(document.pwdForm.upass.value!=document.pwdForm.upass1.value){
			document.getElementById("samePwd").innerHTML="密码不一致";
		}
		if(document.pwdForm.upass.value==document.pwdForm.upass1.value){
			document.getElementById("samePwd").innerHTML="";
		}
	}
	
	function isEmail(input) {
		$.post(path+"/bbsUser",{"flag":"isEmail","email":input.value},function(data){
			if(data.trim()=="1"){
				document.getElementById("info2").style.color="green";
				document.getElementById("info2").innerHTML="邮箱已注册";
				document.getElementById("getCode").style.display="inline-block";
			}else if(data.trim()=="2"){
				document.getElementById("info2").style.color="red";
				document.getElementById("info2").innerHTML="邮箱不能为空";
				document.getElementById("getCode").style.display="none";
			}else{
				document.getElementById("info2").style.color="red";
				document.getElementById("info2").innerHTML="邮箱未注册";
				document.getElementById("getCode").style.display="none";
			}
		},"text");
	}
</script>

<DIV>
	&gt;&gt;<B><a href="<%=request.getContextPath() %>/index.jsp">论坛首页</a>&gt;&gt;重置密码</B>
</DIV>

<div class="t" id="pwdchange" style="MARGIN-TOP: 15px">
	<div class="container" style="margin-left: 40%;">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<FORM onSubmit="return checkUserInfo()" name="pwdForm"
					action="<%=request.getContextPath()%>/bbsUser" method="post">
					<input type="hidden" name="flag" value="resetpwd"> 
					
					<span>&nbsp;新&nbsp;密&nbsp;码：</span>
					<input type="password" name="upass" class="input" required="required"><br/> 
					<span>重复密码：</span>
					<input type="password" name="upass1" oninput="isSamePwd()" class="input" required="required"><font style="color: red;" id="samePwd" ></font><br/>
					<span>&nbsp;邮&nbsp;&nbsp;&nbsp;&nbsp;箱：&nbsp;&nbsp;</span>
					<input type="email" name="email" id="email" class="input" required="required" oninput="isEmail(this)">
					<a href="javascript:void(0)" onclick="sendcode()" id="getCode" style="display: none;">获取验证码</a>
					<font style="color: red;" id="info2"></font>
					<br/>
					
					<span>&nbsp;验&nbsp;证&nbsp;码：&nbsp;&nbsp;</span><input type="text" name="mycode" class="input" required="required"><br/>
					
					<font style="color: red;">${msg}</font>
					<font style="color: green;">${success}</font>
					<br/>
					<INPUT style="width: 200px;height: 25px;" class="easyui-linkbutton" tabIndex="4" type="submit" value="重置">
				</FORM>
			</div>
		</div>
	</div>
</div>
<%@ include file="bottom.jsp"%>
