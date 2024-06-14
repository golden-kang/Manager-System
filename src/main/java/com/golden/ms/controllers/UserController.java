package com.golden.ms.controllers;

import com.golden.ms.controllers.model.CheckResourceAccessResponse;
import com.golden.ms.exceptions.model.BadRequestException;
import com.golden.ms.model.User;
import com.golden.ms.service.UserManagementService;
import com.golden.ms.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.golden.ms.controllers.model.Constants.ATTRIBUTE_USER;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    protected UserManagementService userManagementService;

    private String SUCCESS_RESPONSE="success";
    private String FAILURE_RESPONSE="failure";
    // since the requirement has ambiguity for resource and endpoint, assuming they are the same
    @GetMapping("/{resource}")
    public ResponseEntity<String> checkResourceAccess(HttpSession session, @PathVariable("resource") String resource){
        User user = (User)session.getAttribute(ATTRIBUTE_USER);

        if (!StringUtils.hasText(resource)){
            throw new BadRequestException("resource is not provided");
        }

        // although the user is already get, but do it once more because it is a business logic
        if (userManagementService.checkUserResourceAccess(user.getUserId(), resource)) {
            return ResponseEntity.status(HttpStatus.OK).body(CodeUtils.serializeSuccessResponse(HttpStatus.OK, new CheckResourceAccessResponse(SUCCESS_RESPONSE)));
        }

        return ResponseEntity.status(HttpStatus.OK).body(CodeUtils.serializeSuccessResponse(HttpStatus.OK, new CheckResourceAccessResponse(FAILURE_RESPONSE)));
    }
}
