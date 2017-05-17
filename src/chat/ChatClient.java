package chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public class ChatClient extends Frame implements ActionListener { 

	 private static final long serialVersionUID = 1L;
	 private TextField textField= new TextField(24);
	 private TextArea textArea = new TextArea(30, 80);
	 private JFrame	frame;
	 private Panel panel = new Panel();
	 private DataOutputStream strmout;
	 private JButton btn_file = new JButton("��ȭ ����");
	 
	 private int port=9090;
	 private String addr="localhost"; // ������ �������ּ�
	 private Socket sc;
	 private String nickname=null;
	 
	 ArrayList<String> search_list = new ArrayList<String>();
	 ArrayList<String> user = new ArrayList<String>();


	 //

	 public ChatClient(String name){
		 
		this.nickname=name;
		frame = new JFrame(nickname);		
		add(textArea,BorderLayout.CENTER);
		add(textField,BorderLayout.SOUTH);
		add(btn_file,BorderLayout.BEFORE_FIRST_LINE);


		 textField.addActionListener(this);
		 addWindowListener(new WindowAdapter() {
			 @Override
			 public void windowClosing(WindowEvent arg0) {
				 System.exit(0);
			 }
		 });
		 setTitle(nickname);
		 setSize(400,500);
		 setVisible(true);
			
	 }

	 public static void main(String[] args) {
				String name = null;
				Scanner scanner = new Scanner(System.in);

				while( true ) {
					System.out.println("��ȭ���� �Է��ϼ���.");
					System.out.print(">>> ");
					name = scanner.nextLine();
					if (name.isEmpty() == false ) {
						break;
					}
					
					System.out.println("��ȭ���� �ѱ��� �̻� �Է��ؾ� �մϴ�.\n");
				}
				
				scanner.close();
		 new ChatClient(name).connect();
	 }

	 //Ŭ���̾�Ʈ �ٽ� �޼ҵ�
	 public void connect() {
		 // ������ �����Ѵ�.
		 try{
			 sc=new Socket(addr,port); // �����ǿ� ��Ʈ�� �����ش�
			 //
			 strmout=new DataOutputStream(new BufferedOutputStream(sc.getOutputStream()));
			 strmout.writeUTF(nickname);
			 strmout.flush();
			 //
			 textArea.setText("������ ������...\n");
			 run();
		 }catch(Exception e){
			 sc=null;
			 textArea.setText(" ���� ������ ���� �߻�..\n");
		 }
	 }

	 //@Override
	 public void run() {
		 String str;
		 try{
			 btn_file.addActionListener(new File_MakeListener());
			 
			 if(sc==null)
				 return;
			 BufferedReader br=new BufferedReader
					 (new InputStreamReader(sc.getInputStream()));
			 while((str=br.readLine())!=null){
				 textArea.append(str+"\n");
				 search_list.add(str);
			 }
		 }catch(Exception e){
			 textArea.append("������ ������ ������...\n");
			 sc=null;
		 }
	 }

	 @Override
	 public void actionPerformed(ActionEvent e) {
		 Component comp=(Component)e.getSource();
		 if(comp instanceof TextField){
			String s=textField.getText().trim();
			 if(s.length()==0)
				 return;
			 try{
				 // ������ ������� ���� ���
				 if(sc==null)
					 return;
				 //������ ������ �����ϱ�
				 PrintWriter pw=new PrintWriter(sc.getOutputStream(), true);
				 //name=Integer.toString(sc.getPort());
				 pw.println(nickname+"] "+s);
				 search_list.add(nickname+"] "+s);
				 user.add(nickname+"] "+s);
				 textArea.append("����] "+s+"\n");
				 
				 textField.setText("");
				 textField.requestFocus();
			 }catch(Exception e2){
				 textArea.append("������ ������ ������");
			 }
		 }
	 }
	 
	 public class File_MakeListener implements ActionListener{	//��ȭ ������ ���Ϸ� txt���Ϸ�
			public void actionPerformed(ActionEvent arg0) {
				WriterFile file = new WriterFile();
				file.Make_File(search_list,"D:\\bigdata\\chatdialogue.txt");
				String str1 ="D:\\bigdata\\"+nickname+"dialogue.txt";
				file.Make_File(user,str1);
	
				
		}
	}

	
}





