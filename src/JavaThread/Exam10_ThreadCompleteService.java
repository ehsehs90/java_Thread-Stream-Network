package JavaThread;
/*
 * 1 부터 100까지 숫자의 합을 구하려고해요
 * 1~10까지 1개의 Thread가 합을 계산해서 결과를 return
 * 11~20까지 1개의 Thread가 합을 계산해서 결과를 return
 * ...
 * 91~100까지 1개의 Thread가 합을 계산해서 결과를 return 
 * ==> Thread Pool을 이용해야 하고 Callable을 이용해서 리턴값을 받아야 해요
 * ==> 10개의 Thread로부터 각각 데이터를 받아들이는 Thread를 하나 만들어야 해요
 * 
 * 
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam10_ThreadCompleteService extends Application {

	TextArea textarea;
	// initBtn : Thread Pool을 생성
	// startBtn : Thread Pool을 이용해서 Thread를 실행
	// stopBtn : 프로그램이 종료될 때 Thread Pool을 종료
	Button initBtn, stopBtn, startBtn;

	// ExecutorService : Thread Pool
	ExecutorService executorService;
	ExecutorCompletionService<Integer> executorCompletionService;
	private int total = 0;

	private void printMsg(String msg) {
		Platform.runLater(() -> {
			textarea.appendText(msg + "\n");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 화면 구성해서 window에 띄우는 코드
		// BorderPane : 화면 기본 layout을 설정 => 화면을 동/서/남/북/중앙 (5개의 영역)으로 분리
		BorderPane root = new BorderPane();
		// BorderPane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);

		// Component를 생성해서 BorderPane에 부착
		// 글 상자 생성
		textarea = new TextArea();
		// 화면의 가운데에 글 상자 위치
		// default size : 전체
		root.setCenter(textarea);

		initBtn = new Button("Thread Pool 생성");
		initBtn.setPrefSize(250, 50);
		initBtn.setOnAction(t -> {

			executorService = Executors.newCachedThreadPool();
			executorCompletionService = new ExecutorCompletionService<Integer>(executorService);

		});

		stopBtn = new Button("Thread Pool 종료");
		stopBtn.setPrefSize(250, 50);

		// setOnAction(특정 interface를 구현한 객체이고 추상 method를 overriding 하는 코드)
		stopBtn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭)했을 때 호출!
			// shutdown() : Thread Pool 내의 Thread들을 interrupt 걸어서 종료시킨 후
			// Thread Pool도 종료시킴
			executorService.shutdown();

		});

		startBtn = new Button("Thread 실행");
		startBtn.setPrefSize(250, 50);

		// setOnAction(특정 interface를 구현한 객체이고 추상 method를 overriding 하는 코드)
		startBtn.setOnAction(t -> {
			// 버튼에서 Action이 발생(클릭)했을 때 호출!
			for (int i = 0; i < 10; i++) {
				// 람다 내에서 지역변수 쓸 수 없음 -> final 임시 변수 선언
				final int j = i;

				// Callable 객체 생성
				Callable<Integer> callable = new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						IntStream intStream = IntStream.rangeClosed(j, j + 9);
						int sum = intStream.sum();
						return sum;
					}
				};
				executorCompletionService.submit(callable);
			}

			Runnable runnable = new Runnable() {     // 결과값 취합하는 스레드를 만들어야해요!

				@Override
				public void run() {
					for(int i=0;i<10;i++) {
					  try {
						Future<Integer> future = executorCompletionService.take(); //take()일단 대기	
						total += future.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
						
					}
					printMsg("최종 결과값은 : "+ total);
				}
				
			};
			executorService.execute(runnable);
		});

		// FlowPane : 오른쪽으로 붙이는 layout
		// 안스의 Linear Layout
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);

		// FlowPane에 버튼 올리기
		flowpane.getChildren().add(initBtn);
		flowpane.getChildren().add(stopBtn);
		flowpane.getChildren().add(startBtn);
		root.setBottom(flowpane);

		// 실제 Window에 띄우기 위해 Scene 객체 필요
		Scene scene = new Scene(root);
		// Stage primaryStage : 실제 Window 객체
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread Pool 예제입니다");
		// Window에 띄우기
		primaryStage.show();

	}

	public static void main(String[] args) {
		// launch() : start()를 실행시키는 함수
		launch();
	}

}