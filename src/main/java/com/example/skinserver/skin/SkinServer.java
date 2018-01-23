//package com.example.skinserver.skin;
//
//import java.io.IOException;
//import java.util.Map;
//
//public class SkinServer extends WifiNanoHTTPD {
//    public SkinServer(int port) {
//        super(port);
//    }
//
//    @Override
//    public Response serve(IHTTPSession session) {
//        String msg = "<html><body><h1>Hello server</h1>\n";
//        Map<String, String> parms = session.getParms();
//        if (parms.get("username") == null) {
//            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//        } else {
//            msg += "<p>Hello, " + parms.get("username") + "!</p>";
//        }
//        return newFixedLengthResponse(msg + "</body></html>\n");
//    }
//
//    public static void main(String[] args) {
//        SkinServer server = new SkinServer(9876);
//        try {
//            server.start();
//            System.out.println("\nRunning! Point your browsers to http://localhost:9876/ \n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
