package JavaThread;


import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_ThreadSleep extends Application{
   
   TextArea textarea;
   Button btn;
   
   private void printMsg(String msg) { 
      Platform.runLater(()->{                      //화면 제어는 새로 생성한 Thread가 처리하지 않고 JavaFX Application Thread가 수행하도록 일을 부탁함
         textarea.appendText(msg +"\n");		   //runLater()의 인자로 runnable 객체가 나와야 함
      });
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {   // primaryStage 요게 실제 윈도우를 지칭하는 reference
      // 화면 구성해서 window 띄우는 코드
      // 화면 기본 layout을 설정 => 화면을 동서남북 중앙(5개의 영역)으로 분리
      BorderPane root = new BorderPane();
      // Borderpane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
      root.setPrefSize(700, 500);      
      
      // Component생성해서 BorderPane에 부착
      textarea = new TextArea();
      root.setCenter(textarea);
      
      btn = new Button("버튼 클릭!!");   // 버튼클릭!! 이들어가있는 버튼이 생성됨
      btn.setPrefSize(250, 50);
      btn.setOnAction(t -> {
         // 버튼에서 Action이 발생(클릭)했을 떄 호출!   // setOnAction(여기엔 객체가 들어가야되요!?)
         // 1부터 5까지 5번 반복 => for문 이용 (이럴땐 포문쓰는게 나아)
         IntStream intStream = IntStream.rangeClosed(1, 5);
         intStream.forEach(value -> {
            // 1부터 5까지 5번 반복 => Thread를 생성
            Thread thread = new Thread(()->{
               // 스레드가 무슨짓을 하는지 여기다 코드를 작성하시면 댑니다
               for(int i=0; i<5; i++) {
                  try {
                     Thread.sleep(3000);
                     printMsg(i + " : " +Thread.currentThread().getName());
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }
               }
            });
            thread.setName("TreadNumber-" + value);
            thread.start();
         });
      });
      FlowPane flowpane = new FlowPane();      // 긴 판자 하나 만든다고 생각하시면 됩니다
      flowpane.setPrefSize(700, 50);
      // flowpane에 버튼을 올려요!
      flowpane.getChildren().add(btn);
      root.setBottom(flowpane);            // 바탐쪽에 flowpane 붙이는 작업
      
      // Scene객체가 필요해요.
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);         // 윈도우(primaryStage)에 화면을 설정해 주는 코드
      primaryStage.setTitle("Thread 예제입니다.!!");
      primaryStage.show();
      
   }

   public static void main(String[] args) {
      launch();      // 내부적으로 바로위에 start method가 실행됩니다.
   }
}
