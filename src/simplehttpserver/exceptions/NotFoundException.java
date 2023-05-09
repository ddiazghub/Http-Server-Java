/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver.exceptions;

/**
 *
 * @author david
 */
public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message, 404);
    }
    
    public NotFoundException(Throwable cause) {
        super(cause, 404);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause, 404);
    }
}
