package JavaThread;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class JavaFxUiTemplate extends Application{

	TextArea textarea; //아예 class의 멤버로 선언
	Button btn;
	
	
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
		
		
		btn = new Button("버튼 클릭");
		btn.setPrefSize(250,50);
		
		//btn.setOnAction("여기 안에는 리스너 객체가 들어와야 해요");  //안드로이드의 setOnClickListener 같은넘 
		btn.setOnAction(t->{
			// 버튼에서 Action 이 발생(클릭) 했을 때 호출!
			// TextArea에 글자를 넣어요
			textarea.appendText("소리없는 아우성!!\n");
			
		});  
		FlowPane flowpane = new FlowPane(); //판자 하나 깔꺼에요 - component 옆으로 길게 늘여트리기 우ㅣ해 플로우펜써요
		flowpane.setPrefSize(700,50); //판때기 길게 늘여서
		
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
		
	}


}
