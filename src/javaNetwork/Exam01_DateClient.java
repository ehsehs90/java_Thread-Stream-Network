package javaNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_DateClient extends Application {

	TextArea textarea; // 아예 class의 멤버로 선언
	Button btn;
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			textarea.appendText(msg+"\n");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();		
		root.setPrefSize(700, 500);

		
		textarea = new TextArea(); // 글상자가 뿅 나타나요
		root.setCenter(textarea); // textarea 를 가운데다 덥썩 붙일거에요

		btn = new Button("Date 서버 접속");
		btn.setPrefSize(250, 50);
		
		btn.setOnAction(t -> {
	
			try {
				Socket socket = new Socket("127.0.0.1", 5554); 
				// 접속할 수 있는 2개의 번호 : 포트번호+url
				// 만약에 접속에 성공하면 Socket객체를 하나 획득
				InputStreamReader isr =
						new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String msg = br.readLine();	
				printMsg(msg);
				
			} catch (Exception e) {
				System.out.println(e);
			}
		});
		
		FlowPane flowpane = new FlowPane(); 
		flowpane.setPrefSize(700, 50); 
		
		flowpane.getChildren().add(btn);
		root.setBottom(flowpane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread객체입니다");
		primaryStage.show();

	}

	
	public static void main(String[] args) {
		launch();
	}

}
