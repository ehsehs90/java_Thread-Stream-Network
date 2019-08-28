package javaLambda;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/*
 * 
 * Supplier라고 불리는 함수적 인터페이스 여러 개가 우리에게 제공됨
 * 이 인터페이스의 특징은 매개변수가 없음
 * 대신 return 값이 존재
 * getXXX()라는 method가 추상 메소드 형태로 인터페이스 안에 선언되어 있음
 * 
 * 
 * 친구 목록을 List<String> 형태로 만듦
 */

public class Ex05_LambdaUsingSupplier {

	// 로또 번호 (1~45)를 자동으로 생성하고 출력하는 간단한 method 작성
	public static void generateLotto(IntSupplier intsupplier, Consumer<Integer> consumer) {
		// Set : 중복 배제
		Set<Integer> set = new HashSet<Integer>();
		while (set.size() != 6) {
			set.add(intsupplier.getAsInt());
		}
		for (Integer i : set) {
			consumer.accept(i);
		}
	}

	public static void main(String[] args) {
		// IntSupplier : 정수값을 1개 리턴하는 supplier
		// 로또 번호를 자동으로 생성하고 출력하는 간단한 method를 작성

		// generateLotto(supplier,consumer);
		generateLotto(() -> {
			return (int) (Math.random() * 45 + 1);
		}, t -> System.out.print(t + " "));
	}
}
