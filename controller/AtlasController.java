package cn.ingen.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ingen.entity.Basicinformation;
import cn.ingen.entity.Detailedinformation;
import cn.ingen.entity.Enterpriseforeigninvestment;
import cn.ingen.entity.Mainstaff;
import cn.ingen.entity.Msg;
import cn.ingen.entity.Shareholdersinformation;
import cn.ingen.service.AtlasService;
import cn.ingen.service.EnterService;
import cn.ingen.util.JsonUtil;
import cn.ingen.util.Relation;
import cn.ingen.util.RelationLink;
import cn.ingen.util.ResultVo;
import cn.ingen.util.ResultVoByCom;
import cn.ingen.util.ResultVoByTree;
import cn.ingen.util.ResultVoByTreeSon;
import cn.ingen.util.ResultVoByenteratlas;
import cn.ingen.util.ResultVoByenteratlasLinks;
import cn.ingen.util.ResultVoByenteratlasLinnksAndNodes;
import cn.ingen.util.ResultVoByholder;
import cn.ingen.util.ResultVoByholderValue;
import cn.ingen.util.ResultVoLink1;

@Controller
@RequestMapping(value = "/Atlas")
public class AtlasController {

	@Autowired
	private AtlasService aservice;
	@Autowired
	private EnterService eservice;
  
	//股权结构饼状图
	@RequestMapping(value = "goownerstructAtlas.action")
	public String ownerstructAtlas(Model model,@RequestParam Integer id) {
		Basicinformation enter = aservice.selectEnterById(id);
		model.addAttribute("enter", enter);
		
		return "atlasjsp/ownerstructAtlas";
	}

	@RequestMapping(value = "goownerAtlasByJson.action")
	public void ownerByJson(HttpSession session, HttpServletResponse response) {
		String dd=null;
		Integer j=null;
		int id = Integer.parseInt(session.getAttribute("CompanyId").toString());
		List<Shareholdersinformation> holders = eservice.selectholderBycomyId(id);
		Basicinformation selectEnterById = aservice.selectEnterById(id);
		String ename = selectEnterById.getName();
		List<String> name = new ArrayList<String>();
		for (int i = 0; i < holders.size(); i++) {
			name.add(holders.get(i).getName());
		}
		List<ResultVoByholderValue> all = new ArrayList<ResultVoByholderValue>();
		for (int i = 0; i < holders.size(); i++) {
			if(holders.get(i).getSubscribedCapitalContribution().equals("-")){
				Detailedinformation detail = eservice.seleDetailedEnterById(id);
				String replace = detail.getRegisteredCapital();
				 dd = replace.replaceAll("[^\\d+]", "");
				 j = Integer.parseInt(dd);
			}else{
			 dd = holders.get(i).getSubscribedCapitalContribution().replaceAll("[^\\d+]", "");
			 j = Integer.parseInt(dd);
			}
			all.add(new ResultVoByholderValue(j, holders.get(i).getName()));
		}
		ResultVoByholder<ResultVoByholderValue, List<String>, String> holderdata = new ResultVoByholder<>(all, name,
				ename);
		JsonUtil.renderString(response, holderdata);
	}

	//企业图谱
	@RequestMapping(value = "goenteratlasByJson.action")
	public void EnterAtlasByJson(HttpSession session, HttpServletResponse response) throws IOException {
		ResultVoByenteratlas rbt = null;
		ResultVo<Enterpriseforeigninvestment, Shareholdersinformation, Mainstaff> entertlasByjson = null;
		ResultVoByenteratlasLinks rbtl = null;

		int id = Integer.parseInt(session.getAttribute("CompanyId").toString());
		Basicinformation selectEnterById = aservice.selectEnterById(id);
		String EnterName = selectEnterById.getName();
		List<Mainstaff> mainstaffs = aservice.selectMainBycomyId(id);
		List<Shareholdersinformation> holders = aservice.selectHolderById(id);
		List<Enterpriseforeigninvestment> foreigns = aservice.selectForeignById(id);
		List<ResultVoByenteratlas> resList = new ArrayList<ResultVoByenteratlas>();
		resList.add(new ResultVoByenteratlas(4, selectEnterById.getName(), 10));
		resList.add(new ResultVoByenteratlas(2, "股东", 2));
		resList.add(new ResultVoByenteratlas(10, "高管", 1));
		resList.add(new ResultVoByenteratlas(1, "历史股东", 1));
		resList.add(new ResultVoByenteratlas(1, "历史法人", 1));
		resList.add(new ResultVoByenteratlas(4, "裁判文书", 4));
		resList.add(new ResultVoByenteratlas(3, "法院公告", 3));
		resList.add(new ResultVoByenteratlas(1, "对外投资", 1));
		for (int i = 0; i < mainstaffs.size(); i++) {
			if (mainstaffs != null) {
				resList.add(new ResultVoByenteratlas(2, mainstaffs.get(i).getName(), 2));
			}
		}
		for (int i = 0; i < holders.size(); i++) {
			if (holders != null) {
				resList.add(new ResultVoByenteratlas(2, holders.get(i).getName(), 2));
			}
		}
		for (int i = 0; i < foreigns.size(); i++) {
			if (foreigns != null) {
				resList.add(new ResultVoByenteratlas(2, foreigns.get(i).getInvestedEnterpriseName(), 2));
			}
		}
		List<ResultVoByenteratlasLinks> rbtlList = new ArrayList<ResultVoByenteratlasLinks>();
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "股东", 1));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "高管", 2));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "历史股东", 3));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "历史法人", 4));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "裁判文书", 5));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "法院公告", 6));
		rbtlList.add(new ResultVoByenteratlasLinks(selectEnterById.getName(), "对外投资", 7));
		for (int i = 0; i < mainstaffs.size(); i++) {
			rbtlList.add(new ResultVoByenteratlasLinks("高管", mainstaffs.get(i).getName(), 2));

		}
		for (int i = 0; i < holders.size(); i++) {
			rbtlList.add(new ResultVoByenteratlasLinks("股东", holders.get(i).getName(), 1));
		}
		for (int i = 0; i < foreigns.size(); i++) {

			rbtlList.add(new ResultVoByenteratlasLinks("对外投资", foreigns.get(i).getInvestedEnterpriseName(), 7));
		}
		ResultVoByenteratlasLinnksAndNodes<ResultVoByenteratlasLinks, ResultVoByenteratlas, String> resblan = new ResultVoByenteratlasLinnksAndNodes<>(
				rbtlList, resList, EnterName);
		JsonUtil.renderString(response, resblan);
	}

	@RequestMapping(value = "goenteratlas.action")
	public String EnterAtlas(Model model,@RequestParam Integer id) {
		
		Basicinformation enter = aservice.selectEnterById(id);
		model.addAttribute("enter", enter);
		return "atlasjsp/enteratlas";
	}
	
	//股权结构树状图
	@RequestMapping(value="goownertree.action")
	public String ownerTree(){
		return "atlasjsp/ownertree";
	}
	
	@RequestMapping(value="goownertreeByJson.action")
	public void ownerTreeByJson(HttpServletResponse response,HttpSession session){
		int id = Integer.parseInt(session.getAttribute("CompanyId").toString());
		List<Enterpriseforeigninvestment> sons = aservice.selectForeignById(id);
		Basicinformation selectEnterById = aservice.selectEnterById(id);
		String EnterName = selectEnterById.getName();
		List<ResultVoByTreeSon> son =new ArrayList<ResultVoByTreeSon>();
		List<ResultVoByTree<String,ResultVoByTreeSon>> middle =new ArrayList<ResultVoByTree<String,ResultVoByTreeSon>>();
		for (int i = 0; i < sons.size(); i++) {
			son.add(new ResultVoByTreeSon(sons.get(i).getInvestedEnterpriseName(),sons.get(i).getInvestmentProportion()));
		}
		ResultVoByTree<String,ResultVoByTreeSon>  tree=new ResultVoByTree<>(EnterName,son);
		middle.add(tree);
		ResultVoByTree<String,ResultVoByTree<String,ResultVoByTreeSon>>  bigtree=new ResultVoByTree<>(EnterName,middle);
		JsonUtil.renderString(response, bigtree);
	}
	
	//对外投资
	@RequestMapping(value="gooutinvest.action")
	public String outInvest(Model model,@RequestParam Integer id){
		Basicinformation enter = aservice.selectEnterById(id);
		model.addAttribute("enter", enter);
		return "atlasjsp/outinvest";
	}
	
	@RequestMapping(value="gooutinvestByjson.action")
	public void outInvestByjson(HttpSession session, HttpServletResponse response){
		int id = Integer.parseInt(session.getAttribute("CompanyId").toString());
		Basicinformation selectEnterById = aservice.selectEnterById(id);
		String EnterName = selectEnterById.getName();
		
		List<Enterpriseforeigninvestment> foreigns = aservice.selectForeignById(id);
		List<ResultVoByTreeSon> foreign =new ArrayList<ResultVoByTreeSon>();
		for (int i = 0; i < foreigns.size(); i++) {
			foreign.add(new ResultVoByTreeSon(foreigns.get(i).getInvestedEnterpriseName(),foreigns.get(i).getRegisteredCapital()));
		}
		List<Shareholdersinformation> holders = aservice.selectHolderById(id);
		List<ResultVoByTreeSon> holder =new ArrayList<ResultVoByTreeSon>();
		for (int i = 0; i < holders.size(); i++) {
			holder.add(new ResultVoByTreeSon(holders.get(i).getName(),holders.get(i).getSubscribedCapitalContribution()));
		}
		List<ResultVoByTree<String,ResultVoByTreeSon>> middle =new ArrayList<ResultVoByTree<String,ResultVoByTreeSon>>();
		middle.add(new ResultVoByTree<>("股东",holder));
		middle.add(new ResultVoByTree<>("对外投资",foreign));
		ResultVoByTree<String,ResultVoByTree<String,ResultVoByTreeSon>>  father=new ResultVoByTree<>(EnterName,middle);
		JsonUtil.renderString(response, father);
	}
	
	//关联图谱
	@RequestMapping(value="gorelationAltas.action")
	public String relationAltas(HttpSession session,Model model ){
		int id = Integer.parseInt(session.getAttribute("CompanyId").toString());
		Basicinformation enter = aservice.selectEnterById(id);
		model.addAttribute("enter", enter);
		return "atlasjsp/relationShip";
	}
	
	@RequestMapping(value="gorelationAltasByJson.action")
	@ResponseBody
	public Msg relationAltasByjson(HttpServletResponse response){
		RelationLink relation=new RelationLink();
		Relation link = relation.link("江苏汉微系统集成有限公司", "北京京能电力股份有限公司");
		return Msg.success().add("link",link);
	}

}
