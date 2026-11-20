
package com.kishan.reporadar.controller;

import com.kishan.reporadar.entity.RepositoryEntity;
import com.kishan.reporadar.service.RepositoryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GitHubRepositoryController.class)
class GitHubRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepositoryService repositoryService;

    @Test
    void searchRepositories_shouldReturnRepositories() throws Exception {

        RepositoryEntity entity = new RepositoryEntity();
        entity.setGithubId(123L);
        entity.setName("spring-boot");
        entity.setLanguage("Java");
        entity.setStars(50000);

        when(repositoryService.searchAndSaveRepositories(any()))
                .thenReturn(List.of(entity));

        String request = """
                {
                    "query": "spring boot",
                    "language": "Java",
                    "sort": "stars"
                }
                """;

        mockMvc.perform(post("/api/github/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("spring-boot"))
                .andExpect(jsonPath("$[0].language").value("Java"))
                .andExpect(jsonPath("$[0].stars").value(50000));

        verify(repositoryService).searchAndSaveRepositories(any());
    }

    @Test
    void getRepositories_shouldReturnStoredRepositories() throws Exception {

        RepositoryEntity entity = new RepositoryEntity();
        entity.setGithubId(123L);
        entity.setName("spring-boot");
        entity.setLanguage("Java");
        entity.setStars(50000);

        when(repositoryService.getStoredRepositories("Java", 10000, "stars"))
                .thenReturn(List.of(entity));

        mockMvc.perform(get("/api/github/repositories")
                        .param("language", "Java")
                        .param("minStars", "10000")
                        .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("spring-boot"))
                .andExpect(jsonPath("$[0].language").value("Java"))
                .andExpect(jsonPath("$[0].stars").value(50000));

        verify(repositoryService)
                .getStoredRepositories("Java", 10000, "stars");
    }

    @Test
    void searchRepositories_shouldReturnBadRequestForInvalidRequest()
            throws Exception {

        String invalidRequest = """
                {
                    "query": "",
                    "language": "Java",
                    "sort": "stars"
                }
                """;

        mockMvc.perform(post("/api/github/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
