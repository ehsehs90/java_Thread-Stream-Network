package JavaThread;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam06_ThreadInterrupt extends Application{
   
   TextArea textarea;
   Button startbtn, stopbtn;
   Thread counterThread;
   
   
   private void printMsg(String msg) { 
	   //textarea 에 문자열 출력하는 method   --자바 fx 스레드에게  화면처리해달라고 요청하는것임. msg 넘어오는거 출력해져@
	   
      Platform.runLater(()->{                      //화면 제어는 새로 생성한 Thread가 처리하지 않고 JavaFX Application Thread가 수행하도록 일을 부탁함
         textarea.appendText(msg +"\n");		   //runLater()의 인자로 runnable 객체가 나와야 함
      });
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {                                         // primaryStage 요게 실제 윈도우를 지칭하는 reference
      // 화면 구성해서 window 띄우는 코드
      // 화면 기본 layout을 설정 => 화면을 동서남북 중앙(5개의 영역)으로 분리
      BorderPane root = new BorderPane();
      // Borderpane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
      root.setPrefSize(700, 500);      
      
      // Component생성해서 BorderPane에 부착
      textarea = new TextArea();
      root.setCenter(textarea);
      
      startbtn = new Button("버튼 클릭!!");                 
      startbtn.setPrefSize(250, 50);
      startbtn.setOnAction(t -> {
         // 버튼에서 Action이 발생(클릭)했을 떄 호출!   // setOnAction(여기엔 객체가 들어가요)
    	  
    	  counterThread = new Thread(()->{
    		  try {
    			  for(int i=0;i<9;i++) {
    				  Thread.sleep(1000);                                                           //만약 인터럽트 걸려있으면 어 고만해야지 하고 캐치문으로 탁 튕겨나옴
    				  printMsg(i+"-"+Thread.currentThread().getName());
    				  
    			  }
    		  }catch(Exception e){
    			  //만약 interrupt()가 걸려있는 상태에서 block상태로 진입하면
    			  //Exception을 내면서 catch 문으로 이동.
    			  printMsg("Thread가 종료되었어요");
    		  }    		  
    	  });
    	  
    	  counterThread.start();     
      });
      
      
      stopbtn = new Button("버튼 클릭!!");   // 버튼클릭!! 이들어가있는 버튼이 생성됨
      stopbtn.setPrefSize(250, 50);
      stopbtn.setOnAction(t -> {
         // 버튼에서 Action이 발생(클릭)했을 떄 호출!   // setOnAction(여기엔 객체가 들어가야되요!?)
    	  counterThread.interrupt();  //method가 실행된다고 바로
    	  							  //Thread가 종료되지 않아요!
    	  //interrupt() method가 호출된 Thread 는 sleep()과 같이
    	  //block  상태에 들어가야지 interrupt를 시킬 수 있어요!
    	  
    	  
       
      });
      
      
      FlowPane flowpane = new FlowPane();      // 긴 판자 하나 만든다고 생각하시면 됩니다
      flowpane.setPrefSize(700, 50);
      // flowpane에 버튼을 올려요!
      flowpane.getChildren().add(startbtn);
      flowpane.getChildren().add(stopbtn);
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
