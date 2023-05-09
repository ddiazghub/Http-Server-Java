/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver.httpresponse;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author david
 */
public class TextResponse<T> extends HttpResponse<T> {
    public TextResponse(T body, int status) {
        super(body, status);
        
        this.setHeader("Content-Type", "text/html; charset=utf-8");
    }
    
    public TextResponse(int status) {
        super(status);
    }
    
    public byte[] raw() {
        if (this.body == null)
            return this.serialize(null);
        
        return this.serialize(this.body.toString().getBytes(StandardCharsets.UTF_8));
    }
}
