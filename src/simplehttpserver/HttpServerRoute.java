/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver;

import simplehttpserver.httpresponse.HttpResponse;
import java.util.function.Function;

/**
 *
 * @author david
 */
public class HttpServerRoute {
    private String path;
    private HttpRequestMethod method;
    private Function<HttpRequest, HttpResponse> handler;

    public HttpServerRoute(String path, HttpRequestMethod method, Function<HttpRequest, HttpResponse> handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public Function<HttpRequest, HttpResponse> getHandler() {
        return handler;
    }
}
