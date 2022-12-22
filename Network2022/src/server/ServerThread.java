package server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Scanner;

import client.Client;

class ServerThread extends Thread {
    Socket socket;
    DataOutputStream out;
    DataInputStream in;
    FileInputStream fileIn;
    String name;
    Scanner scanner;
    FileInputStream fis;
    File file;

    ServerThread(Socket socket) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {}
    }

    @Override
    public void run() {
    	
    	while(true) {
	    	try {
	    		String[] command = in.readUTF().split("::");
	    		switch (command[0]) {
	    	      case "[AUTH]": {
	    	      	auth(command[1], command[2]);
	    	      	break;
	    	      }
	    	      case "[LIST]": {
	    	      	list();
	    	          break;
	    	  	  }
	    	      case "[UPLOAD]": {
	    	      	
	    	      	File dir = new File(Server.SERVER_FOLDER_PATH);
	    	      	
	    	      	File[] listOfSubfiles = dir.listFiles();
	    	  		
	    	      	int check = 0;
	    	      	
	    	  		for (File file : listOfSubfiles) {
	    	  			
	    	  			if (command[2].equals(file.getName())) {
	    	  				out.writeUTF("[DUPLICATE]" + client.Client.STANDARD);
	    	  				check = 1;
	    					}
	    	  		}
	    	  		
	    	  		if (check == 0) {
	    	  			out.writeUTF("[SUCCESS]" + client.Client.STANDARD);
	    				}
	    	      	
	    	  		Server.FILENAME = command[2];
	    	  		
	    	  		Server.FILESIZE = Long.parseLong(command[1]);
	    	  		
	    	          break;
	    	      }
	    	      case "[SEND_FILE]": {
	    	      	 FileOutputStream fos = new FileOutputStream(Server.SERVER_FOLDER_PATH + "/" + Server.FILENAME);
	    	           int readBit = 0;
	    	           for (long i = 0; i < Server.FILESIZE; i++) {
	    	               readBit = in.read();
	    	               fos.write(readBit);
	    	           }  			
	    	          break;
	    	      }
	    	      case "[CANCEL_SEND_FILE]": {
	    	      }
	    	      case "[DOWNLOAD]": {
	    	      	
	    	      	FileInputStream fis = new FileInputStream(Server.SERVER_FOLDER_PATH + command[1]);
	    	      	
	    	      	File dir = new File(Server.SERVER_FOLDER_PATH);
	    	      	
	    	      	File[] listOfSubfiles = dir.listFiles();
	    	  		
	    	      	long size = 0;
	    	      	
	    	      	String name = "";
	    	      	
	    	  		for (File file : listOfSubfiles) {
	    	  			
	    	  			if (command[1].equals(file.getName())) {
	    						size = file.length();
	    						name = file.getName();
	    					}
	    	  		}
	    	  		
	    	      	out.writeUTF("[SEND_FILE]" + client.Client.STANDARD + Long.toString(size) + client.Client.STANDARD + name);
	    	          byte[] bytes = new byte[1024];
	    	          int readBit = 0;
	    	          while((readBit = fis.read(bytes)) != -1) {
	    	              // bytes�� ����� ������ ����
	    	          	out.write(bytes, 0, readBit);
	    	          }
	    	      }
	    	      default: {
	    	          System.out.println(command[0]);
	    	          break;
	    	      }
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    }

    
    public void auth(String id, String pw) throws Exception {
		if (id.equals("admin") && pw.equals("1234")) {
			System.out.println("����");
			out.writeUTF("[AUTH]" + client.Client.STANDARD + "success");
		}else {
			System.out.println("����");
			out.writeUTF("[AUTH]" + client.Client.STANDARD + "fail");
		}
	}
    
    public void list() throws Exception {
		File dir = new File(Server.SERVER_FOLDER_PATH);
		if(!dir.exists()) {
			out.writeUTF("[NOTLIST]" + client.Client.STANDARD);
		}
    	File[] listOfSubfiles = dir.listFiles();
		for (File file : listOfSubfiles) {
			out.writeUTF("[LIST]" + client.Client.STANDARD + file.getName() + client.Client.STANDARD + file.length());
		}
	}
}

