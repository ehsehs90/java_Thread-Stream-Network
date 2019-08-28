package JavaThread;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam07_ThreadDaemon extends Application {

	TextArea textarea;
	Button btn;

	@Override
	public void start(Stage primaryStage) throws Exception {                                // primaryStage 요게 실제 윈도우를 지칭하는 reference
		// 화면 구성해서 window 띄우는 코드
		// 화면 기본 layout을 설정 => 화면을 동서남북 중앙(5개의 영역)으로 분리
		BorderPane root = new BorderPane();
		// Borderpane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);

		// Component생성해서 BorderPane에 부착
		textarea = new TextArea();
		root.setCenter(textarea);

		btn = new Button("버튼 클릭!!"); // 버튼클릭!! 이들어가있는 버튼이 생성됨
		btn.setPrefSize(250, 50);
		btn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭)했을 떄 호출! // setOnAction(여기엔 객체가 들어가야되요!?)
			// Thread를 생성 (for loop를 1초 sleep하면서 10번 반복
			// 이 Thread는 dead 상태로 가기 위해서는 10초가 걸려요!
			Thread thread = new Thread(() -> {
				try {

					for (int i = 0; i < 10; i++) {
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			});
			thread.setDaemon(true); // 해당 Thread를 daemon thread로 설정합니다.
			// 자식 thread가 된다고 생각하면 되요.
			// 부모 thread가 중지되면 자동적으로 자식 thread도 중지
			// 화면을 만든 스레드를 죽으면 버튼을 눌러 동작하는 스레드 같이 사망하게 만듬

			thread.start();

		});

		FlowPane flowpane = new FlowPane(); // 긴 판자 하나 만든다고 생각하시면 됩니다
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요!
		flowpane.getChildren().add(btn);
		root.setBottom(flowpane); // 바탐쪽에 flowpane 붙이는 작업

		// Scene객체가 필요해요.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene); // 윈도우(primaryStage)에 화면을 설정해 주는 코드
		primaryStage.setTitle("Thread 예제입니다.!!");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(); // 내부적으로 바로위에 start method가 실행됩니다.
	}
}
