package chat;

import java.io.*;
import java.util.ArrayList;

public class WriterFile {
	
	public void Make_File(ArrayList<String> search_list,String name){
	
	int index = 0;
	try{
		FileWriter writer = new FileWriter(name);//������ �̸�
		
			writer.write(search_list.size());
			while(index < search_list.size()){
			String chat_list = "";
			chat_list = (String)search_list.get(index);
			writer.write(chat_list);		//arraylist�� ��� index�� 
			writer.write("\r\n");									//���Ͽ� ����
			index++;
		}
		writer.close();
	}catch (IOException e){
		e.printStackTrace();
		}
	}
}