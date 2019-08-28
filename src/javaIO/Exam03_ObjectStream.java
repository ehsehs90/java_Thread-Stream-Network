package javaIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Exam03_ObjectStream {

	public static void main(String[] args) {
		
		Map<String,String>map =
				new HashMap<String, String>();		
		
		map.put("1","홍길동");
		map.put("2","강감찬");
		map.put("3","심사임당");
		map.put("4","마당쇠");
	                                                                      ///** 이 애들을 파일에 저장하고싶어요
		                                                                  ///objectStream이 생기기 전에는 이 데이터를 추출해서 파일에 저장햇어요
		                                                                  ///이를 object STream을 사용해 쉽게 할 수 있어요 ->객체를저장
		
		// 일단 객체가 저장될 파일에 대한 File 객체부터 만들어요!
		// 해당 파일이 존재하는지 그렇지 않은지는 상관 없어요!
		File file = new File("asset/objectStream.txt");                   //파일에 대한 레퍼런스
		
		//객체가 저장할 File에 outputStream부터 열어요!
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);          //새로운 객체 내보내는 Stream
			
			oos.writeObject(map);                                     //map:내가 내보낼 객체(map)를 써줘
			
			
			
			//저장하는 코드이다보니... close를 제대로 해줘야 해요!
			
			oos.close();
			fos.close();  //나중에 만든 스트림먼저 닫아줘요
			
			// 객체가 저장된 파일을 open해서 해당 객체를 프로그램으로 읽어들여요
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis); 
			
			Object obj = ois.readObject();
			
			//문자열로 표현된 객체를 읽어들여서 원래 객체로 복원
			//언마샬링
			
			HashMap<String,String>result = null;
			
			//generic type은 상관하지 않고 Map의 객체인지 확인
			if(obj instanceof Map<?,?>) {
				result=(HashMap<String,String>) obj;                    //만약내가 읽어온 객체가 맵 객체라면 원래상태로 돌려줘
			}
			//key값이 3인 요소 출력
			System.out.println(result.get("3"));
 			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}catch(IOException e) {
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
}
