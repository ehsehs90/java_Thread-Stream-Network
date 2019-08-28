package javaLambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/*
 * Function 함수적 인터페이스는 입력 매개변수와 리턴값이 있는
 * 추상 메소드 applyXXX()이 제공 됨
 * 
 * 일반적으로, 입력 매개변수와 리턴값의 타입이 다른 경우 두 변수를 Mapping 시킬 때 사용 됨
 * 
 * Function <T,R> func = t->{return ... };
 * T : 입력 매개 변수의 generic
 * R : 리턴값의 generic
 */

class Ex06_Student {
	private String sName;
	private int kor;
	private int math;
	private int eng;

	public Ex06_Student() {
	}

	public Ex06_Student(String sName, int kor, int math, int eng) {
		super();
		this.sName = sName;
		this.kor = kor;
		this.math = math;
		this.eng = eng;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

}

public class Ex06_LambdaUsingFunction {
	private static List<Ex06_Student> student = Arrays.asList(new Ex06_Student("홍길동", 50, 54, 13),
			new Ex06_Student("김길동", 30, 41, 80), new Ex06_Student("이순신", 90, 94, 93),
			new Ex06_Student("강감찬", 10, 100, 73));

	private static void printName(Function<Ex06_Student, String> function) {
		for (Ex06_Student s : student) {
			System.out.println(function.apply(s));
		}

	}

//	private static double getAvg(Function<Ex06_Student, Integer> function) {
	private static double getAvg(ToIntFunction<Ex06_Student> function) {
		int sum = 0;
//		if (subject.equals("KOR")) {
//			// 국어평균
//			for (Ex06_Student s : student) {
//				sum += s.getKor();
//			}
//		}
//		if (subject.equals("ENG")) {
//			// 영어평균
//			for (Ex06_Student s : student) {
//				sum += s.getEng();
//			}
//		}
//		if (subject.equals("MATH")) {
//			// 수학평균
//			for (Ex06_Student s : student) {
//				sum += s.getMath();
//			}
//		}
//		for (Ex06_Student s : student) {
//			sum += function.apply(s);
//		}
		for (Ex06_Student s : student) {
			sum += function.applyAsInt(s);
		}
		return (double) sum / student.size();
	}

	public static void main(String[] args) {
		// 학생 이름을 출력
		printName(t -> {
			return t.getsName();
		});

		// getAvg라는 static method를 만들어서 다음의 내용을 출력
		// 학생들의 국어 성적 평균
		// System.out.println("국어 평균 : " + getAvg("KOR"));
		System.out.println("국어 평균 : " + getAvg(t -> t.getKor()));
		// 학생들의 영어 성적 평균
		// System.out.println("영어 평균 : " + getAvg("ENG"));
		System.out.println("영어 평균 : " + getAvg(t -> t.getEng()));
		// 학생들의 수학 성적 평균
		// System.out.println("수학 평균 : " + getAvg("MATH"));
		System.out.println("수학 평균 : " + getAvg(t -> t.getMath()));

	}

}
