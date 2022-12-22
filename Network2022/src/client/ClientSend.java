package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Scanner;

class ClientSend extends Thread {
	
    Socket socket;
    DataOutputStream out;
    FileInputStream fileIn;
    String name;
    String id;
    String pw;
    Scanner scanner;
    FileInputStream fis;
    File file;

    ClientSend(Socket socket, String name) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            this.name = name;
        } catch (Exception e) {}
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        try {
            access();
            System.out.println("** FTP ������ �����Ͽ����ϴ�. **");
            while (out != null) {
                String command = scanner.nextLine();
                sortCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void access() throws IOException{
        while (true) {
        	
        	// �α��� ����
            if (Client.LOGIN_STATUS)
                break;
            
            System.out.print("ID: ");
            id = scanner.nextLine();
            System.out.print("PASS: ");
            pw = scanner.nextLine();

            out.writeUTF("[AUTH]"+Client.STANDARD+id+Client.STANDARD+pw);

            Client.LOAD_STATUS = true;
            while(Client.LOAD_STATUS){}
        }
    }

    private void sortCommand(String command) throws IOException {
    	
        String[] commandDetail = command.split(" ");

        switch (commandDetail[0]) {
            case "/list": {
                out.writeUTF("[LIST]"+Client.STANDARD);
                break;
            }
            case "/upload": {
                if (commandDetail.length >= 3) {
                    longFile(commandDetail[1], commandDetail[2]);
                } else if (commandDetail.length == 2){
                    shortFile(commandDetail[1]);
                } else {
                    System.out.println("/upload ���ϰ�� ���ϸ� �������� �Է��Ͻÿ�.");
                }
                break;
            }
            case "/download": {
                if (commandDetail.length < 2) {
                    System.out.println("/download ���ϸ� �������� �Է��Ͻÿ�.");
                } else {
                    downloadFile(commandDetail[1]);
                }
                break;
            }
            case "/exit": {
                exit();
                break;
            }
            default: {
                out.writeUTF("["+name+"] " + command);
            }
        }
    }

    private void downloadFile(String fileName) throws IOException {
        out.writeUTF("[DOWNLOAD]" +
        		Client.STANDARD +
                fileName
        );
        while (true) {
            if (Client.FILE_STATUS != null)
                break;
            System.out.print("");
        }
        if (Client.FILE_STATUS) {
            System.out.println(
            		Client.FILE_PATH +
                    " ��ο� ���� �ٿ�ε尡 �Ϸ�Ǿ����ϴ�." +
                    fileName
            );
        } else {
            System.out.println("���� �ٿ�ε忡 �����߽��ϴ�.");
        }
        Client.FILE_STATUS = null;
    }

    private void shortFile(String path) throws IOException {
        fis = initFile(path);
        if (fis == null)
            return;
        file = new File(path);
        sendFileInfo(file.getName());
    }

    private void longFile(String path, String name) throws IOException {
        fis = initFile(path);
        if (fis == null)
            return;
        file = new File(path);

        String ext;
        if (!name.contains(".")) {
            String fileName = file.getName();
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            name = name + "." + ext;
        }
        sendFileInfo(name);
    }

    private void sendFileInfo(String fileName) throws IOException {
        out.writeUTF("[UPLOAD]" +
                Client.STANDARD +
                Files.size(file.toPath()) +
                Client.STANDARD +
                fileName
        );

        while(Client.DUP_LOAD_STATUS) { }

        if (Client.DUP_STATUS) {
            System.out.println("���� �̸��� ������ �ֽ��ϴ�.");
            System.out.println("����� �Ͻǰǰ���? (Y: ����� / N: ���)");

            String answer = scanner.nextLine();

            if (answer.toLowerCase(Locale.ROOT).equals("y")) {
                readFile(fis, true);
            } else {
                readFile(fis, false);
            }
        } else {
            readFile(fis, true);
        }

        Client.DUP_LOAD_STATUS = true;
        Client.DUP_STATUS = null;
    }

    private void exit() {
        System.out.println("������ �����մϴ�.");
        System.exit(0);
    }

    private FileInputStream initFile(String filePath) {
        File file = new File(filePath);
        try {
            fileIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("������ ã�� �� �����ϴ�.");
            return null;
        }
        return fileIn;
    }

    private void readFile(FileInputStream fis, Boolean result) throws IOException {
        if (result) {
            out.writeUTF("[SEND_FILE]");
            byte[] bytes = new byte[1024];
            int readBit = 0;
            while((readBit = fis.read(bytes)) != -1) {
                out.write(bytes, 0, readBit);
            }
        } else {
            out.writeUTF("[CANCEL_SEND_FILE]");
        }
    }
    
}





































