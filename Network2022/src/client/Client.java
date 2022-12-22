package client;

import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
    public static Boolean LOGIN_STATUS = false;
    public static Boolean LOAD_STATUS = false;
    public static Boolean DUP_LOAD_STATUS = true;
    public static Boolean DUP_STATUS;
    public static Boolean FILE_STATUS;
    public static final String FILE_PATH = "D:\\download\\";
    public static final String STANDARD = "::";

    public static void main(String[] args) {
    	
        String nickName;
        Scanner scanner = new Scanner(System.in);
        System.out.print("�г��� : ");
        nickName = scanner.nextLine();

        try {
            Socket socket = new Socket("127.0.0.1", 3000);
            System.out.println("** ������ �����Ͽ����ϴ�. **");

            Thread send = new Thread(new ClientSend(socket, nickName));
            Thread receive = new Thread(new ClientReceive(socket));

            send.start();
            receive.start();
            
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {}
    }
}
