/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver.httpresponse;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author david
 */
public abstract class HttpResponse<T> {
    protected final int status;
    protected final T body;
    protected final HashMap<String, String> headers;
    
    public HttpResponse(T body, int status) {
        this.status = status;
        this.body = body;
        this.headers = new HashMap();
    }
    
    public HttpResponse(int status) {
        this(null, status);
    }

    public void setHeader(String header, String value) {
        this.headers.put(header, value);
    }
    
    public abstract byte[] raw();
    
    protected byte[] serialize(byte[] body) {
        if (body == null || body.length == 0)
            return this.buildString().toString().getBytes(StandardCharsets.UTF_8);
        
        this.setHeader("Content-Length", Integer.toString(body.length + 2));
        byte[] headerBytes = this.buildString().append("\r\n\r\n").toString().getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(headerBytes.length + body.length + 4);
        
        return buffer.put(headerBytes).put(body).put("\r\n\r\n".getBytes()).array();
    }
    
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }
    
    private StringBuilder buildString() {
        StringBuilder response = new StringBuilder("HTTP/1.1 " + this.status + "\r\n");
        
        for (Entry<String, String> entry : this.headers.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        
        return response;
    }
    
    @Override
    public String toString() {
        StringBuilder response = this.buildString();
        
        if (this.body != null)
            response.append("\r\n\r\n").append(body.toString()).append("\r\n\r\n");
        
        return response.toString();
    }
}
