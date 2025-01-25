package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.ResponseResult;
import com.atyan.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("link")
public class LinkController {

    @Autowired
    private LinkService linkService;


    @GetMapping("/getAllLink")
    @mySystemlog(businessName = "获取友链列表")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}