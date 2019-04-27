package com.epam.addressbook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private Map<String,String> env = new HashMap<>();

    public EnvController(@Value("${PORT:NOT SET}")String port,
                         @Value("${MEMORY_LIMIT:NOT SET}")String memory,
                         @Value("${CF_INSTANCE_INDEX:NOT SET}")String index,
                         @Value("${CF_INSTANCE_ADDR:NOT SET}")String address){
        env.put("PORT", port);
        env.put("MEMORY_LIMIT", memory);
        env.put("CF_INSTANCE_INDEX", index);
        env.put("CF_INSTANCE_ADDR", address);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){
        return env;
    }

}
