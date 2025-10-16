package com.bionic.test.user_management;

import com.bionic.test.user_management.dto.UserProfileDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class ProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "test@example.com", roles = {"CUSTOMER"})
    public void testGetMyProfile() throws Exception {
        mockMvc.perform(get("/profiles/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"CUSTOMER"})
    public void testUpdateMyProfile() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setFullName("Updated Name");
        profileDTO.setAddress("Updated Address");
        profileDTO.setPhone("Updated Phone");

        mockMvc.perform(put("/profiles/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"CUSTOMER"})
    public void testDeleteMyProfile() throws Exception {
        mockMvc.perform(delete("/profiles/me"))
                .andExpect(status().isNoContent());
    }
}
