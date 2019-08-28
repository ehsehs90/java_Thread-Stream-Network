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

class ERunnable implements Runnable{ // Thread 클래스
   // 가지고 있어야하는 Field는 소켓이 있어야하고 소켓으로부터 인풋,아웃풋 스트림을 뽑아내야함.
   Socket socket;  // 클라이언트와 연결된 소켓
   BufferedReader br;  // 입력을 위한 스트림
   PrintWriter out;  // 출력을 위한 스트림
   
   public ERunnable(Socket socket) {
      super();
      this.socket = socket;
      try {
      this.br = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
         this.out = new PrintWriter(socket.getOutputStream());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   @Override
   public void run() {
      // 클라이언트와 echo처리 구현
      // 클라이언트가 문자열을 보내면 해당 문자열을 받아서 다시 클라이언트에게 전달.
      // 한번하고 종료하는게 아니라 클라이언트가 "/EXTI"라는 문자열을
      // 보낼 때까지 지속.
      String line = "";
      try {
         while((line = br.readLine()) != null) { // 접속을 강제로 끊었네? 라는 판단을 하게 함
            if(line.equals("/EXIT/")) {
               break; // 가장 근접한 loop를 탈출!
            }
            else {
               out.println(line);
               out.flush();
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
public class Exam03_EchoServerMultiClient extends Application{

      TextArea textarea;          // 메시지 창 용도로 사용
      Button startbtn,closebtn;  // 서버 시작, 서버 중지 버튼
      ExecutorService executorservice =
            Executors.newCachedThreadPool();  // Cached : 능동적으로 늘었다 줄었다.
      //ServerSocket server = new ServerSocket(7777); 
      // 클라이언트의 접속을 받아들이는 서버 소켓. but 예외처리가 필요하기 때문에 필드에서 직접 생성하기 못한다. 따라서 선언만 해주고 
      // 아래 시작할때 예외처리를 하면서 생성한다.
      ServerSocket server;
      
      private void printMsg(String msg) {
         Platform.runLater(() -> {
            textarea.appendText(msg + "\n");
         });
      }
      
      @Override
      public void start(Stage primaryStage) throws Exception {
         // 화면구성해서 window 띄우는 코드
         // 화면기본 layout을 설정 => 화면을 동서남북중앙(5개의 영역)으로 분리
         BorderPane root = new BorderPane();
         //BorderPane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
         root.setPrefSize(700, 500);
         
         // Component 생성해서 BorderPane에 부착
         textarea = new TextArea();
         root.setCenter(textarea);
         
         startbtn = new Button("Echo 서버 시작!!");
         startbtn.setPrefSize(250,50);
         startbtn.setOnAction(t->{
            // 버튼에서 Action이 발생(클릭)했을 때 호출!
            // 서버 프로그램 시작 버튼
            // 클라이언트 접속을 기다려요! => 접속이 되면 Thread를 하나 생성
            // => Thread를 시작해서 클라이언트와 Thread가 통신하도록 만들어요
            // 서버는 다시 다른 클라이언트의 접속을 기다려요!
            
            Runnable runnable = () -> {
               try {
                  server = new ServerSocket(7777);
                  printMsg("Echo Server 가동 !! ");
                  
                  // Socket s = server.accept(); // 접속 대기 부분. 한줄만 있으면 한명의 클라이언트만 받음
                  while(true) {
                     printMsg("클라이언트 접속 대기");
                     Socket s = server.accept(); // 접속 대기 부분. 여러명의 접속을 위해 무한 루프를 이용 
                                           // accept 떄문에 javaFX가 멈춤. 그래서 Thread를 별개로 만들어야함
                     printMsg("클라이언트 접속 성공");
                     ERunnable r = new ERunnable(s); // 클라이언트가 접속했으니 Thread만들고 시작해요!
                     executorservice.execute(r); // thread 실행
                  }
               } catch (IOException e) {
                  e.printStackTrace();
               }
            };
            executorservice.execute(runnable);         
         }); 
         
         closebtn = new Button("Echo 서버 종료 !!");
         closebtn.setPrefSize(250,50);
         closebtn.setOnAction(t->{
            // 버튼에서 Action이 발생(클릭)했을 때 호출!
            // 접속버튼
                
         }); 
         
         
         FlowPane flowpane = new FlowPane();
         flowpane.setPrefSize(700, 50);
         // flowpane에 버튼을 올려요!
         flowpane.getChildren().add(startbtn);
         flowpane.getChildren().add(closebtn);
         root.setBottom(flowpane);
         
         // Scene 객체가 필요해요.
         Scene scene =new Scene(root);
         primaryStage.setScene(scene);
         primaryStage.setTitle("Thread 예제입니다.");
         primaryStage.show();
      }
         
      public static void main(String[] args) {
         
         launch();
      }
}