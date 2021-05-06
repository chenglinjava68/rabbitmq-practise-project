package com.hualala.springevent.controller;


import com.hualala.core.vo.AjaxResult;
import com.hualala.springevent.service.EventPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author cheng
 */
@RestController
@RequestMapping("/api")
public class SpringEventController {

    @Autowired
    private EventPublish eventPublish;

    @RequestMapping("/publishMsg")
    public AjaxResult publishMsg() {
        eventPublish.publish();
        return AjaxResult.success();
    }

}
