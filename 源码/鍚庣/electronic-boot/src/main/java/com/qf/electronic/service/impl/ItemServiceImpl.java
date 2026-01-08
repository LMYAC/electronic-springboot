package com.qf.electronic.service.impl;

import com.qf.electronic.mapper.ItemMapper;
import com.qf.electronic.pojo.Item;
import com.qf.electronic.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Item> getItems(String itemType) {
        return itemMapper.getItems(itemType);
    }
}
