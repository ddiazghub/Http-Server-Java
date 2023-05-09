/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simplehttpserver;

import java.io.File;
import simplehttpserver.httpresponse.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import simplehttpserver.exceptions.HttpException;
import simplehttpserver.exceptions.NotFoundException;
import simplehttpserver.httpresponse.FileResponse;
import simplehttpserver.httpresponse.TextResponse;

/**
 *
 * @author david
 */
public class SimpleHttpServer {
    private ServerSocket socket;
    private HashMap<String, HttpServerRoute> routes;
    private ArrayList<Function<HttpRequest, HttpResponse>> middlewares;
    private AtomicReference<String> staticFolder;
    private int port;
    
    public SimpleHttpServer(int port) {
        this.port = port;
        this.routes = new HashMap<>();
        this.staticFolder = new AtomicReference<>("");
        this.middlewares = new ArrayList<>();
        this.middlewares.add(request -> SimpleHttpServer.staticMiddleware(request, this.staticFolder.get()));
    }
    
    public SimpleHttpServer() {
        this(8080);
    }
    
    public void start() {
        try (ServerSocket s = new ServerSocket(this.port)) {
            System.out.println("Debug: Server started on port " + port);
            this.socket = s;
            
            while (true) {
                Socket client = this.socket.accept();
                this.addClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void route(String path, HttpRequestMethod method, Function<HttpRequest, HttpResponse> handler) {
        this.routes.put(method + " " + path, new HttpServerRoute(path, method, handler));
    }
    
    public void get(String path, Function<HttpRequest, HttpResponse> handler) {
        this.route(path, HttpRequestMethod.GET, handler);
    }
    
    public void middleware(Function<HttpRequest, HttpResponse> handler) {
        this.middlewares.add(handler);
    }
    
    public void serveStatic(String path) {
        this.staticFolder.set(path);
    }
    
    public HttpResponse callMiddlewares(HttpRequest request) {
        for (Function<HttpRequest, HttpResponse> middleware : this.middlewares) {
            HttpResponse response = middleware.apply(request);
            
            if (response != null)
                return response;
        }
        
        return null;
    }
    
    public Function<HttpRequest, HttpResponse> getRequestHandler(HttpRequest request) {
        String route = request.getMethod() + " " + request.getRoute();
        
        if (!this.routes.containsKey(route))
            return null;
        
        return this.routes.get(request.getMethod() + " " + request.getRoute()).getHandler();
    }
    
    private void addClient(Socket client) {
        HttpClientThread clientThread = new HttpClientThread(this, client);
        clientThread.start();
    }
    
    public static HttpResponse staticMiddleware(HttpRequest request, String staticFolder) {
        File file = new File(staticFolder, request.getRoute());
        
        if (file.exists() && file.isFile())
            return new FileResponse(file.getPath(), 200);
        
        return null;
    }
    
    public static void defaultFallbackRoute(HttpRequest request) {
        throw new NotFoundException("Could not get handler for route " + request.getMethod() + " " + request.getRoute());
    }
    
    public static HttpResponse defaultErrorHandler(HttpException error) {
        System.out.println(error.getStatus() + " " + error.getMessage());
        error.printStackTrace();
        
        String response = "<div style=\"padding:25px;border:5px solid red;border-radius:20px;background:rgba(255,0,0,0.3)\">\n" +
            "  <h1 style=\"color:red\">Something Went Wrong :(</h1>\n" +
            "  <p style=\"color:red\">Status " + error.getStatus() + ". " + error.getMessage() + "</p>\n" +
            "</div>";
        
        return new TextResponse(response, error.getStatus());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleHttpServer server = new SimpleHttpServer(3001);
        
        server.serveStatic("src/public");
        
        server.get("/", request -> {
            System.out.println("Hello index");
            
            return new FileResponse("src/public/index.html", 200);
        });
        
        server.get("/route2", request -> {
            System.out.println("Hello route2");
            
            return new TextResponse("<b>Hello Route2</b>", 200);
        });
        
        server.get("/sus", request -> {
            System.out.println("Hello sus");
            
            return new TextResponse("<b>Hello sus</b>", 200);
        });
        
        server.get("/google", request -> {
            return new FileResponse("src/public/Google.html", 200);
        });
        
        server.start();
    }
}
