package com.zzx.executor.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PhoneDao {

    @Select("SELECT money FROM t_trade where trade_type=#{tradeType} ORDER BY insertime desc LIMIT 0,1;")
    String getcode(@Param("tradeType") String tradeType);
    @Select("SELECT phone from pdd_buyer where failure=1;")
    List<String> getphone();
    @Update("UPDATE pdd_buyer set access_token=#{accessToken},failure=0 where phone=#{phone}")
    int updateuser(@Param("accessToken") String accessToken,
                   @Param("phone") String phone);

}
