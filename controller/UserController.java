package cn.ingen.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.ingen.entity.User;

import cn.ingen.service.UserService;
//import cn.ingen.util.SmsSDKUtils;
import cn.ingen.util.SmsSDKUtils;

@Controller
public class UserController {

	@Autowired
	private UserService uservice;
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "toAdmin.action")
	public String toAdmin(Model model) {
		return "adminjsp/admin-index";

	}

	@RequestMapping(value = "toUser.action")
	public String toUser(Model model, HttpServletRequest request) {
		String userphone = request.getSession().getAttribute("userPhone").toString();
		User user = uservice.selectUserByPhone(userphone);
		model.addAttribute("user", user);
		return "adminjsp/admin-user";

	}

	@RequestMapping(value = "toHelp.action")
	public String toHelp(Model model) {
		return "adminjsp/admin-help";

	}

	@RequestMapping(value = "toForm.action")
	public String toForm(Model model) {
		return "adminjsp/admin-form";

	}

	@RequestMapping(value = "toGallery.action")
	public String toGallery(Model model) {
		return "adminjsp/admin-gallery";

	}

	@RequestMapping(value = "toLog.action")
	public String toLog(Model model) {
		return "adminjsp/admin-log";

	}

	@RequestMapping(value = "toTable.action")
	public String toTable(Model model) {
		return "adminjsp/admin-table";

	}

	@RequestMapping(value = "to404.action")
	public String to404(Model model) {
		return "adminjsp/admin-404";

	}

	// test
	@RequestMapping(value = "selUserAll.action")
	public String selUserAll(Model model) {
		List<User> users = uservice.selUserAll();
		model.addAttribute("users", users);
		return "userjsp/select";
	}

	// 进入登录界面
	@RequestMapping(value = "login.action")
	public String login(String userphone, String userpwd, Model model, HttpServletRequest request) {
		User selectUserByPhone = uservice.selectUserByPhone(userphone);
		if (selectUserByPhone != null) {
			String tuserpwd = selectUserByPhone.getUserpwd();
			if (userpwd.equals(tuserpwd)) {
				Date date=new Date();
				SimpleDateFormat dd=new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
			    request.getSession().setAttribute("newdate", dd.format(date));  
				
				request.getSession().setAttribute("userPhone", userphone);
				return "userjsp/AfterLogin";

			} else {
				model.addAttribute("error2", "请输入正确密码！");
				return "userjsp/Login";
			}
		} else {
			model.addAttribute("error1", "请注册或请输入正确号码！");
			return "userjsp/Login";
		}
	}

	@RequestMapping(value = "aftermain.action")
	public String aftermain() {
		return "userjsp/AfterLogin";
	}

	@RequestMapping(value = "register.action")
	public String register(Model model, String phone, String name, String password) {
		if (phone != null && name != null && password != null) {
			User user = new User();
			user.setUsername(name);
			user.setUserphone(phone);
			user.setUserpwd(password);
			uservice.insertUser(user);
		}
		return "userjsp/Login";
	}
@RequestMapping(value="ContactAfterLogin.action")
public String Contact(){
	return "userjsp/ContactAfterLogin";
}
	// 验证登录进入主界面
	@RequestMapping(value = "gomain.action")
	public String main() {
		return "userjsp/main";
	}

	// 重置密码界面
	@RequestMapping(value = "resetpwd.action")
	public String resetpwd(Model model) {
		return "userjsp/resetpwd";
	}

	// 随机重置密码
	public String getCode(HttpServletRequest request) {
		Random rand = new Random();
		String code = "";
		for (int i = 0; i < 8; i++) {
			code += rand.nextInt(10);
		}
		// code="12345678";
		request.getSession().setAttribute("code", code);

		return code;
	}

	// 发送重置密码
	@RequestMapping(value = "sendMessage.action")
	public void sendMessage(String userphone, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String code = getCode(request);
		request.getSession().setAttribute("userphone", userphone);
		// userphone="15755332385";
		System.out.println(userphone);
		SmsSDKUtils.sendMessageCode(userphone, code, response);
		response.getWriter().write("true");
		System.out.println(code);
		response.flushBuffer();
	}

	//更改用户信息
	@RequestMapping(value = "toUpdateUser.action")
	public String toUpdateUser(String email, String QQ, String weibo, String intro, Object userid) {
		userid = request.getParameter("userid");
		System.out.println(userid);
		User user = new User();
		user.setUserid(userid);
		user.setEmail(email);
		user.setQQ(QQ);
		user.setTwitter(weibo);
		user.setIntro(intro);
		uservice.updateUserByPhone(user);

		return "adminjsp/admin-user";
	}

	//笔记记录
	@RequestMapping(value="toUpdateNote.action")
	public String toUpdateNote(String note,String userphone)
		{
			note=request.getParameter("note");
			System.out.println(note);
			User user=new User();
			user.setMynote(note);
			user.setUserphone(userphone);
			uservice.updateUserNote(user);
		return"adminjsp/admin-user";
		}
	@RequestMapping(value="BossAfterLogin.action")
	public String Boss()
	{
		return "userjsp/BossAfterLogin";
	}
}
