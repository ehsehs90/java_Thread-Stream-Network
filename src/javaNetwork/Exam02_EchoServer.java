package javaNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
* Echo program을 작성해 보아요
* 클라이언트 프로그램으로부터 문자열은 네트워크를 통해 전달받아서
* 다시 클라이언트에게 전달하는 echo 서버 프로그램* 
* 
* 현재는 단 1번만 echo가 동작하는데 클라이언트가 접속을 종료할 때까지
* echo 작업이 지속적으로 동작하도록 프로그램을 수정!
* 서버는 클라이언트가 종료되면 같이 종료되도록 수정.
* 
* 지금 프로그램은 서버가 클라이언트 1명만 서비스 할 수 있어요!
* 다수의 클라이언트를 서비스 하려면 어떻게 해야 하나요?
* ==> Thread를이용해서 이 문제를 해결.
*  
*/

public class Exam02_EchoServer {

	public static void main(String[] args) {

		ServerSocket server = null;
		Socket socket = null; // 클라이언트와 연결되는 소켓.
		try {
			// 하는 일 : 클라이언트 소켓 대기 -> 소켓 들어오면 튕겨내기
			server = new ServerSocket(5558);
			System.out.println("서버 프로그램 가동 -5558");
			socket = server.accept(); // 클라이언트 기다리고있다가

			// Stream생성
			// 입력 스트림은 버퍼드리더
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// outputstream은 PrintReader
			PrintWriter out = new PrintWriter(socket.getOutputStream());

			// br로부터 데이터를 읽어서 out을 통해 다시 전달

			String msg = "";
			while (!((msg = br.readLine()).equals("/@EXIT"))) {
				// 클라이언트로부터 데이타를 받아들임
				out.println(msg);
				out.flush(); // 서버가 데이터 받아서 다시 클라이언트한테 보내줌.
				// 나머지 사용됐던 리소스 싹 다 정리 ::리소스 해제
				
			}
			out.close();
			br.close();
			socket.close();
			server.close();

			System.out.println("서버프로그램 종료");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
