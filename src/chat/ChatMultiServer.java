package chat;

import java.io.*;
import java.net.*;
import java.util.*;


// 1:�� ä��(����)
public class ChatMultiServer {
	 // Ŭ���̾�Ʈ ���� ��ü�� �����ϱ� ���� ��ü
	 private Vector<Socket> client=new Vector<Socket>();
	 private static final int PORT = 9090;

	 
	 // ���� ������ Ŭ����
	 class Worker extends Thread {
		 private Socket sc=null;
		 private DataInputStream strmin;
		 private String ip=null;
		 private int port=0;

		 
		 public Worker(Socket sc) {
			 this.sc=sc;
		 }

		 public void run() {
			 String str;
			 String recv=null;

			 try {
				 // ������ Ŭ���̾�Ʈ�� ���Ϳ� �����Ѵ�.
				 client.add(sc);
				 // ���� Ŭ���̾�Ʈ�� ip
				 ip=sc.getInetAddress().getHostAddress();
				 port=sc.getPort();
				 
				 log( "����� from " +  ip + ":" + port );

				 strmin=new DataInputStream(sc.getInputStream());
				 recv=strmin.readUTF();
				 
				 
				 // Ŭ���̾�Ʈ�� ������ �о� ���̱� ���� �Է� ��Ʈ��
				 BufferedReader br=new BufferedReader(new InputStreamReader(sc.getInputStream()));
				 // �ٸ� Ŭ���̾�Ʈ���� ���� ����� �˸�()
				 str=recv+" ���� !!!";
				 log( str);
				 for(Socket s : client) {
					 if(sc==s)
						 continue;
					 PrintWriter pw=new PrintWriter(
							 s.getOutputStream(), true);
					 pw.println(str);

				 }
				 System.out.println(str);
				 // Ŭ���̾�Ʈ�� ���� �޽��� �б�
				 while((str=br.readLine())!=null) {
					 // �ٸ� Ŭ���̾�Ʈ���� ����
					 for(Socket s: client) {
						 if(sc==s)
							 continue;
						 PrintWriter pw=new PrintWriter(s.getOutputStream(), true);
						 pw.println(str);

					 }
				 }
			 } catch (Exception e) {
				 str=recv+" ����...";
				 log( str );
				 try {
					 // �ٸ� Ŭ���̾�Ʈ���� ���� ��� �˸�
					 for(Socket  s: client) {
						 if(s==sc)
							 continue;
						 PrintWriter pw=new PrintWriter(
								 s.getOutputStream(), true);
						 pw.println(str);
					 }
				 } catch (Exception e2) {
					 log( "error:" + e2 );
				 }
				 client.remove(sc);
				 sc=null;
			 }
		 }
	 }

	 public void serverStart() {
		 ServerSocket ss=null;
		 try {
			 ss=new ServerSocket(PORT);
			 log( "��������...." );
			 while(true) {
				 // Ŭ���̾�Ʈ�� �����ؼ� �����⸦ �����
				 Socket sc=ss.accept();
				 // ������ Ŭ���̾�Ʈ�� ó���ϱ� ����  ������ ��ü
				 Worker worker=new Worker(sc);
				 worker.start();
			 }
		 } catch (Exception e) {
			 log( "error:" + e );
		 }
	 }

		public static void log( String log ) {
			System.out.println( "[chat-server] " + log );
		}

	 public static void main(String[] args) {
		 new ChatMultiServer().serverStart();
	 }
}