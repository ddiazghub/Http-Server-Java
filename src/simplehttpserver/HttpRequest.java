/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author david
 */
public class HttpRequest {
    private final HttpRequestMethod method;
    private final URL url;
    private final String route;
    private final InetAddress ip;
    private final String protocol;
    private final String version;
    private final Socket socket;
    private final String host;
    private final String body;
    private final HashMap<String, String> headers;
    
    public HttpRequest(String rawContent, Socket socket) throws MalformedURLException, IndexOutOfBoundsException {
        this.socket = socket;
        this.ip = socket.getInetAddress();
        String[] lines = rawContent.split("\r\n");
        String[] first = lines[0].split(" ");
        this.method = HttpRequestMethod.valueOf(first[0]);
        this.route = first[1];
        this.version = first[2];
        this.protocol = this.version.matches("HTTPS") ? "https" : "http";
        this.host = lines[1].split(" ")[1];
        this.url = new URL(this.protocol + "://" + this.host + this.route);
        this.headers = new HashMap<>();
        
        int i = 2;
        while (i < lines.length && !lines[i].isEmpty()) {
            String[] header = lines[i].split(" ");
            this.headers.put(header[0].replace(":", ""), header[1]);
            i++;
        }
        
        StringBuilder body = new StringBuilder();
        
        while (i + 1 < lines.length) {
            body.append(lines[i]);
            i++;
        }
        
        this.body = body.toString();
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public URL getUrl() {
        return url;
    }

    public InetAddress getIp() {
        return ip;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getBody() {
        return body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getRoute() {
        return route;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }
    
    @Override
    public String toString() {
        StringBuilder headers = new StringBuilder();
        
        for (String key : this.headers.keySet())
            headers.append(key).append(": ").append(this.headers.get(key)).append("\r\n");
        
        return this.method.getValue() + " " + this.route + " " + this.version + "\r\n" +
            "Host: " + this.host + "\r\n" +
            headers.toString() + "\r\n" +
            this.body;
    }
}
