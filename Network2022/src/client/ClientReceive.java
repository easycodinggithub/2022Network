package client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

class ClientReceive extends Thread {
    Socket socket;
    DataInputStream in;

    ClientReceive(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        while (in != null) {
            try {
                String[] command = in.readUTF().split(Client.STANDARD);
                switch (command[0]) {
                    case "[AUTH]": {
                        authReceive(command[1]);
                        break;
                    }
                    case "[LIST]": {
                        listReceive(command[1], command[2]);
                        break;
                    }
                    case "[NOTLIST]": {
                        System.out.println("������ ������ �������� �ʽ��ϴ�.");
                        break;
                    }
                    case "[DUPLICATE]": {
                        findDuplicateFile();
                        break;
                    }
                    case "[SUCCESS]": {
                        notDuplicateFile();
                        break;
                    }
                    case "[SEND_FILE]": {
                        downloadFile(command[1], command[2]);
                        break;
                    }
                    case "[DOWN_FAIL]": {
                    	Client.FILE_STATUS = false;
                        break;
                    }
                    default: {
                        System.out.println(command[0]);
                        break;
                    }
                }
            } catch (IOException e) { }
        }
    } // run

    private void authReceive(String authResult) {
        if (Objects.equals(authResult, "success")) {
        	System.out.println("���� ����");
        	Client.LOGIN_STATUS = true;
        } else {
            System.out.println("���� ����");
            Client.LOGIN_STATUS = false;
        }
        Client.LOAD_STATUS = false;
    }

    private void findDuplicateFile() {
    	Client.DUP_STATUS = true;
    	Client.DUP_LOAD_STATUS = false;
    }

    private void notDuplicateFile() {
    	Client.DUP_STATUS = false;
    	Client.DUP_LOAD_STATUS = false;
    }

    private void listReceive(String fileName, String fileSize) {
        System.out.println("���ϸ� : " + fileName + "   ������ : " + fileSize);
    }

    private void downloadFile(String fileSize, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(Client.FILE_PATH + "/" + fileName);
        int readBit = 0;
        for (long i = 0; i < Long.parseLong(fileSize); i++) {
            readBit = in.read();
            fos.write(readBit);
        }
        Client.FILE_STATUS = true;
    }
}






































//package kr.hs.dgsw.network.test01.n2317.client;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.Objects;
//
//class ClientReceive {
//    Socket commandSocket, downloadSocket, uploadSocket;
//    
//    //String nickName;
//    
//    DataInputStream commandIn;
//
//    ClientReceive(Socket commandSocket, Socket downloadSocket, Socket uploadSocket) {
//        this.commandSocket = commandSocket;
//        this.downloadSocket = downloadSocket;
//        this.uploadSocket = uploadSocket;
//        //this.nickName = nickName;
//        
//        try {
//			commandIn = new DataInputStream(commandSocket.getInputStream());
//		} catch (IOException e) {}
//    }
//    
//    public String receiveLogin(){
//    	try {
//			return commandIn.readUTF();
//		} catch (IOException e) {}
//	}  
//}
//    public static void main(String[] args) {
//    	while (in != null) {
//            try {
//                String[] command = in.readUTF().split(Client.STANDARD);
//                switch (command[0]) {
//                    case "[AUTH]": {
//                        authReceive(command[1]);
//                        break;
//                    }
//                    case "[LIST]": {
//                        listReceive(command[1], command[2]);
//                        break;
//                    }
//                    case "[DUPLICATE]": {
//                        findDuplicateFile();
//                        break;
//                    }
//                    case "[SUCCESS]": {
//                        notDuplicateFile();
//                        break;
//                    }
//                    case "[SEND_FILE]": {
//                        downloadFile(command[1], command[2]);
//                        break;
//                    }
//                    case "[DOWN_FAIL]": {
//                        Client.IS_SUCCESS_FILE_DOWNLOAD = false;
//                        break;
//                    }
//                    default: {
//                        System.out.println(command[0]);
//                        break;
//                    }
//                }
//            } catch (IOException e) { }
//        }
//	}
//
//    private void authReceive(String authResult) {
//        if (Objects.equals(authResult, "success")) {
//            Client.IS_LOGIN = true;
//        } else {
//            System.out.println("������ �����Ͽ����ϴ�. �ٽ� �Է����ּ���!");
//            Client.IS_LOGIN = false;
//        }
//        Client.IS_LOADING = false;
//    }
//
//    private void findDuplicateFile() {
//        Client.IS_DUPLICATE = true;
//        Client.IS_DUPLICATE_WAIT = false;
//    }
//
//    private void notDuplicateFile() {
//        Client.IS_DUPLICATE = false;
//        Client.IS_DUPLICATE_WAIT = false;
//    }
//
//    private void listReceive(String fileName, String fileSize) {
//        System.out.printf("���ϸ� : %0s ���� ������ : %10s \n", fileName, fileSize);
//    }
//
//    private void downloadFile(String fileSize, String fileName) throws IOException {
//        FileOutputStream fos = new FileOutputStream(Client.CLIENT_FOLDER_PATH + "/" + fileName);
//        int readBit = 0;
//        for (long i = 0; i < Long.parseLong(fileSize); i++) {
//            readBit = in.read();
//            fos.write(readBit);
//        }
//        Client.IS_SUCCESS_FILE_DOWNLOAD = true;
//    }
//} // ClientReceiver
