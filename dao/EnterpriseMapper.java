package cn.ingen.dao;

import java.util.List;

import cn.ingen.entity.Enterpriseforeigninvestment;

public interface EnterpriseMapper {
    List<Enterpriseforeigninvestment> selectForeignById(int CompanyId);
}
