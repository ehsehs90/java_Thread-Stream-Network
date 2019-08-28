package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class EchoRunnable implements Runnable {
	// 결국 얘가 스레드가 되는거에요
	// 가지고 있어야 하는 Field = socket, Inputstream / Outputstream

	Socket socket; // 클라이언트와 연결된 소켓
	BufferedReader br; // 클라이언트로부터 데이터 받기위한 BufferedREader (입)
	PrintWriter out; // 클라이언트로부터 데이터 쏴주기 위한 PrintWriter (출)

	User user;
	String name;
	public void sendMsg(String msg) {
		out.println(msg);
		out.flush();
	}
	
	@Override
	public void run() {
		// 클라이언트와 echo처리를 하는 작업
		// 클라이언트가 문자열을 보내면 해당 문자열을 받아서 다시 클라이언트에게 전달.
		// 한번하고 종료하는게 아니라 클라이언트가 "/EXIT"라는 문자열을 보낼 때 까지 지속
		String line = "";

		try {
			while ((line = br.readLine()) != null) {
//				if (Thread.currentThread().isInterrupted()) {
//					break;
//				}
				if (line.equals("/EXIT/")) { // 문자열 비교를 위해 equals메소드를 사용
					break; // 가장 근접한 loop 탈출
				} else {
					user.setMsg(line);
					user.sendMsg();
//					out.flush(); // 조심해야 할 것 ! flush해야 데이터가 가요.
				}

			}
			// Thread 안에 있는 socket close
			socket.close();
			out.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 생성자에서 아예 스트림 잡아줘요
	public EchoRunnable(Socket socket) {
		super();
		this.socket = socket;
		try {
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EchoRunnable(User user, Socket socket) {
		super();
		this.user = user;
		this.socket = socket;
		// 접속한 clienet로부터 데이터를 읽어들이기 위한 Data InputStream생성
		// in = new DataInputStream(socket.getInputStream());
		// 최초 사용자로부터 닉네임을 읽어들임
		try {
			this.out = new PrintWriter(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.name = Thread.currentThread().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

//입력상자만드러
public class Exam04_EchoServerultiClient extends Application {

	TextArea textarea; // 아예 class의 멤버로 선언
	Button startbtn, stopbtn; // 서버시작, 서버중지버튼

	ExecutorService executorservice = Executors.newCachedThreadPool(); // 쓰레드풀
	// startbtn을 누르든, closebtn을 누르든 이걸 사용할거기 떄문에 얘도 필드로 선언함
	// 클라이언트의 접속을 받아 들이는 서버소켓.
	ServerSocket server;
	User user = new User();

	// 메세지 찍기 위한 간단한 메서드
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
		startbtn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭) 했을 때 호출!
			// 서버 프로그램을 시작
			// 클라이언트의 접속을 기다려요! -> 접속이 되면 Thread를 하나 생성
			// -> Thread를 시작해서 클라이언트와 Thread가 통신하도록 만들어요
			// 서버는 다시 다른 클라이언트의 접속을 기다려요!

			Runnable runnable = () -> {
				try {
					server = new ServerSocket(7780);
					int count = 0; /**/
					Thread thread[] = new Thread[10];/**/
					printMsg("Echo 서버 가동!");
					while (true) {
						printMsg("클라이언트 접속 대기");
						Socket s = server.accept(); // accept가 풀렸다? : 클라이언트가 접속했으니 Thread를 만들고 시작해야 해요!

						printMsg("클라이언트 접속 성공");
						EchoRunnable r = new EchoRunnable(user, s);
						user.add(r);
						executorservice.execute(r); // thread 실행 //executorservice를 이용해 스레드 실행!

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			};

			executorservice.execute(runnable);

		});

		stopbtn = new Button("echo 서버 종료");
		stopbtn.setPrefSize(250, 50);

		// 연결하고 스트림열고 데이터 받아
		stopbtn.setOnAction(t -> {
			executorservice.shutdown();
		});

		FlowPane flowpane = new FlowPane(); // 판자 하나 깔꺼에요 - component 옆으로 길게 늘여트리기 우ㅣ해 플로우펜써요
		flowpane.setPrefSize(700, 50); // 판때기 길게 늘여서
		// flowpane에 버튼을 올려요
		flowpane.getChildren().add(startbtn);
		flowpane.getChildren().add(stopbtn);
		root.setBottom(flowpane);

		// 화면구성 끝!
		// Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("다중 클라이언트 Echo Server 예제 입니다");
		primaryStage.show();

	}

	public static void main(String[] args) {
		// launch() : start()를 실행시키는 함수
		launch();
	}

}
