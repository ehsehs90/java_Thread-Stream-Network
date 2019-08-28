package JavaThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam08_ThreadPoolBasic extends Application{

	TextArea textarea; //아예 class의 멤버로 선언
	Button initBtn, startBtn, stopBtn;
	
	//initBtn : Thread Pool을 생성하는 버튼
	//startBtn : Thread Pool을 이용해서 Thread를 실행하는 버튼
	//stopBtn : Thread Pool을 종료하는 버튼
	
	ExecutorService executorService;
	// executorService : Thread Pool
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			
			textarea.appendText(msg + "\n");
		});
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 화면 구성해서 window 띄우는 코드	
		// 화면 기본 layout 을 설정 -> 화면을 동서남북 중앙(5개 영역)
		
		BorderPane root = new BorderPane();
		//BorderPane의 크기를 설정 -> 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);
		
		// Component생성해서 BorderPane에 부착
		textarea = new TextArea(); //글상자가 뿅 나타나요
		root.setCenter(textarea); //textarea 를 가운데다 덥썩 붙일거에요
		
		initBtn = new Button("Thread Pool 생성");
		initBtn.setPrefSize(250,50);
		initBtn.setOnAction( t->{
			//버튼에서 Action 이 발생(클릭)했을 시 호출!
			
			executorService = Executors.newCachedThreadPool();
			int threadNum = 
					((ThreadPoolExecutor)executorService).getPoolSize();
			printMsg("현재 Pool안의 Thread개수 : "+ threadNum);
			
			// 처음에 만들어지는 Thread Pool 안에는 thread가 없어요.
			// 만약 필요하면 내부적으로 Thread를 생성.
			// 만드는 Thread의 수는 제한이 없어요.
			// 60초 동안 Thread가 사용되지 않으면 자동적으로 삭제.
			//executorService = Executors.newFixedThreadPool(5);
			// 처음에 만들어지는 Thread Pool안에는 thread가 없어요
			// 만약 필요하면 내부적으로 Thread를 생성
			// **인자로 들어온 int수 만큼의 Thread를 넘지 못해요**
			// Thread가 사용되지 않더라도 만들어진 Thread는 계속 유지.
		});
		
		
		stopBtn = new Button("Thread Pool 종료");
		stopBtn.setPrefSize(250,50);
		stopBtn.setOnAction( t->{			
			executorService.shutdown();   // 스레드의 종료 후 스레드 풀 종료
			
		});
		startBtn = new Button("Thread Pool 실행");
		startBtn.setPrefSize(250,50);
		startBtn.setOnAction( t->{			
			for(int i=0;i<10;i++) {				
				final int k = i;
				Runnable runnable = () -> {
					Thread.currentThread().setName("MYThread "+k); //이름정해주고
					String msg = Thread.currentThread().getName() + 
							" Pool의 개수 : " + 
							((ThreadPoolExecutor)executorService).getPoolSize();
					printMsg(msg);
					try {
					Thread.sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					
				};				
				executorService.execute(runnable);
				//스레드 만들고 스타트 안해 그런거 다 executorservice가 관리해.
				//러너블 객체만 스레드 execute에 넣어주면 얘가 실행시켜줌				
			}			
		});
		
		
	
		FlowPane flowpane = new FlowPane(); //판자 하나 깔꺼에요 - component 옆으로 길게 늘여트리기 우ㅣ해 플로우펜써요
		flowpane.setPrefSize(700,50); //판때기 길게 늘여서
		
		//flowpane에 버튼을 올려요
		flowpane.getChildren().add(initBtn);
		flowpane.getChildren().add(startBtn);
		flowpane.getChildren().add(stopBtn);
		root.setBottom(flowpane); 
		
		//화면구성 끝!
		//Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread객체입니다");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch();
	}


}
