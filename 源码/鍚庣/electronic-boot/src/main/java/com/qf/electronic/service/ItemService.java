package com.qf.electronic.service;

import com.qf.electronic.pojo.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItems(String itemType);
}
