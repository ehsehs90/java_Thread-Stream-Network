package javaLambda;

import java.util.function.IntBinaryOperator;

/*
 * Operator는 function과 하는 일이 거의 비슷함
 * 입력 매개변수가 있고 리턴값이 있음
 * 
 * Function은 Mapping 용도로 많이 사용 됨
 * -> 입력 매개변수를 다른 타입의 리턴 타입으로 변환, 매핑하는 형태
 * 
 * Operator는 연산 용도로 많이 사용됨
 * -> 입력 매개변수를 연산에 이용하여 같은 타입의 리턴값을 돌려주는 형태 
 * 
 * 최댓값과 최솟값을 구하는 static method를 하나 작성
 * 
 * 
 */
public class Ex07_LambdaUsingOperator {
	private static int arr[] = { 100, 92, 50, 89, 34, 27, 99, 3 };

	// getMaxMin()을 static method로 만들자
	// 사용하는 Operator는 IntBinaryOperator
	private static int getMaxMin(IntBinaryOperator operator) {
		int result = arr[0];
		for (int k : arr) {
			result = operator.applyAsInt(result, k);
		}
		return result;

	}

	public static void main(String[] args) {
		// getMaxMin("MAX");// 최댓값 구하는 method 호출
		// getMaxMin("MIN");// 최솟값 구하는 method 호출
		// 이렇게 하지 말고 Operator를 이용해서 처리하기!

		// 최댓값
		System.out.println("최댓값 : " + getMaxMin((a, b) -> {
			return a >= b ? a : b;
		}));

		// 최솟값
		System.out.println("최솟값 : " + getMaxMin((a, b) -> {
			return a < b ? a : b;
		}));

	}

}
