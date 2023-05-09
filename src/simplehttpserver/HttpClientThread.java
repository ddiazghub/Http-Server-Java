/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplehttpserver;

import simplehttpserver.httpresponse.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Function;
import simplehttpserver.exceptions.HttpException;
import simplehttpserver.exceptions.NotFoundException;

/**
 *
 * @author david
 */
public class HttpClientThread extends Thread {
    private final Socket client;
    private final SimpleHttpServer server;
    
    public HttpClientThread(SimpleHttpServer server, Socket client) {
        super();
        
        System.out.println("Debug: New client " + client);
        this.client = client;
        this.server = server;
    }
    
    @Override
    public void run() {
        try {
            HttpResponse response;
            
            try {
                HttpRequest request = this.parseRequest();
                System.out.println("Debug: Request from " + client);
                System.out.println(request);

                response = this.server.callMiddlewares(request);
                
                if (response == null) {
                    Function<HttpRequest, HttpResponse> handler = this.server.getRequestHandler(request);

                    if (handler == null)
                        SimpleHttpServer.defaultFallbackRoute(request);

                    response = handler.apply(request);
                }
            } catch (HttpException e) {
                response = SimpleHttpServer.defaultErrorHandler(e);
            }
            
            System.out.println("Debug: Response for " + client);
            System.out.println(response);
            
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(response.raw());
            clientOutput.flush();
            client.close();
            System.out.println("Debug: Terminated client " + client);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private HttpRequest parseRequest() throws IOException {
        InputStream stream = this.client.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        StringBuilder request = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null && !line.isEmpty())
            request.append(line).append("\r\n");
    
        return new HttpRequest(request.toString(), this.client);
    }

    public Socket getClient() {
        return client;
    }
    
    private byte[] serializeResponse() {
        String response = "HTTP/1.1 200\r\n" +
            "ContentType: text/html\r\n" +
            "\r\n<b>It works!</b>\r\n" + 
            "\r\n";
        
        System.out.println(response);
        
        return response.getBytes();
    }
}
