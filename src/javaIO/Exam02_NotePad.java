package javaIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*
 * JavaFX를 이용해서 GUI프로그램을 만드려고해요
 * 화면에 창 띄우려면 Application 이라는 class의 instance를 생성   **1. extend시켜주시구요
 */

public class Exam02_NotePad extends Application { // **2. 상속받으려면 반드시 start() 오버라이딩 해야해

	TextArea textarea;
	Button openBtn, saveBtn;
	File file;

	private void printMsg(String line) {
		Platform.runLater(() -> {
			textarea.appendText(line + "\n");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane(); // ** 그림을 그려요
		root.setPrefSize(700, 500);

		textarea = new TextArea();
		root.setCenter(textarea);

		openBtn = new Button("파일 열기");
		openBtn.setPrefSize(150, 50);
		openBtn.setOnAction(t -> {
			textarea.clear(); // textarea안의 내용을 다 지워요.
			FileChooser chooser = new FileChooser();
			chooser.setTitle("오픈할 파일을 선택하세요");
			// 파일 chooser로부터 오픈할 파일에 대한 reference를 획득
			file = chooser.showOpenDialog(primaryStage);
			// file객체로부터 input stream을 열어요! //** 1.내가 처리할 대상 주체 획득하고 -> 2.Stream열어줘요
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				// 해당 파일에 ~가 여러개 있어요
				String line = "";
				try {
					while ((line = br.readLine()) != null) {
						printMsg(line);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		});
		
		saveBtn = new Button("파일 저장");
		saveBtn.setPrefSize(150, 50);
		saveBtn.setOnAction(t->{
			
			String content = textarea.getText();
			try {
				FileWriter fw = new FileWriter(file);
				fw.write(content);
				fw.close(); //  반드시 close처리를 해줘야 해요!          **Writing하고 close안하면 파일 저장이 안될 수 있어요
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("파일저장");
				alert.setHeaderText("File Save!!");
				alert.setContentText("파일에 내용이 저장되었어요");
				alert.showAndWait();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
		});
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.getChildren().add(openBtn);
		flowpane.getChildren().add(saveBtn);
		root.setBottom(flowpane);
		
		Scene scene = new Scene(root);  //장면을 만들고
		primaryStage.setScene(scene);   // primaryStage를 창에 띄움.
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(); 
	}

}
