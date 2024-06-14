package com.golden.ms.service;

import com.golden.ms.controllers.model.AddUserRequest;
import com.golden.ms.controllers.model.UpdateUserRequest;
import com.golden.ms.exceptions.model.NotFoundException;
import com.golden.ms.model.User;
import com.golden.ms.runtime.DatabaseMock;
import com.golden.ms.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.golden.ms.model.Constants.ROLE_ADMIN;
import static com.golden.ms.model.Constants.ROLE_USER;

@Service
public class UserManagementService {
    @Autowired
    protected DatabaseMock databaseMock;

    public User getUser(int userId){
        return databaseMock.get(userId);
    }

    public void addUser(AddUserRequest request){
        databaseMock.insert(new User(request.getUserId(), request.getAccountName(), ROLE_USER, request.getEndpoints()));
    }

    public void updateUser(int userId, UpdateUserRequest request) {
        databaseMock.update(new User(userId, request.getAccountName(), ROLE_USER, request.getEndpoints()));
    }

    public boolean checkUserResourceAccess(int userId, String resource) {
        User user = getUser(userId);

        if (user == null) {
            // Normally not should come here. In case some unsafe multi threading issue, which is not implemented.
            throw new NotFoundException(String.format("User %d userId is not found"));
        }

        // Suppose admin is explicitly a user, if need distinguish admin and user, more logic can be added
        // Suppose case sensitive
        if (user.getEndpoints().contains(resource)) {
            return true;
        }

        return false;
    }
}
