package cn.ingen.dao;

import java.util.List;

import cn.ingen.entity.Shareholdersinformation;

public interface ShareholderMapper {
     List<Shareholdersinformation> selectholderBycomyId(int CompanyId);
     List<Integer> selIdByHolderName(String Name);
}
