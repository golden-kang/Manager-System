package com.golden.ms.filters;

import com.golden.ms.filters.model.UserRole;
import com.golden.ms.model.User;
import com.golden.ms.service.UserManagementService;
import com.golden.ms.utils.CodeUtils;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.golden.ms.controllers.model.Constants.ATTRIBUTE_USER;
import static com.golden.ms.controllers.model.Constants.HEADER_AUTH;

@Configuration
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserManagementService userManagementService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequestWrapper headRequestWrapper = new HttpServletRequestWrapper(request);
        String authHeader=headRequestWrapper.getHeader(HEADER_AUTH);

        // Role Info is not given return 401
        if (!StringUtils.hasText(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(CodeUtils.serializeErrorResponse(HttpStatus.UNAUTHORIZED, String.format("User is unauthorized")));
            return;
        }

        UserRole userRole = null;
        try {
            userRole= CodeUtils.deserializeUserRole(CodeUtils.decodeBase64(authHeader));
        } catch (JsonSyntaxException e) {
            // here use bad request for invalid auth head, however for security consideration, UNAUTHORIZED may be better
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(CodeUtils.serializeErrorResponse(HttpStatus.BAD_REQUEST, "Auth Header Malformed"));
            return;
        }

        User user = userManagementService.getUser(userRole.getUserId());
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(CodeUtils.serializeErrorResponse(HttpStatus.UNAUTHORIZED, String.format("User is unauthorized")));
            return;
        }

        request.getSession().setAttribute(ATTRIBUTE_USER, user);

        filterChain.doFilter(request, response);
    }
}