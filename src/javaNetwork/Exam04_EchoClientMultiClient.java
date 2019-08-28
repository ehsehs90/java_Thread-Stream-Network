package javaNetwork;

/*
 * Echo program을 작성해 보아요
 * 클라이언트 프로그램으로부터 문자열은 네트워크를 통해 전달받아서
 * 다시 클라이언트에게 전달하는 echo 서버 프로그램* 
 * 
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;



//입력상자만드러
public class Exam04_EchoClientMultiClient extends Application {

	TextArea textarea; // 아예 class의 멤버로 선언
	Button startbtn, stopbtn; // 서버접속버튼
	TextField tf; // 말그대로 입력상자. 자바 fx에 대한 한줄짜리 입력상자 이게 필요했다꼬@@@@@@@@@
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	ExecutorService executorService = Executors.newCachedThreadPool();
	
	private void printMsg(String msg) {
		
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		textarea = new TextArea(); // 글상자가 뿅 나타나요
		textarea.setPrefSize(100, 100);

		root.setCenter(textarea); // textarea 를 가운데다 덥썩 붙일거에요

		startbtn = new Button("echo 서버 접속");
		startbtn.setPrefSize(250, 50);

		stopbtn = new Button("echo 서버 종료");
		stopbtn.setPrefSize(250, 50);

		// 연결하고 스트림열고 데이터 받아
		stopbtn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭)했을 때 호출!
			try {
				// 클라이언트는 버튼을 누르면 서버쪽에 Socket 접속을 시도
				// Socket에 URL과 port 번호를 넘겨줘야 함
				out.println("/@EXIT");
				out.close();
				br.close();
				socket.close();
				printMsg("Echo 서버 접속 종료");
			} catch (Exception e) {
				System.out.println(e);
			}

		});

		startbtn.setOnAction(t -> {
			// 버튼에서 Action 이 발생(클릭) 했을 때 호출!

			// TextArea에 글자를 넣어요
			// 접속버튼
			try { // 여기에서 서버에 접속할거임
				socket = new Socket("70.12.115.71", 7780); // 접속할 수 있는 2개의 번호 : 포트번호+url

				// Stream을 생성
				// 이미 선언된 필드들을 만들어주는 코드만 써요
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream()); // 서버쪽에서 만든 스트림. 똑같이
				printMsg("Echo 서버 접속 성공!");
				// 클라이언트가 하는일
				///// 입력상자를 통해서 데이터 떙겨서
			} catch (Exception e) {
				System.out.println(e);
			}

			Runnable Echorunnable = () -> {
				while (true) {
					try {
						String result;
						result = br.readLine();
						printMsg(result);
					} catch (IOException e) {						
						e.printStackTrace();
					}
				}
			};
			executorService.execute(Echorunnable);
		});

		tf = new TextField();
		tf.setPrefSize(200, 40);

		tf.setOnAction(t -> {
			// 입력상자(TextField)에서 enter key 가 입력되면 호출
			String msg = tf.getText(); // 입력상자에서 사용자가 입력한 글 딱 들고 와
			out.println("ㄷㅎㄷㅎ"+msg); // 서버로 문자열 전송!
			out.flush();
			tf.clear();
		

		});

		FlowPane flowpane = new FlowPane(); // 판자 하나 깔꺼에요 - component 옆으로 길게 늘여트리기 우ㅣ해 플로우펜써요
		flowpane.setPrefSize(700, 50); 	
		// flowpane에 버튼을 올려요
		flowpane.getChildren().add(startbtn);
		flowpane.getChildren().add(stopbtn);
		flowpane.getChildren().add(tf);
		root.setBottom(flowpane);

		// 화면구성 끝!
		// Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("에코 멀티클라이언트 예제 입니다");
		primaryStage.show();
	}

	public static void main(String[] args) {
		// launch() : start()를 실행시키는 함수
		launch();
	}

}
