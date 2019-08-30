package javaNetwork;

/*
 * Echo program을 작성해 보아요
 * 클라이언트 프로그램으로부터 문자열은 네트워크를 통해 전달받아서
 * 다시 클라이언트에게 전달하는 echo 서버 프로그램* 
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

//입력상자만드러
public class Exam05_ChatServer extends Application {

	TextArea textarea; // 메세지 창
	Button startbtn, stopbtn; // 서버 시작, 서버 종료버튼
	ServerSocket server; // 클라이언트의 접속을 받아들이는 놈.

	// ThreadPool - 나중에 이 Threadpool을 이용하여 클라이언트 스레드를 실행시킬거에요!
	ExecutorService executorService = Executors.newCachedThreadPool(); // ThreadPool 만들어내요.

	// singleton형태의 공유 객체를 생성
	SharedObject shardObject = new SharedObject(); // 프로그램 서버 빵 시작하면 shard object만들거에요

	private void printMsg(String msg) {
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}

	// 클라이언트와 연결된 Thread가 사용하는 공유객체를 만들기 위한
	// 클래스를 사용.
	// inner class 형태로 선언 ( 사용하기 편해요!! )
	// Thread 와 공유객체는 Thread 가 가지고 있어야 하는 자료구조.
	// 기능을 구현해 놓은 객체를 지칭해요!

	// 공유객체 는 싱글톤으로 동작해요. Thread는 딱 한1개의 클래스 객체를 이용해요
	class SharedObject {
		// 클라이언트 Thread를 저장하고 있어야 해요!
		List<ClientRunnable> clients = new ArrayList<ClientRunnable>();

		// 우리가 필요한 기능은.. Broadcast. 메서드 틀 만들어요
		// Thread 가 클라이언트로부터 데이터를 받아서 모든 클라이언트 Thread
		// 에게 데이터를 전달하는 기능을 구현해요!
		
		//모든 클라이언트에 대한 Arraylist. Stream 열어요. 안에 있는 객체 하나씩 끄집어서 
		//공유 객체의 method는 여러 Thread에 의해서 동시에 사용할 수 있어요.
		
		//채팅 메세지가 순차적으로 수행되어야 채팅 순서가 정상적 이겠죠?
		//이런 경우에는 동기화 처리를 해 줘야지 문제없이 출력할 수 있어요.
		// 나 얘기할거니까 다들 조용히해봐@@@@@ 이런느낌..
		
		public synchronized void broadcast(String msg) {
			clients.stream().forEach(t->{
				t.out.println(msg);
				t.out.flush();
			});

		}
	}

	// 클라이언트와 매핑되는 Thread를 만들기 위한 Runnable class
	class ClientRunnable implements Runnable {

		private SharedObject shardObject; // 공유객체
		private Socket socket; // 클라이언트와 연결한 socket
		private BufferedReader br;
		private PrintWriter out;

		// Thread가
		// 생성될 때 Thread 에는 2개의 객체가 전달되어야 해요!
		// 생성자를 2개의 인자(공유객체와 소켓)을 받는 형태로 작성
		// 일반적으로 생성자는 필드 초기화를 담당하기 때문에 여기에서 Stream을
		// 생성
		
		//소켓만들어서 -> 접속하면 -> Stream열어
		
		public ClientRunnable(SharedObject shardObject, Socket socket) {
			super();
			this.shardObject = shardObject;
			this.socket = socket;
			try {
				this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 클라이언트의 Thread가 시작되면 run method() 가 호출되서
		// 클라이언트와 데이터 통신을 시작
		// 반복적으로 클라이언트의 데이터를 받아서 공유객체를 이용해서
		// broadcasting  /// 받아들이고 보내고 받아들이고 보내고 받아들이고 보내고 무한루프 !

		@Override	
		public void run() {
			
			// 일단 대기 타다가 클라이언트로부터 데이터가 오면  브로드캐스팅 해! 			
			String msg ="";
			try {
				while((msg = br.readLine()) != null) {
					//클라이언트가 채팅을 종료하면
					if(msg.equals("/EXIT/")) {
						break;
					}	
					//그게 아니라면?즉 일반적인 채팅 메세지인경우 -> 모든 클라이언트에게 전달!(broadcasting)
					shardObject.broadcast(msg);					
				}
				
			}catch(Exception e){
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
		startbtn = new Button("채팅 서버 시작");
		startbtn.setPrefSize(250, 50);
		startbtn.setOnAction(t -> {
			// 버튼에서 Action이 발생 (클릭) 했을 때 호출!
			textarea.clear();
			printMsg("[채팅 서버 가동 - 6789]");
			// 서버소켓을 만들어서 클라이언트 접속을 기다려야 해요!
			// JavaFX thread가 blocking되지 않도록 새로운 Thread를 만들어서 (창과 클라이언트 접속을 분리시켜)
			// 클라이언트 접속을 기다려야 해요!
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						server = new ServerSocket(6789);
						while (true) {
							printMsg("[클라이언트 접속 대기중!!]");
							Socket socket = server.accept();
							printMsg("[클라이언트 접속 성공!]");

							// 클라이언트가 접속했으니 Thread를 하나
							// 생성하고 실행해야 해요!
							ClientRunnable cRunnable = new ClientRunnable(shardObject, socket);

							// 새로운 클라이언트가 접속되엇으니
							// 공용객체의 List 안에 들어가야 겠죠? 방금만든cRunnable 을 list안에 쒹 넣어요

							// Thread에 의해서 공용객체의 데이터가 사용될 때는
							// 동기화 처리를 해 줘야 안전해요.

							shardObject.clients.add(cRunnable);
							printMsg("[현재 클라이언트 수 :]" + shardObject.clients.size() + "]");
							executorService.execute(cRunnable);

							// 정리 : 실제 서버가 하는일 -> 클라이언트의 접속 대기 하고 있다가 클라이언트 접속 하면 list에 하나씩 add 하고 가동시켜요

						}
					} catch (IOException e) {
						System.out.println(e);
					}

				}
			};
			executorService.execute(runnable);
		});

		stopbtn = new Button("채팅 서버 종료");
		stopbtn.setPrefSize(250, 50);

		// 연결하고 스트림열고 데이터 받아
		stopbtn.setOnAction(t -> {
		});

		FlowPane flowpane = new FlowPane(); // 판자 하나 깔꺼에요 - component 옆으로 길게 늘여트리기 우ㅣ해 플로우펜써요
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요
		flowpane.getChildren().add(startbtn);
		flowpane.getChildren().add(stopbtn);
		root.setBottom(flowpane);

		// 화면구성 끝!
		// Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("방 1개짜리 채팅입니다!");
		primaryStage.show();
	}

	public static void main(String[] args) {
		// launch() : start()를 실행시키는 함수
		launch();
	}

}
