package JavaThread;
/*
 * Java Application은 main thread가 main() method를 호출해서 실행.
 * 
 * 프로그램은 main method()가 종료 될 때 종료되는 것이 아니라
 * 프로그램 내에서 파생된 모든 Thread가 종료될 때 종료되요!!
 * 
 * 1. Thread 의 생성
 *    => Thread class를 상속받아서 class를 정의하고 객체 생성해서 사용
 *    => Runnable interface를 구현한 class를 정의하고 객체를 생성해서 
 *       Thread 생성자의 인자로 넣어서 Thread 생성.
 *    => 현재 사용되는 Thread의 이름을 출력!
 * 2. 실제 Thread의 생성(new) 
 *    -> start()     (thread 를 실행시키는게 아니라 runnable 상태로 전환) 
 *    -> JVM안에 있는 Thread schedule에 의해  하나의 Thread가 선택되서 thread가 running상태로 전환 
 *    -> 어느 시점이 되면 Thread scheduler에 의해서 runnable 상태로 전환 
 *    -> (다른+방금 선택된 놈) thread
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_ThreadBasic extends Application{

	TextArea textarea; //아예 class의 멤버로 선언
	Button btn;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(Thread.currentThread().getName());
		
		
		
		// 화면 구성해서 window 띄우는 코드	
		// 화면 기본 layout 을 설정 -> 화면을 동서남북 중앙(5개 영역)
		
		BorderPane root = new BorderPane();
		//BorderPane의 크기를 설정 -> 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);
		
		// Component생성해서 BorderPane에 부착
		textarea = new TextArea(); //글상자가 뿅 나타나요
		root.setCenter(textarea); //textarea 를 가운데다 덥썩 붙일거에요
		
		
		btn = new Button("버튼 클릭");
		btn.setPrefSize(250,50);		
		btn.setOnAction(t->{
			// 버튼에서 Action 이 발생(클릭) 했을 때 호출!
		    // 버튼을 클릭하면 Thread를 생성
			new Thread(()->{
				System.out.println(Thread.currentThread().getName()); 
				//화면 제어는 JavaFX Application Thread가 담당
				// textarea에 출력하기 위해서 JavaFX Application Thread
				// 한테 부탁을 해요! 
				Platform.runLater(()->{
					textarea.appendText("소리없는 아우성~");  
					//화면 깨지는걸 방지하기 위해 이런식으로 화면출력할거임.
				}); //쓰레드 안 쓰레드파생시켜 거기다가 요청쓰 해버려~				
			}).start();
		});  
		
		FlowPane flowpane = new FlowPane(); 
		flowpane.setPrefSize(700,50); 
		//flowpane에 버튼을 올려요
		flowpane.getChildren().add(btn);
		root.setBottom(flowpane); 
		
		//화면구성 끝!
		//Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread객체입니다");
		primaryStage.show();		
	}
	
	
	public static void main(String[] args) {
		
		//현재 main method를 호출한 Thread의 이름을 출력!
		System.out.println(Thread.currentThread().getName());
		//currentThread 는 현재 동작하고 있는 thread중 이 코드를 사용하는 thread를 딱 찾아요.
		launch();
		
	}


}
