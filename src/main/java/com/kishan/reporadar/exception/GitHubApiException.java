
package com.kishan.reporadar.exception;

public class GitHubApiException extends RuntimeException {

    private final int statusCode;

    public GitHubApiException(String message, Throwable cause) {
        this(message, 502, cause);
    }

    public GitHubApiException(String message) {
        this(message, 502, null);
    }

    public GitHubApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
