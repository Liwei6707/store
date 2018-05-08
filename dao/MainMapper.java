package cn.ingen.dao;

import java.util.List;

import cn.ingen.entity.Mainstaff;

public interface MainMapper {
   List<Mainstaff> selectMainBycomyId(int CompanyId);
   List<Mainstaff> selComyByMainName(String Name);
}
