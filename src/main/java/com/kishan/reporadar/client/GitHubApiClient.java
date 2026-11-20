
package com.kishan.reporadar.client;

import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.dto.response.GitHubApiResponse;
import com.kishan.reporadar.exception.GitHubApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class GitHubApiClient {

    private final RestClient githubRestClient;

    public GitHubApiClient(RestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    public GitHubApiResponse searchRepositories(SearchRequest request) {

        String query = request.getQuery().trim();

        if (request.getLanguage() != null
                && !request.getLanguage().isBlank()) {
            query += " language:" + request.getLanguage().trim();
        }

        final String searchQuery = query;

        String sort = request.getSort();

        if (sort == null || sort.isBlank()) {
            sort = "stars";
        }

        final String sortOrder = sort;

        try {
            GitHubApiResponse response = githubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", searchQuery)
                            .queryParam("sort", sortOrder)
                            .queryParam("order", "desc")
                            .queryParam("per_page", 30)
                            .build())
                    .retrieve()
                    .body(GitHubApiResponse.class);

            if (response == null) {
                throw new GitHubApiException(
                        "GitHub returned an empty response."
                );
            }

            return response;

        } catch (RestClientResponseException ex) {

            String responseBody = ex.getResponseBodyAsString();

            String rateLimitRemaining =
                    ex.getResponseHeaders() != null
                            ? ex.getResponseHeaders()
                            .getFirst("X-RateLimit-Remaining")
                            : null;

            boolean rateLimitExceeded =
                    ex.getStatusCode().value() == 403
                            && ("0".equals(rateLimitRemaining)
                            || responseBody.toLowerCase()
                            .contains("rate limit"));

            if (rateLimitExceeded) {
                throw new GitHubApiException(
                        "GitHub API rate limit exceeded. Please try again later.",
                        503,
                        ex
                );
            }

            throw new GitHubApiException(
                    "GitHub API request failed. Please try again later.",
                    502,
                    ex
            );
        }
    }
}
