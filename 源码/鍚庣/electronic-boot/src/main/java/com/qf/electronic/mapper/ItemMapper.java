package com.qf.electronic.mapper;

import com.qf.electronic.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> getItems(@Param("type") String type);
}
