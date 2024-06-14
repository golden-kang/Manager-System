package com.golden.ms.configuration;

import com.golden.ms.model.User;
import com.golden.ms.runtime.DatabaseMock;
import com.golden.ms.utils.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

import static com.golden.ms.model.Constants.ROLE_ADMIN;

@Configuration
public class RuntimeConfiguration {
    @Bean
    public DatabaseMock databaseMock(){
        DatabaseMock instance = DatabaseMock.instance;

        Map<Integer, User> map = FileUtil.readMapFromFile();
        if (map != null) {
            instance.initMap(map);
        }
        else{
            instance.insert(new User(123456, "admin", ROLE_ADMIN, Arrays.asList("resource A", "resource B", "resource C")));
            FileUtil.writeMapToFile(instance.getMap());
        }
        // Create a default user in case the whole system cannot be used


        return instance;
    }
}
