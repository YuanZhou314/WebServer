完成自改用于密码操作

首先用户请求修改密码页面：update.html,然后在该页面中输入要修改密码的用户的
用户名及原密码和新密码三项，然后点击修改按钮进行修改

若修改成功则显示修改成功提示页面，若用户名或原密码输入有误，
则提示用户名或原密码错误的提示页面

实现：
1：准备页面，在webapps/myweb目录下提供三个页面：
	update.html:修改页面
	该页面中form表单提交的路径为action="update",提交方式为method="post"
	表单要求提供三个输入域，分别表示：
	用户名(username),原密码(oldpwd),新密码(newpwd)
	
	update_success.html:修改成功提示页面
	
	update_fail.html:修改失败提示页面
	
2：在com.webserver.servlet包中添加类:UpdateServlet
	该类需要继承Servlet并重写service方法。
	在方法中首先通过request获取用户的输入的修改信息
	然后根据用户名查找user.dat问价对应用户，并匹配该用户密码是否与提交上来的原密码一致
	一致则将新密码覆盖原密码，并跳转修改成功页面，若密码不一致或没有此用户则跳转修改失败页面
	
3：修改conf/servlets.xml文件，将表单请求/myweb/update与处理类UpdateServlet关联起来，否则ClientHandler
	不能通过这个请求来到UpdateServlet，结果就会出现404的情况