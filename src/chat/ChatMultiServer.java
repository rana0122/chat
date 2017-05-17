package chat;

import java.io.*;
import java.net.*;
import java.util.*;


// 1:다 채팅(서버)
public class ChatMultiServer {
	 // 클라이언트 소켓 객체를 저장하기 위한 객체
	 private Vector<Socket> client=new Vector<Socket>();
	 private static final int PORT = 9090;

	 
	 // 내부 스레드 클래스
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
				 // 접속한 클라이언트를 백터에 저장한다.
				 client.add(sc);
				 // 접속 클라이언트의 ip
				 ip=sc.getInetAddress().getHostAddress();
				 port=sc.getPort();
				 
				 log( "연결됨 from " +  ip + ":" + port );

				 strmin=new DataInputStream(sc.getInputStream());
				 recv=strmin.readUTF();
				 
				 
				 // 클라이언트의 정보를 읽어 들이기 위한 입력 스트림
				 BufferedReader br=new BufferedReader(new InputStreamReader(sc.getInputStream()));
				 // 다른 클라이언트에게 접속 사실을 알림()
				 str=recv+" 입장 !!!";
				 log( str);
				 for(Socket s : client) {
					 if(sc==s)
						 continue;
					 PrintWriter pw=new PrintWriter(
							 s.getOutputStream(), true);
					 pw.println(str);

				 }
				 System.out.println(str);
				 // 클라이언트가 보낸 메시지 읽기
				 while((str=br.readLine())!=null) {
					 // 다른 클라이언트에게 전송
					 for(Socket s: client) {
						 if(sc==s)
							 continue;
						 PrintWriter pw=new PrintWriter(s.getOutputStream(), true);
						 pw.println(str);

					 }
				 }
			 } catch (Exception e) {
				 str=recv+" 퇴장...";
				 log( str );
				 try {
					 // 다른 클라이언트에게 퇴장 사실 알림
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
			 log( "서버시작...." );
			 while(true) {
				 // 클라이언트가 접속해서 들어오기를 대기함
				 Socket sc=ss.accept();
				 // 접속한 클라이언트를 처리하기 위한  쓰레드 객체
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