package com.huawei.cse.porter.oauth2;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date 2018-04-19
 */
@RestController
@RequestMapping(path = "/")
public class TestEndpoint {

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "product id : " + id;
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String getOrder(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "order id : " + id;
    }
    
    @GetMapping("/order/order2/{id}")
    @PreAuthorize("hasRole('USER')")
    public String getOrder2(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "order id : " + id;
    }

}