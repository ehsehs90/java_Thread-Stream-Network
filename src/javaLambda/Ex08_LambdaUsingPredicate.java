package javaLambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/*
 * Predicate
 * 입력 매개변수 o, return o
 * Function과 Operator과의 차이점: return이 boolean(true/false)
 * 사용되는 method는 testXXX()
 * 
 * 학생 객체를 만들어 List형태로 유지
 * static method를 만들어 lambda식으로 인자 전
 */

class Exam08_StudentVO {
	private String name;
	private int kor;
	private int eng;
	private int math;
	private String gender;

	public Exam08_StudentVO() {
	}

	public Exam08_StudentVO(String name, int kor, int eng, int math, String gender) {
		super();
		this.name = name;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		this.gender = gender;
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

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}

public class Ex08_LambdaUsingPredicate {

	private static List<Exam08_StudentVO> students = Arrays.asList(new Exam08_StudentVO("홍길동", 10, 20, 30, "남자"),
			new Exam08_StudentVO("박길동", 20, 90, 60, "남자"), new Exam08_StudentVO("신사임당", 30, 30, 90, "여자"),
			new Exam08_StudentVO("유관순", 80, 80, 100, "여자"), new Exam08_StudentVO("이순신", 30, 10, 10, "남자"));

	private static double getAvg(Predicate<Exam08_StudentVO> predicate, ToIntFunction<Exam08_StudentVO> func) {
		int sum = 0;
		int count = 0;
		for (Exam08_StudentVO student : students) {
			if (predicate.test(student)) {
				sum += func.applyAsInt(student);
				count++;
			}
		}

		return (double) sum / count;
	}

	public static void main(String[] args) {
		System.out.println("남자 수학 성적 평균: " + getAvg(t -> t.getGender().equals("남자"), t -> t.getMath()));
		System.out.println("여자 영어 성적 평균: " + getAvg(t -> t.getGender().equals("여자"), t -> t.getEng()));
	}
}