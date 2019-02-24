package com.bts.essentials.authentication.config;

import com.bts.essentials.authentication.advice.ExcludeJwtControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
class TestController {
    @RequestMapping(value = "/unsecured/resource", method = RequestMethod.GET)
    public ResponseEntity<String> unsecuredEndpoint() {
        return ResponseEntity.ok("You got in!");
    }

    @RequestMapping(value = "/secured/resource", method = RequestMethod.GET)
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("You had a jwt");
    }

    @RequestMapping(value = "/excluded/resource", method = RequestMethod.GET)
    @ExcludeJwtControllerAdvice
    public ResponseEntity<String> excludedEndpoint() {
        return ResponseEntity.ok("Didn't need a jwt");
    }
}
