package com.golden.ms;

import com.golden.ms.controllers.model.AddUserRequest;
import com.golden.ms.controllers.model.UpdateUserRequest;
import com.golden.ms.exceptions.model.ErrorResponse;
import com.golden.ms.model.ResponseBodyAddUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class AdminControllerTests {
    @Autowired
    private DatabaseMock databaseMock;

    @Autowired
    private MockMvc mockMvc;

	private static String DEFAULT_HEADER="eyJ1c2VySWQiOjEyMzQ1NiwgImFjY291bnROYW1lIjoidGVzdCIsICJyb2xlIjoiYWRtaW4ifQ==";

    @BeforeEach
    void clean(){
        DatabaseMock.instance.clear();
    }

    // Test For Add
    @Test
    void testAddUserWithoutHeader() throws Exception {
        mockMvc
                .perform(post("/admin/addUser", new AddUserRequest(1, "test", Arrays.asList("resourceA"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAddUser() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "test", Arrays.asList("resourceA"))))

                )
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseBodyAddUser response = TestCodeUtils.deserializeAddUserResponse(content);

        Assert.assertEquals(1, response.getBody().getUserId());
        Assert.assertEquals("test", response.getBody().getAccountName());
        Assert.assertEquals(ROLE_USER, response.getBody().getRole());
        Assert.assertEquals(1, response.getBody().getEndpoints().size());
        Assert.assertEquals("resourceA", response.getBody().getEndpoints().get(0));
    }

    @Test
    void testAddUserWithoutUserId() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(0, "test", Arrays.asList("resourceA"))))

                )
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("userId is missing", response.getMessage());
    }

    @Test
    void testAddUserWithoutAccountName() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "", Arrays.asList("resourceA"))))

                )
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("accountName is missing", response.getMessage());
    }

    @Test
    void testAddUserWithoutEndpoints() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "test", null)))

                )
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("endPoints is missing", response.getMessage());
    }

    @Test
    void testAddUserWithInvalidUser() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("resourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "", Arrays.asList("resourceA"))))

                )
                .andExpect(status().isForbidden())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
        Assert.assertEquals("User test does not have permission to perform add user", response.getMessage());
    }

    @Test
    void testAddUserWithAUserNotExist() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("resourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjIsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ==");

        MvcResult result = mockMvc
                .perform(
                        post("/admin/addUser")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "", Arrays.asList("resourceA"))))

                )
                .andExpect(status().isUnauthorized())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        Assert.assertEquals("User is unauthorized", response.getMessage());
    }


    // Test For Update
    @Test
    void testUpdateUserWithoutHeader() throws Exception {
        mockMvc
                .perform(put("/admin/updateUser/1", new UpdateUserRequest("test", Arrays.asList("resourceA"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUser() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        put("/admin/updateUser/1")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "test", Arrays.asList("resource B"))))

                )
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseBodyAddUser response = TestCodeUtils.deserializeAddUserResponse(content);

        Assert.assertEquals(1, response.getBody().getUserId());
        Assert.assertEquals("test", response.getBody().getAccountName());
        Assert.assertEquals(ROLE_USER, response.getBody().getRole());
        Assert.assertEquals(1, response.getBody().getEndpoints().size());
        Assert.assertEquals("resource B", response.getBody().getEndpoints().get(0));
    }

    @Test
    void testUpdateUserWithAUserNotFound() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        put("/admin/updateUser/1")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new UpdateUserRequest("test", Arrays.asList("resource B"))))

                )
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        Assert.assertEquals("User with user id 1 is not found", response.getMessage());
    }

    @Test
    void testUpdateUserWithoutAccountName() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        put("/admin/updateUser/1")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new UpdateUserRequest( "", Arrays.asList("resource B"))))

                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("accountName is missing", response.getMessage());
    }

    @Test
    void testUpdateUserWithoutEndpoints() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, DEFAULT_HEADER);

        MvcResult result = mockMvc
                .perform(
                        put("/admin/updateUser/1")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new UpdateUserRequest( "test", null)))

                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        Assert.assertEquals("endpoints is missing", response.getMessage());
    }

    @Test
    void testUpdateUserWithInvalidUser() throws Exception {
        databaseMock.insert(new User(1, "test", ROLE_USER, Arrays.asList("ResourceA")));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_AUTH, "eyJ1c2VySWQiOjEsICJhY2NvdW50TmFtZSI6InRlc3QiLCAicm9sZSI6InVzZXIifQ");

        MvcResult result = mockMvc
                .perform(
                        put("/admin/updateUser/1")
                                .headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)
                                .content(TestCodeUtils.serializeObject(new AddUserRequest(1, "test", Arrays.asList("resource B"))))

                )
                .andExpect(status().isForbidden())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ErrorResponse response= TestCodeUtils.deserializeErrorResponse(content);

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
        Assert.assertEquals("User test does not have permission to perform update user", response.getMessage());
    }

}
