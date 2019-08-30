package javaNetwork;

import java.io.BufferedReader;
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
public class Exam05_ChatClient extends Application {

	TextArea textarea;
	Button connBtn, disConnBtn; // 서버 접속, 접속 끊기 버튼
	TextField idTf, msgTf;

	// tcp통신하기 위한 소켓/ 스크림 2마리
	Socket socket;
	BufferedReader br;
	PrintWriter out;

	ExecutorService executorService = Executors.newCachedThreadPool();

	// 클라이언트도 이제 스레드가 필요하니까 밑에 쓰레드 풀 만들어.
	// 근데 클라이언트에는 스레드가 1개 밖에 안만들건데 굳이 쓰레드 풀을 만들 필요는 없당..!
	// thread pool도 threadpool객체 만들고 해야하니까

	
	private void printMsg(String msg) {
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}

	// 서버로부터 들어오는 메세지를 계속 받아서 회면에 출력하기 위한 용도의 Thread
	class ReceiveRunnable implements Runnable {
		// 서버로부터 들어오는 메시지를 받아들이는 역할을 수행
		// 소켓에 대한 입력스트림만 있으면 되요!
		// 이 쓰레드는 인풋으로 들어오는 입력 스트림을 받아들여요 (br)
		private BufferedReader br;

		public ReceiveRunnable(BufferedReader br) {
			super();
			this.br = br;
		}

		@Override
		public void run() {
			String line = "";
			try {
				while ((line = br.readLine()) != null) {
					printMsg(line);

				}
			} catch (Exception e) {
				System.out.println(e);

			}

		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		textarea = new TextArea(); // 글상자가 뿅 나타나요
		textarea.setPrefSize(100, 100);

		root.setCenter(textarea); // textarea 를 가운데다 덥썩 붙일거에요

		connBtn = new Button("echo 서버 접속");
		connBtn.setPrefSize(250, 50);
		connBtn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭) 했을 때 호출!
			// 서버 프로그램을 시작
			// 클라이언트의 접속을 기다려요! -> 접속이 되면 Thread를 하나 생성
			// -> Thread를 시작해서 클라이언트와 Thread가 통신하도록 만들어요
			// 서버는 다시 다른 클라이언트의 접속을 기다려요!
				try {
					//클라이언트는 버튼을 누르면 서버쪽에 Socket접속을 시도
					// 만약에 접속에 성공하면 socket객체를 하나 획득
					
					socket = new Socket("127.0.0.1", 6789);
					// Stream을 생성
					InputStreamReader isr = new InputStreamReader(socket.getInputStream());
					br = new BufferedReader(isr);
					out = new PrintWriter(socket.getOutputStream());
					printMsg("채팅 서버 접속 성공");
					
					
					//접속을 성공했으니 이제 Thread를 만들어서 서버가 보내준
					//데이터를 받을 준비를 해요!
					
					ReceiveRunnable runnable = new
							ReceiveRunnable(br);
					executorService.execute(runnable);
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			
		});

		disConnBtn = new Button("접속 종료");
		disConnBtn.setPrefSize(250, 50);
		disConnBtn.setOnAction(t -> {
			// 버튼에서 Action 이 발생 클릭했을 때 호출!
			out.println("/EXIT/");
			out.flush();
			printMsg("서버 접속 종료");	
		
		});

		idTf = new TextField();
		idTf.setPrefSize(100, 40);

		msgTf = new TextField();
		msgTf.setPrefSize(200, 40);
		msgTf.setOnAction(t ->

		{
			// 입력상자 (TextField))에서 enter key가 입력되면 호출
			// 예) 홍길동? 안녕하세요!

			String msg = idTf.getText() + ">" + msgTf.getText();
			out.println(msg);
			out.flush();
			msgTf.setText("");
		});

		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요
		flowpane.getChildren().add(connBtn);
		flowpane.getChildren().add(disConnBtn);
		flowpane.getChildren().add(idTf);
		flowpane.getChildren().add(msgTf);
		root.setBottom(flowpane);
		

		// 화면구성 끝!
		// Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("채팅 클라이언트 입니다");
		primaryStage.show();

	}

	public static void main(String[] args) {
		// launch() : start()를 실행시키는 함수
		launch();
	}

}
