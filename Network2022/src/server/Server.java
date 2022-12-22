package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server {
	
	public static final String SERVER_FOLDER_PATH = "D:\\upload\\";
	public final String STANDARD = "::";

	public static String FILENAME = "";
	public static long FILESIZE = 0;
	
	public static void main(String[] args) {
		try {
			ServerSocket socket = new ServerSocket(3000);
			
			System.out.println("������ ���۵Ǿ����ϴ�.");
			
			while(true) {
			
				Socket commandSocket = socket.accept();
	
				System.out.println(commandSocket.getInetAddress() +": �����Ͽ����ϴ�.");
				
				Thread serverThread = new Thread(new ServerThread(commandSocket));
	
				serverThread.start();
			}
		} catch (Exception e) {}
	}
}
