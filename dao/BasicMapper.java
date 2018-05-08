package cn.ingen.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ingen.entity.Basicinformation;

public interface BasicMapper {
	Basicinformation selectEnterById(int Id);
	List<Basicinformation> selectEnterByName(String Name);
	List<Basicinformation> selectEnterByNamePage(@Param("Name")String Name,@Param("line")Integer line,@Param("cloumn")Integer cloumn);
	List<String> selectEnterAll();
	List<String> selectBossAll();
	int selectIdbyName(String name);
}
