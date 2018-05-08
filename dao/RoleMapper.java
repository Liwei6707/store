package cn.ingen.dao;

import java.util.List;

import cn.ingen.entity.Role;

public interface RoleMapper {
	
	List<Role> selectRoleBycomyId(int CompanyId);

}
