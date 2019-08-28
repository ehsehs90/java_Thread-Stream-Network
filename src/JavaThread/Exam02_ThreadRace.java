package JavaThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

class UserPanel extends FlowPane {
	private TextField nameField = new TextField();
	private ProgressBar progressBar = new ProgressBar(0.0);
	private ProgressIndicator progressIndicator = new ProgressIndicator(0.0);
	// progress bar(0은 아예없는거 1은 꽉찬거)

	
	public UserPanel() { // 기본생성자
	}

	public UserPanel(String name) { // 생성자
		setPrefSize(700, 50); // 전체길이
		nameField.setText(name);
		nameField.setPrefSize(100, 50);
		progressBar.setPrefSize(500, 50);
		progressIndicator.setPrefSize(50, 50);
		getChildren().add(nameField);
		getChildren().add(progressBar);
		getChildren().add(progressIndicator);
	}

	public TextField getNameField() {
		return nameField;
	}

	public void setNameField(TextField nameField) {
		this.nameField = nameField;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public ProgressIndicator getProgressIndicator() {
		return progressIndicator;
	}

	public void setProgressIndicator(ProgressIndicator progressIndicator) {
		this.progressIndicator = progressIndicator;
	}

}

class ProgressRunnable implements Runnable { //ProgressRunnable 객체 생성
    int seq=0;
    public ProgressRunnable(int seq) {
        this.seq = seq;
    } 
	private String name;
	private ProgressBar progressBar;
	private ProgressIndicator progressIndicator;
	private TextArea textarea;
	
	//입력으로 들어오는 constructor의 인자로 들어오는 넘을 매핑시켜.. ProgressRunnable의 생성자
	public ProgressRunnable(String name, ProgressBar progressBar, ProgressIndicator progressIndicator,
			TextArea textarea) {
		super();
		this.name = name;
		this.progressBar = progressBar;
		this.progressIndicator = progressIndicator;
		this.textarea = textarea;
	}
	 
	@Override
	public void run() {
		//Thread 가 동작해서 각각의 progressBar를 제어하면 될 거 같아요
       Random random = new Random();
       double k=0; 
       int c=1;
       while(progressBar.getProgress()< 1.0 ){   //(progressBar.getProgress(): 현재 프로그래스 바의 값을 팅겨내요
    	   try {
    		   Thread.sleep(200);
    		   
    		   k+= (random.nextDouble()*0.1);  
    		   //쓰레드가 잤다 깼다 잤다 꺳다 하면서 k값이 지속적으로 증가한다.
    		   //이 k값을 자바 fx 쓰레드에게 넘겨 왜냐하면 자바 fx쓰레드가 화면처리를 하기 떄문에
    		    final double tt = k;
    		      		   
    		   Platform.runLater(()->{
    			  progressBar.setProgress(tt); // 지금 내 progress값을 k값으로 셋팅해줘~
    			  progressIndicator.setProgress(tt);
    			  
    		   });
    		  
    		   if(k>1.0) {
    			   this.seq++;
    			   textarea.appendText(name+this.seq);
    			   
    		   break; 
    		   }
    	   }catch(Exception e){
    		   System.out.println(e);  
    	   }
       }
	}
}

public class Exam02_ThreadRace extends Application {
	

	private List<String> names = Arrays.asList("강동원", "이순신", "전지현");

	// progressBar를 제어할 Thread가 FlowPane 1개당 1개씩 존재해야 해요.
	private List<ProgressRunnable> uRunnable =
			new ArrayList<ProgressRunnable>();
	TextArea textarea; // 아예 class의 멤버로 선언
	Button btn;

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(Thread.currentThread().getName());

		// 화면 구성해서 window 띄우는 코드
		// 화면 기본 layout 을 설정 -> 화면을 동서남북 중앙(5개 영역)
		BorderPane root = new BorderPane();
		// BorderPane의 크기를 설정 -> 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);
		
		//center부분을 차지할 TilePane을 생성해야 해요!
		TilePane center = new TilePane();
		center.setPrefColumns(1);            //그리드가 격자인데 우리가 필요한건 1열. //1열만 존재하는 TilePane
		center.setPrefRows(4);                 //사람 3명 textarea 1개

		//메시지가 출력될 TextArea 생성 및 크기 결정
		textarea = new TextArea();
		textarea.setPrefSize(600, 100);
		
		//이제 만들어진 TilePane에 3개의 FlowPane과 TextArea를 부착
		for(String name : names) {
			UserPanel panel = new UserPanel(name); //홍길동이라는 이름 가지고 패널 하나! 이름 하나씩 꺼내서 패널하나!
			center.getChildren().add(panel); //팡팡팡 갖다 붙여요
			uRunnable.add(new ProgressRunnable(
					panel.getNameField().getText(),
					panel.getProgressBar(),
					panel.getProgressIndicator(),					
					textarea)); //어레이 리스트에 러너블 객체 뿅뿅뿅뿅 넣은담에 //객체 생성자 순서 맞춰서 써
			
			
			//갖고 있어야할 정보를 인자로 갖고 있어서 객체 넘겨줘..
			//프로그레스바 제어 
	        //텍스트 에리아 찍어줄것도 
		}
		center.getChildren().add(textarea); //맨 마지막에는 textarea 1개 붙여요
		
		root.setCenter(center); // 
				
		btn = new Button("버튼 클릭");
		btn.setPrefSize(250, 50);
		btn.setOnAction(t -> {
			// 버튼에서 Action 이 발생(클릭) 했을 때 호출!
			// uRunnable(ArrayList)를 돌면서 Thread를 생성하고
			// start() 호출
			for(ProgressRunnable runnable : uRunnable) {
				new Thread(runnable).start();
			}
			
		});

		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요
		flowpane.getChildren().add(btn);
		root.setBottom(flowpane);

		// 화면구성 끝!
		// Scene 객체가 필요해요
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread객체입니다");
		primaryStage.show();
	}

	public static void main(String[] args) {

		// 현재 main method를 호출한 Thread의 이름을 출력!
		System.out.println(Thread.currentThread().getName());
		// currentThread 는 현재 동작하고 있는 thread중 이 코드를 사용하는 thread를 딱 찾아요.
		launch();

	}

}
