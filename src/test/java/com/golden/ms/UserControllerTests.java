package com.golden.ms;

import com.golden.ms.controllers.model.AddUserRequest;
import com.golden.ms.controllers.model.CheckResourceAccessResponse;
import com.golden.ms.exceptions.model.ErrorResponse;
import com.golden.ms.model.ResponseBodyAddUser;
import com.golden.ms.model.ResponseBodyCheckResourceAccessResponse;
import com.golden.ms.model.User;
import com.golden.ms.runtime.DatabaseMock;
import com.golden.ms.utils.TestCodeUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static com.golden.ms.controllers.model.Constants.HEADER_AUTH;
import static com.golden.ms.model.Constants.ROLE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class UserControllerTests {
    @Autowired
    private DatabaseMock databaseMock;

    @Autowired
    private MockMvc mockMvc;

	private static String DEFAULT_HEADER="eyJ1c2VySWQiOjEyMzQ1NiwgImFjY291bnROYW1lIjoidGVzdCIsICJyb2xlIjoiYWRtaW4ifQ==";

    @BeforeEach
    void clean(){
        DatabaseMock.instance.clear();
    }

    @Test
    void testCheckResourceAccessWithoutHeader() throws Exception {
        mockMvc
                .perform(get("/user/resourceA"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCheckResourceAccess() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        get("/user/ResourceA")
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseBodyCheckResourceAccessResponse response= TestCodeUtils.deserializeCheckResourceAccessResponse(content);

        Assert.assertEquals("success", response.getBody().getResult());
    }

    @Test
    void testCheckResourceAccessWithoutPermission() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        get("/user/ResourceB")
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseBodyCheckResourceAccessResponse response= TestCodeUtils.deserializeCheckResourceAccessResponse(content);

        Assert.assertEquals("failure", response.getBody().getResult());
    }

    @Test
    void testCheckResourceAccessWithoutPermissionWithSpace() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("Resource A")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        get("/user/{resouce}", "Resource A")
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseBodyCheckResourceAccessResponse response= TestCodeUtils.deserializeCheckResourceAccessResponse(content);

        Assert.assertEquals("success", response.getBody().getResult());
    }

    @Test
    void testCheckResourceAccessWithoutResource() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        get("/user/{resouce}", " ")
                                .headers(httpHeaders)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
