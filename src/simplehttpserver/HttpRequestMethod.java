/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package simplehttpserver;

/**
 *
 * @author david
 */
public enum HttpRequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH");

    private final String value;
    
    public String getValue() {
        return value;
    }
    private HttpRequestMethod(String value) {
        this.value = value;
    }
}
