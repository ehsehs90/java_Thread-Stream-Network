package javaNetwork;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exam01_DateServer {
	
	public static void main(String[] args) {
		// 서버쪽 프로그램은 클라이언트의 소켓 접속을 기다려야해요!
		// ServerSocket class 를 이용해서 기능을 구현
		ServerSocket server = null;
		// 클라이언트와 접속된 후 Socket 객체가 있어야지 클라이언트와
		// 데이터 통신이 가능
		Socket socket = null;
		try {
			// port 번호를 가지고 ServerSocket객체를 생성
			// port 번호는 0~65535 사용 가능. 0~1023까지는 예약되어 있어요.
			server = new ServerSocket(5554); /// 5554라는 포트번호를 갖고 서버포켓이 뿅 생겨요
			System.out.println("클라이언트 접속 대기");
			socket = server.accept(); // 클라이언트 접속을 기다려요(block)
			// 만약 클라이언트가 접속해 오면 socket객체를 하나 리턴해요!
			
			PrintWriter out = 
					new PrintWriter(socket.getOutputStream());
			
			
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
			out.print(format.format(new Date())); // 실제 문자열 형태로 데이터 쏴주면 돼
			//일반적으로 Reader와 Writer 는 내부 buffer를 가지고 있어요
			out.flush();          //명시적으로 내부 buffer를 비우고 데이터를 전달 명령
			out.close();
			socket.close();
			server.close();   //서버는 서버소켓도 갖고있음.이것도 닫아줘야해
			
			
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
