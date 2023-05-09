/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver.exceptions;

/**
 *
 * @author david
 */
public class HttpException extends RuntimeException {
    private int status;
    
    public HttpException(String message, int status) {
        super(message);
        this.status = status;
    }
    
    public HttpException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }
    
    public HttpException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
