package cn.ingen.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

import cn.ingen.entity.Basicinformation;
import cn.ingen.entity.Detailedinformation;
import cn.ingen.entity.Enterpriseforeigninvestment;
import cn.ingen.entity.Mainstaff;
import cn.ingen.entity.Shareholdersinformation;
import cn.ingen.service.AtlasService;
import cn.ingen.service.EnterService;
import cn.ingen.util.JsonUtil;

@Controller
public class EnterController {

	@Autowired
	private EnterService eservice;
	@Autowired
	private AtlasService aservice;

	@RequestMapping(value = "detailed.action")
	public String detailed(Integer id, Model model, HttpSession session) throws UnsupportedEncodingException {
		session.setAttribute("CompanyId", id);
		Basicinformation enter = aservice.selectEnterById(id);
		List<Enterpriseforeigninvestment> foreigns = eservice.selectForeignById(id);
		Detailedinformation detail = eservice.seleDetailedEnterById(id);
		List<Shareholdersinformation> holders = eservice.selectholderBycomyId(id);
		List<Mainstaff> mains = eservice.selectMainBycomyId(id);
		if (detail != null) {
			model.addAttribute("enter", enter);
			model.addAttribute("mains", mains);
			model.addAttribute("foreigns", foreigns);
			model.addAttribute("holders", holders);
			model.addAttribute("detail", detail);
		} else {
			model.addAttribute("error", "nothing");
		}

		return "atlasjsp/detailed";
	}

	// 列表首页
	@RequestMapping(value = "list.action")
	public String list(@RequestParam(required = true, defaultValue = "1") Integer page, String ename, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// JiebaSegmenter segmenter = new JiebaSegmenter();
		// String name = segmenter.process(ename, SegMode.INDEX).toString();
		request.setCharacterEncoding("UTF-8");// 传值编码
		response.setContentType("text/html;charset=UTF-8");// 设置传输编码
		if (ename != null) {
			session.setAttribute("ename", ename);
		} else {
			return "redirect:/listByPage.action?ename=" + ename + "&page=" + page;
		}
		PageHelper.startPage(page, 8);
		List<Basicinformation> enterList = eservice.selectEnterByName(ename);
		PageInfo<Basicinformation> p = new PageInfo<Basicinformation>(enterList, 8);
		if (enterList != null) {
			model.addAttribute("page", p);
			model.addAttribute("enterList", enterList);
		} else {
			model.addAttribute("error", "nothing");
		}
		return "userjsp/list";
	}
    
	//筛选框
	@RequestMapping(value="screen.action")
	public void screen(){
		
	}
	
	// 分页列表
	@RequestMapping(value = "listByPage.action")
	public String listBypage(String ename, int page, Model model, HttpSession session) throws IOException {
		Object en = session.getAttribute("ename");
		ename = en.toString();
		PageHelper.startPage(page, 8);
		List<Basicinformation> enterList = eservice.selectEnterByName(ename);
		PageInfo<Basicinformation> p = new PageInfo<Basicinformation>(enterList, 8);
		if (enterList != null) {
			model.addAttribute("page", p);
			model.addAttribute("enterList", enterList);
		} else {
			model.addAttribute("error", "nothing");
		}
		return "userjsp/list";
	}

	// 自动补全1
	@RequestMapping(value = "autocomp.action")
	public void auto(HttpServletResponse response) {
		List<String> Ename = eservice.selectEnterAll();
		if(Ename.size()>10){
			
		}
		JsonUtil.renderString(response, Ename);
	}
	@RequestMapping(value = "autocomp1.action")
	public void autoBoss(HttpServletResponse response) {
		List<String> Ename = eservice.selectBossAll();
		if(Ename.size()>10){
			
		}
		JsonUtil.renderString(response, Ename);
	}
}
