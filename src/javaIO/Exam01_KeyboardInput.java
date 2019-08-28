package javaIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exam01_KeyboardInput {
	
//	﻿
//	표준입력(keyboard)로 부터 입력을 받기 위해 keyboard와
//
//	연결된 stream객체가 필요
//
//	Java는 표준 입력에 대한 Stream을 기본적으로 제공
//
//	System.in(InputStream)
//
//	Stream은 이렇게 inputStream과 OutputStream으로 구분
//
//	byteStream(기본 데이터형) 과 reader, writer 계열 stream으로 구분

		public static void main(String[] args) {

		System.out.println("키보드로 한줄을 입력해요");
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);                  //조금 더 나은 stream을 만들어요:bufferedReader
		 
		try {
			String input = br.readLine();                               ///blocking메소드인ㅇ readLine();
			System.out.println("입력 받은 데이터  :  "+ input);
		}catch(IOException e){
			e.printStackTrace();
		}
				}

}
