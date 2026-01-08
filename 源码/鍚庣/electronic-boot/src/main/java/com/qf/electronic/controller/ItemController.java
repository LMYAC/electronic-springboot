package com.qf.electronic.controller;

import com.qf.electronic.pojo.Item;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/{itemType}")
    @PreAuthorize("hasAnyRole('ROLE_LINE', 'ROLE_INSEPECTION', 'ROLE_DEFECT')")
    public Result<List<Item>> getItems(@PathVariable("itemType") String itemType){
        return Result.ok(itemService.getItems(itemType));
    }
}
