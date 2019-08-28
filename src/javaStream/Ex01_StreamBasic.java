package javaStream;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/* 
 * Stream은 Java 8에서 도입이 됨
 * 주의해야 할 점은 java.io 안에 있는 Stream과 다른 것
 * 
 * 사용 용도 : 컬렉션 처리 (List, Set, Map, ...)를 위해서 사용 됨
 * 		   컬렉션 안의 데터를 반복시키는 반복자의 역할을 하는 게 Stream
 * 		   예를 들어 ArrayList 안에 학생 객체가 5개 있으면 그 5개를 하나씩 가져오는 역할을 수행
 * 		 -> 이렇게 가져온 데이터를 람다식을 이용해서 처리할 수 있음
 * 
 * 
 * 
 */

class Ex01_Student {
	private String name;
	private int kor;
	private int eng;

	public Ex01_Student() {
		super();
	}

	public Ex01_Student(String name, int kor, int eng) {
		super();
		this.name = name;
		this.kor = kor;
		this.eng = eng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

}

public class Ex01_StreamBasic {

	private static List<String> myBuddy = Arrays.asList("홍길동", "김길동", "이길동", "오길동");
	private static List<Ex01_Student> student = Arrays.asList(new Ex01_Student("홍길동", 15, 20),
			new Ex01_Student("김길동", 43, 20), new Ex01_Student("오길동", 50, 30));

	public static void main(String[] args) {
		// 사람 이름 출력하기
		// 방법 1. 일반 for문(첨자 사용)을 이용해서 처리 -> 효율이 가장 낮은 방법
		for (int i = 0; i < myBuddy.size(); i++)
			System.out.println(myBuddy.get(i));

		// 방법 2. 반복자를 이용해서 처리
		// 첨자를 이용한 반복을 피하기 위해 Iterator 사용
		Iterator<String> iter = myBuddy.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}

		// 방법 3. 반복자가 필요 없음, 내부 반복자 이용
		// 병렬 처리가 가능
		Consumer<String> consumer = t -> {
			System.out.println(t + ", " + Thread.currentThread().getName());
		};

		Stream<String> stream = myBuddy.parallelStream();
		stream.forEach(consumer);

		Stream<Ex01_Student> studeStream = student.stream();

		// t -> t.getKor()).average() : OptionalDouble type
		// getAsDouble()을 이용해 실수 형태로 바꿔줌
		double avg = studeStream.mapToInt(t -> t.getKor()).average().getAsDouble();
		System.out.println(avg);
	}

}
