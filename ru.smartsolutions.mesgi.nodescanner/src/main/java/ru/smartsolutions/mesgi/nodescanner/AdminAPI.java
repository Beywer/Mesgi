package ru.smartsolutions.mesgi.nodescanner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class AdminAPI {
    public static final int TIMEOUT = 5000;
    public static final int DGRAM_LENGTH = 4096;

    private InetAddress address;
    private int port;
//    private byte[] password;
    
    public AdminAPI(InetAddress address, int port, byte[] password)
    {
        this.address = address;
        this.port = port;
//        this.password = password;
    }

    public String getBind()
    {
        return this.address.getHostAddress() + ":" + this.port;
    }

    public String ping() throws IOException
    {
            HashMap<String, Object> request = new HashMap<String, Object>();
            request.put("q", "ping");
            
            Map<String, Object> response = perform(request);
            
    		return (String)response.get("q");
    }

    public Map<String,Object> perform(Map<String,Object> request)
    {
        DatagramSocket socket = null;
		try {
			socket = newSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
        byte[] data = serialize(request);
//		System.out.println("Bencoded request " + new String(data));
//		System.out.println("\n\nJSON request " + new JSONObject(request).toString(4));
        DatagramPacket dgram = new DatagramPacket(data, data.length, this.address, this.port);
        try {
			socket.send(dgram);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        DatagramPacket responseDgram = new DatagramPacket(new byte[DGRAM_LENGTH],DGRAM_LENGTH);
        try{
        	socket.receive(responseDgram);
        }
        catch (SocketTimeoutException e){
    		System.out.println("Timeout");
        }
        catch(Exception e){
    		System.out.println("Something else");
        }

        socket.close();

        Map<String,Object> response = null;
    	response = parse(responseDgram.getData());

//		System.out.print("Распарсен ответ : ");
//		System.out.println("Bencode response : " + new String(responseDgram.getData()));
//		System.out.println("\n\n");
//		System.out.println("\n\nJSON response " + new JSONObject(response).toString(4));
        return response;
    }

    public void sendRequest (Map<String,Object> request){

        DatagramSocket socket = null;
		try {
			socket = newSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

        byte[] data = serialize(request);
//		System.out.println("Bencoded request " + new String(data));
//		System.out.println("\n\nJSON request " + new JSONObject(request).toString(4));
        DatagramPacket dgram = new DatagramPacket(data, data.length, this.address, this.port);
        try {
			socket.send(dgram);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    protected byte[] serialize(Map<String,Object> request)
    {
    	BencodeEncoder encoder = new BencodeEncoder();
    	String bencode = encoder.encodeMap(request);
    	return bencode.getBytes();
    }

    protected Map<String,Object> parse(byte[] data)
    {
    	BencodeDecoder decoder = new BencodeDecoder(new String(data));
    	Map<String,Object> response = decoder.readMap();
    	return response;
    }
    protected Map<String,Object> parse(String data) throws IOException
    {
    	BencodeDecoder decoder = new BencodeDecoder(data);
    	Map<String,Object> response = decoder.readMap();
    	return response;
    }

    protected DatagramSocket newSocket() throws SocketException
    {
        DatagramSocket socket = new DatagramSocket();
//        socket.setSoTimeout(TIMEOUT);
        return socket;
    }
}