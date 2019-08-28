package javaStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * reduction
 * 
 * =>대량의 데이터를 가공해서 축소하는 개념
 * => sum, average, count, max, min
 * 
 * Collection 사용할 때 Stream 을 이용해서 이런 reduction 작업을 쉽게 할 수 있어요!
 * 
 * 만약 Collection  안에 reduction 하기가 쉽지 않은 형태로 데이터가 들어가 있으면
 * 중간 처리과정을 거쳐서 reduction 하기 좋은 형태로 변환.
 * 
 * Stream 은 pipeline을 지원(stream 을 연결해서 사용할 수 있어요)
 * 
 * 간단한 예제를 통해서 이해해 보아요!
 * 직원 객체를 생성해서 ArrayList안에 여러명의 직원을 저장.
 * 이 직원중에 IT에 종사하고 남자인 직원을 추려서 해당 직원들의 연봉 평균을
 * 출력!
 */

class Exam03_Employee implements Comparable<Exam03_Employee> {

	private String name; // 이름
	private int age; // 나이
	private String dept; // 부서
	private String gender; // 성별
	private int salary; // 연봉

	public Exam03_Employee(String name, int age, String dept, String gender, int salary) {
		super();
		this.name = name;
		this.age = age;
		this.dept = dept;
		this.gender = gender;
		this.salary = salary;
	}

	public Exam03_Employee() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
	// 만약 overriding 을 하지 않으면 메모리 주소 가지고 비교.(기본 기능)

	// 내가 이 메소드를 원하는 방식으로 overriding을 해서 특정 조건을 만족하면
	// 객체가 같아! 라는 식으로 작성해 보아요!
	@Override
	public boolean equals(Object obj) {

		boolean result = false;
		Exam03_Employee target = (Exam03_Employee) obj; // 일단 캐스팅!
		if (this.getName().equals(target.getName())) {
			result = true;
		} else {
			result = false; // 객체의 이름 같으면 같은객체야! 이름 다르면 다른객체야! 나는 이렇게 메소드를 바꿨어요
		} // 만약 현재객체가 갖고 잇는 getName이 오브젝트가 갖고 있는 getName과 같다면?
		return result;
	}

	@Override
	public int compareTo(Exam03_Employee o) { // sorted 사용하려면 이거 무조건 구현해야해요
		// 이 친구는 정수값을 리턴해요
		// 양수가 리턴되면 순서를 바꿔요
		// 0이나 음수가 리턴되면 순서를 바꾸지 않아요!
		int result = 0;
		// 현재 이 메서드를 호출하는 객체 는 this객체.
		// 들어오는 객체
		// 이렇게 2개의 employee객체를 가지고 비교할거에요
		if (this.getSalary() > o.getSalary()) {
			result = 1; // 양수를 리턴 . 오름차순으로 정렬할건데 큰게 앞에잇으면 뒤롤보내라는 듰

		} else if (this.getSalary() == o.getSalary()) {
			result = 0;
		} else {
			result = -1;
		}
		return result; // 이 코드가 말하는것 : salary 값이 큰것이 뒤로가요
	}

}

public class Ex03_StreamPipeline {

	private static List<Exam03_Employee> employees = Arrays.asList(new Exam03_Employee("홍길동", 20, "IT", "남자", 1000),
			new Exam03_Employee("김길동", 30, "Sales", "여자", 3000), new Exam03_Employee("최길동", 40, "IT", "남자", 1000),
			new Exam03_Employee("이길동", 50, "Sales", "남자", 1000), new Exam03_Employee("도길동", 60, "IT", "남자", 3300),
			new Exam03_Employee("현길동", 50, "Sales", "여자", 2000), new Exam03_Employee("오길동", 40, "IT", "남자", 5500),
			new Exam03_Employee("서길동", 70, "Sales", "남자", 5000), new Exam03_Employee("애길동", 60, "IT", "남자", 6500),
			new Exam03_Employee("황길동", 40, "Sales", "여자", 1000), new Exam03_Employee("이길동", 50, "Sales", "남자", 1000));

	public static void main(String[] args) {
		// 부서가 IT인 사람들 중 남자에 대한 연봉 평균을 구해보세요!
		Stream<Exam03_Employee> stream = employees.stream();
		// Stream의 중간처리와 최종 처리를 이용해서 원하는 작업을 해 보아요!
		// filter method는 결과값을 가지고 있는 stream 을 리턴.

		double avg = stream.filter(t -> t.getDept().equals("IT")).filter(t -> t.getGender().equals("남자"))
				.mapToInt(t -> t.getSalary()) // 여기까지 중간처리
				.average().getAsDouble(); // 최종처리 - average()얘가 실행되기 전 까지 중간처리는 되지않아요

		System.out.println("IT부서의 남자 평균 연봉 : " + avg);

		// Stream은 실행되려면 일단 최종처리(reduction)의 존재(.average())를 먼저 확인한 다음에 그때서야 중간처리를 해요.
		// lazy처리라고도 해요!

		// 그럼 Stream이 가지고 있는 method는 무엇이 있나요?
		// 나이가 35 이상인 직원 중 남자 직원의 이름을 출력하세요
//		              stream.filter(t-> (t.getAge() >=35) )                //35살이상인 사람들의 객체만 걸러서 새로운 스트림만들어.
//							.filter(t->t.getGender().equals("남자")) 
//							.forEach(t->System.out.println(t.getName()));   //안에 객체들이 들어가있을건데 그거의 이름(들)을 뽑아낼거니까 foreach써서 뽑아

		// 중복 제거를 위한 중간처리
//		int temp[] = {10,20,30,40,50,30,40};
//		IntStream s = Arrays.stream(temp);
//		s.distinct().forEach(t->System.out.print(t+" "));

//		 객체에 대한 중복제거를 해 보아요!
//		 VO안에서 equals() method를 overriding해서 처리
//		employees.stream().distinct().forEach(t -> System.out.println(t.getName())); // 스트림이용. 중복제거 해서 이름찍겠어

		// mapToInt() -> mapXX()
		// 정렬 (부서가 IT인 사람을 연봉순으로 출력 )

		employees.stream().filter(t -> t.getDept().equals("IT")) // sorted - 객체는 그 자체로 정렬이 될수 없어요. sorted를 이용해서 객체를 정렬하고
																	// 싶으면 객체를 어떤 방식을 정렬할건지에 대해 말해줘야해요
				// 반드시 comparable 이라는 인터페이스를 구현해서 객체를 어떻게 정렬할건지에 대해 기능구현을 해줘야해요. // vo class를
				// 정렬이 가능한 형태의 class로 지정해줘야해요.
				.distinct().sorted(Comparator.reverseOrder()) // 오름차순 정렬
				.forEach(t -> System.out.println(t.getName() + "," + t.getSalary()));

		// 반복
		// forEach()를 이용하려면 스트림 안의 요소를 반복할 수 있어요!
		// forEach()는 최종처리함수
		// 그래서 이렇게 쓸 수 없어요!

		// 불편해요!
		// 중간처리 함수로 반복처리 하는 함수가 하나 더 있어요 (forEach 대용)
		// 반복처리를 중간에 한번 해야한다 -> peek을 써용

		// 결론 : 반복은 2개 있다. foreach 와 peek

//	employees.stream()
//	         .peek(t->System.out.println(t.getName()))
//	         .mapToInt(t->t.getSalary())
//	         .forEach(t->System.out.println(t));
//		

		// 확인용 최종 처리 함수 => predicate 를 이용해서 boolean으로 return
		// 50살 이상인 사람만 추출해서 이름 출력

//	boolean result = employees.stream()
//			 .filter(t->(t.getAge() >=50))
//	//		 .allMatch(t->(t.getAge()>55)); //다 매치되면 true가 리턴/ 그렇지 않으면 false리턴
//	//       .anyMatch(t->(t.getAge()>55));
//	System.out.println(result);
//	

//	 최종 확인용 함수로 forEach를 많이 사용했는데
//	 forEach말고  collect()를 이용해 보아요!
//	 나이가 50살 이상인 사람들의 연봉을 구해서
//	 List<Integer>형태의 ArrayList에 저장해보아요!	
		
		List<Integer> tmp = employees.stream()
				.map(t -> t.getSalary()) // 그 객체를 숫자로 바꿔(연봉) -> 연봉에 대한 숫자만 뽕뽕 스트링에있음
				.collect(Collectors.toList());
		System.out.println(tmp);

		
		
		// 당연히 set으로도 저장할 수 있고요 Map으로도 저장할 수 있어요!
//	Set<Integer> tmp = employees.stream()
//			.filter(t->(t.getAge()>= 50))  
//			.map(t->t.getSalary())   
//			//.collect(Collectors.toList());
//			.collect(Collectors.toCollection(HashSet:: new));
//	
//	System.out.println(tmp);	
//	
//	}

	}
}
