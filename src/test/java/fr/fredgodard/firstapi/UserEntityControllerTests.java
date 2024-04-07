package fr.fredgodard.firstapi;

import fr.fredgodard.chatop.service.ChatopUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest
public class ChatopUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatopUserService chatopUserService;

    @Test
    public void testGetUser() {

/*
       try {
           mockMvc.perform(get("/api/login//1")).andExpect(status().isOk());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
 */
    }

    @Test
    public void testCreateUser() {
       /*

       ChatopUserEntity chatopUserEntity = new ChatopUserEntity();
       chatopUserEntity.setName("Godard, Fred");
       chatopUserEntity.setPassword("OC2024");
       chatopUserEntity.setEmail("godard.fred@oc.fr");
       ObjectMapper objectMapper = new ObjectMapper();
       try {
            String json = objectMapper.writeValueAsString(chatopUserEntity);
            MvcResult result = mockMvc.perform(
                    post("/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
       } catch (Exception e) {
            throw new RuntimeException(e);
       }
        */
    }

}
