package javaStream;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * java.util.stream package 안에 사용할 수 있는 stream이 존재
 * BaseStream으로부터 상속 받아서 몇 몇개의 Stream이 존재
 * 
 * Stream : 해당 Stream 안에 객체가 들어가 있는 경우
 * IntStream :해당 Stream 안에 int 값이 들어가 있는 경우
 * LongStream
 * DoubleStream
 * 
 * 여러 가지 형태의 다양한 source에서 Stream을 얻어낼 수 있음
 * 
 */
public class Ex02_StreamSource {

	private static List<String> names = Arrays.asList("홍길동", "김길동", "최길동");
	private static int myArr[] = { 10, 20, 30, 40, 50 };

	public static void main(String[] args) {

		// List로부터 Stream을 생성할 수 있음
		Stream<String> stream1 = names.stream();

		// 배열로부터 stream으로 만들 때는
		// Arrays.stream() method를 이용
		IntStream stream2 = Arrays.stream(myArr);
		System.out.println(stream2.sum());

		// 정수형 숫자 영역을 이용해서 Stream을 생성할 수 있음
		IntStream stream3 = IntStream.rangeClosed(1, 10);

		// 파일로부터 Stream을 생성할 수 있음
		// Path 객체 : file을 찾아서 객체화 시키는 class
		Path path = Paths.get("asset/readme.txt");
		// File 객체 (java.io)와 유사한 java.nio에 포함된 class가 Path

		try {
			// Files class의 static method lines()를 이용해서 stream 생성
			Stream<String> stream4 = Files.lines(path, Charset.forName("UTF-8"));
			stream4.forEach(t -> System.out.println(t));
			stream4.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
