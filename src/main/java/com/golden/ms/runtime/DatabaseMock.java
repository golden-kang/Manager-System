package com.golden.ms.runtime;

import com.golden.ms.model.User;
import com.golden.ms.utils.FileUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.golden.ms.model.Constants.ROLE_ADMIN;

// Mock Database, in reality this part can be a real database
public enum DatabaseMock {
    instance();

    private Map<Integer, User> map = new HashMap<>();

    public void initMap(Map<Integer, User> map){
        this.map=map;
    }

    public void insert(User user){
        map.put(user.getUserId(), user);

        // Just keep simple, write map to file
        // This is not an efficient way to do so, a good database system should be designed
        FileUtil.writeMapToFile(map);
    }

    public Map<Integer, User> getMap(){
        return map;
    }

    // currently update and insert shares the same implementation, but if partial update is required, this new method
    // can be written
    public void update(User user){
        map.put(user.getUserId(), user);
        FileUtil.writeMapToFile(map);
    }
    public User get(int userId) {
        return map.get(userId);
    }

    // used for test
    public void clear() {
        map.clear();
        insert(new User(123456, "admin", ROLE_ADMIN, Arrays.asList("resource A", "resource B", "resource C")));
        FileUtil.writeMapToFile(map);
    }
}
