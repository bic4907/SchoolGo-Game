import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class GameManager {

	private int nMonth, nDay, nHumanCount, nRandomDay;
	private long nMoney, lastIncome;

	private Vector<Student> nStudent;
	private Vector<Professor> nProfessor;
	private boolean flag_scouting;
	
	private Vector<Building> buildingList;
	private Vector<Building> nBuilding;
	public Vector<String> CollegeType;
	
	private Vector<HumanWalking> humanList;
	private int randomList[];
	private boolean isOccur;
	
	public enum PersonType {
		Student, Professor
	}
	
	public enum ResultType {
		Perfect, OK, Inmun, Jayon, Gonggwa, Yeche, Fail, Pasan
		// Perfect : 3개 이상의 단과대학이 기준치를 넘었을 경우
		// OK	   : 2개 이상의 단과대학이 기준치를 넘었을 경우
		// Imnum   : 인문대만 기준치를 넘었을 경우
		// Jayon   : 자연대만 기준치를 넘었을 경우
		// Gonggwa : 공과대만 기준치를 넘었을 경우
		// Yeche   : 예체능만 기준치를 넘었을 경우
		// Fail    : 기준치를 넘은 대학이 0개일 경우
	    // Pasan   : 재정이 부족하여 게임이 강제종료 되었을 때
	}
	
	
	public GameManager() {
		// 2월 1일부터 게임시작
		nMonth = 2;
		nDay = 0;
		nMoney = 1000;
		lastIncome = 0;
		isOccur = false;
		nRandomDay = (int)(Math.random() * 19) + 5;
		
		buildingList = new Vector<Building>();
		humanList = new Vector<HumanWalking>(); // humanList
		randomList = new int[7];
		for(int i=0;i<7;i++)
			randomList[i] = -1;
		
		/* 빌딩 추가하는 곳*/
		{
			Building b = new Building("율곡관", 300000, 80, new Point(125, 75), "res/building/g1.png");
			Effect e = new Effect(0, 1.0, 0, 0, 0, 0, 0.2, 0, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("이노베이션센터", 1000000, 150, new Point(225, 50), "res/building/g2.png");
			Effect e = new Effect(0, 0, 1.6, 0, 0, 0, 0.8, 0, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("영실관", 300000, 50, new Point(475, 50), "res/building/j1.png");
			Effect e = new Effect(0, 1.0, 1.0, 0, 0, 0.3, 0.3, 0, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("충무관", 300000, 50, new Point(600, 45), "res/building/j2.png");
			Effect e = new Effect(0, 1.0, 1.0, 0, 0, 0.3, 0.3, 0, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("광개토관", 800000, 120, new Point(100, 260), "res/building/i1.png");
			Effect e = new Effect(0.8, 0.8, 0.8, 0.8, 0.3, 0.1, 0.1, 0.1, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("집현관", 300000, 50, new Point(140, 180), "res/building/i2.png");
			Effect e = new Effect(0.8, 0, 0, 0, 0.8, 0, 0, 0, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("용덕관", 350000, 50, new Point(540, 180), "res/building/y1.png");
			Effect e = new Effect(0, 0, 0, 0.8, 0, 0, 0, 0.3, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("진관홀", 300000, 50, new Point(600, 280), "res/building/y2.png");
			Effect e = new Effect(0, 0, 0, 0, 0.4, 0.5, 0.5, 0.4, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("기숙사", 200000, 50, new Point(350, 50), "res/building/p2.png");
			Effect e = new Effect(0, 0, 0, 0, 0.2, 0.2, 0.2, 0.2, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("CU", 100000, 20, new Point(410, 103), "res/building/p4.png");
			Effect e = new Effect(0, 0, 0, 0, 0.3, 0.3, 0.3, 0.3, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("체육관", 200000, 10, new Point(280, 160), "res/building/p3.png");
			Effect e = new Effect(0, 0, 0, 0.5, 0, 0, 0, 0.5, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		{
			Building b = new Building("비원", 100000, 10, new Point(380, 210), "res/building/p1.png");
			Effect e = new Effect(-0.8, -0.8, -0.8, -0.8, 1, 1, 1, 1, "", "");
			b.addEffect(e);
			buildingList.add(b);
		}
		/* 빌딩 추가 하는 곳 끝 */

		nBuilding = new Vector<Building>();
		nBuilding.add(new Building("컨테이너", 1, 50, new Point(188, 330), "res/building/container.png"));
		
		nStudent = new Vector<Student>();
		nProfessor = new Vector<Professor>();
		nProfessor.add(new Professor("Natural"));
		
		this.CollegeType = new Vector<String>();
		this.CollegeType.add("인문대");
		this.CollegeType.add("자연대");
		this.CollegeType.add("공과대");
		this.CollegeType.add("예체능대");
		

	} 
	
	public long getMoney(){return nMoney;}
	public void setMoney(long Money) {nMoney = Money;}
	public int[] getRandomList() {return randomList;}

	// get/set method
	
	public void nextDay() {
		this.nDay++;
		if(this.nDay > 30) {
			this.nDay = 1;
			this.nMonth++;
			this.isOccur = false; //이번 달에는 아직 랜덤 이벤트 발생하지 않았음
			this.nRandomDay = (int)(Math.random() * 19) + 5; //달이 바뀌면 랜덤 이벤트 발생 날짜를 다시 설정
		}
		nextStep();
	}
	
	public void nextStep() {
		int money = 0;
		
		/* 돈 관련 */
		int num = this.nStudent.size();
		money += num * 300;
		num = this.nProfessor.size();
		money += num * -1000;
		num = this.nBuilding.size() - 1;
		money += num * -5000;
		this.lastIncome = money;
		this.nMoney += money;
		
		/* 행복도 관련 */
		for(Student s : this.nStudent) {
			if(s.getHappyness() == 0) continue;
			s.setHappyness(s.getHappyness() - 0.1);
			if(s.getHappyness() < 0) s.setHappyness(0);
		}
		/* 지식 관련 */
		
		HashMap<String, Integer> cntProfessor = new HashMap<String, Integer>();
		for(String type : this.CollegeType) {
			cntProfessor.put(type, 1);
		}
		
		for(Professor f : this.nProfessor) {
			if(f.getCollege() == "Natural") continue;
			
			cntProfessor.put(f.getCollege(), cntProfessor.get(f.college) + 1);
		}
		
		for(Student s : this.nStudent) {
			s.setKnowledge(s.getKnowledge() + (cntProfessor.get(s.college) / 30));
			if(s.getKnowledge() < 0) s.setKnowledge(0);
		}	
		
		
		
		Effect totalEffect = new Effect();
		/* 건물 효과 관련 */
		for(Building b : this.nBuilding) {
			
			Vector<Effect> b_e = b.getEffects();
			
			
			for(Effect e : b_e) {
				
				for(String type : this.CollegeType) {
					
					totalEffect.setHappyness(type,
							totalEffect.getHappyness(type) +
							e.getHappyness(type)
							);
					totalEffect.setKnowledge(type,
							totalEffect.getKnowledge(type) +
							e.getKnowledge(type)
							);
				}
				
			}
		}

		for(Student s : this.nStudent) {
			s.setHappyness(s.getHappyness() + totalEffect.getHappyness(s.getCollege()));
			s.setKnowledge(s.getKnowledge() + totalEffect.getKnowledge(s.getCollege()));
		}	
		
		
		
		
		if(nMoney <= -500000) {
			SceneManager.getInstance().changeState(SceneManager.SceneType.RESULT);
		}

	}
	
	
	public int getMonth() {
		return this.nMonth;
	}
	public int getDay() {
		return this.nDay;
	}


	public Vector<Building> getBuildingList() {
		return this.buildingList;
	}

	public boolean buyBuilding(String name) {
		// TODO 돈체크 후 건물구입
		
		
		for(Building b : buildingList) {
			if(b.getName() == name) {
				if(b.getPrice() <= this.nMoney) {
					nBuilding.add(new Building(b));
					this.nMoney -= b.getPrice();
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public Vector<Building> getMyBuilding() {
		return this.nBuilding;
	}


	public void scoutPerson(PersonType type, String text) {
		if(type == PersonType.Student) {
			Random r = new Random();
			int num = r.nextInt(20) + 20;
			
			int allCount = this.getStudentCount();
			int capacity = this.getAllCapacity();
			int margin = 0;
			
			if(allCount + num > capacity) { // 학생이 넘쳐서 돌아가야 하는 경우
				margin = allCount + num - capacity;
				num = capacity - allCount;
			}
			
			for(int i = 0; i < num; i++) {
				this.nStudent.add(new Student(text));
			}
			
			if(num == 0){
				SceneManager.getInstance().addAlert(new AlertDialog("정원이 꽉찼습니다"));
			} // 정원이 꽉 찼을 때
			else if(margin > 0) {
				SceneManager.getInstance().addAlert(new AlertDialog(text + " " + Integer.toString(num) + "명을 뽑았습니다"));
				SceneManager.getInstance().addAlert(new AlertDialog(Integer.toString(margin) + "명이 자리가 없어서 돌아감"));
			} // 정원이 남았지만 더 많이 모았을 때
			else
			{
				SceneManager.getInstance().addAlert(new AlertDialog(text + " " + Integer.toString(num) + "명을 뽑았습니다"));
				//휴먼리스트 추가
			} // 정상적으로 모집했을 때
			
			if( ((allCount+num) / 30) > nHumanCount ) // 30명마다 사람 1명 추가
			{
				HumanWalking hw = new HumanWalking(nHumanCount);
				humanList.add(hw);
			
				nHumanCount++;
			} // if
		} else if(type == PersonType.Professor) {
			Random r = new Random();
			int num = r.nextInt(3) + 5;
			

			for(int i = 0; i < num; i++) {
				this.nProfessor.add(new Professor(text));
			}
			
			SceneManager.getInstance().addAlert(new AlertDialog(text + " 교수 " + Integer.toString(num) + "명을 뽑았습니다"));

		}
	}


	public boolean getScouting() {
		return flag_scouting;
	}

	public void setScouting(boolean flag_scouting) {
		this.flag_scouting = flag_scouting;
	}

	public int getStudentCount() {
		return this.nStudent.size();
	}
	public int getProfessorCount() {
		return this.nProfessor.size();
	}
	
	public int getAllCapacity() {
		int cnt = 0;
		for(Building b : this.nBuilding) {
			cnt += b.getCapacity();
		}
		cnt -= this.nProfessor.size();
		return cnt;
		
	}

	public long getLastIncome() {
		return this.lastIncome;
	}
	
	public void applyEffect(Effect e) {
		
		for(Student s : this.nStudent) {
			s.setHappyness(s.getHappyness() + e.getHappyness(s.getCollege()));
			s.setKnowledge(s.getKnowledge() + e.getKnowledge(s.getCollege()));
		}

	}
	
	public Result getResult() {
		// Perfect : 3개 이상의 단과대학이 기준치를 넘었을 경우
		// OK	   : 2개 이상의 단과대학이 기준치를 넘었을 경우
		// Imnum   : 인문대만 기준치를 넘었을 경우
		// Jayon   : 자연대만 기준치를 넘었을 경우
		// Gonggwa : 공과대만 기준치를 넘었을 경우
		// Yeche   : 예체능만 기준치를 넘었을 경우
		// Fail    : 기준치를 넘은 대학이 0개일 경우
	    // Pasan   : 재정이 부족하여 게임이 강제종료 되었을 때
		HashMap<String, Double> avg_knowledge, avg_happyness;
		HashMap<String, Boolean> passed = new HashMap<String, Boolean>();
		HashMap<String, Integer> cnt_student = new HashMap<String, Integer>();
		avg_knowledge = new HashMap<String, Double>();
		avg_happyness = new HashMap<String, Double>();
		
		// 1. 변수 초기화
		for(String s : this.CollegeType) {
			avg_knowledge.put(s, (double)0);
			avg_happyness.put(s, (double)0);
			passed.put(s, false);
			cnt_student.put(s, 1);
		}
		
		// 2. 전체 합 구하기
		for(Student s : nStudent) {
			avg_knowledge.put(s.getCollege(), avg_knowledge.get(s.getCollege()) + s.getKnowledge());
			avg_happyness.put(s.getCollege(), avg_happyness.get(s.getCollege()) + s.getHappyness());
			cnt_student.put(s.getCollege(), cnt_student.get(s.getCollege()) + 1);
		}
		
		// 3. 평균 구하기
		int cnt_pass = 0;
		for(String s : this.CollegeType) {

			avg_knowledge.put(s, avg_knowledge.get(s) / cnt_student.get(s));
			avg_happyness.put(s, avg_happyness.get(s) / cnt_student.get(s));
			System.out.println(s + "/[Knowledge] " + avg_knowledge.get(s) + "/[Happyness] " + avg_happyness.get(s));			
			if(avg_knowledge.get(s) >= 60.0 && avg_happyness.get(s) >= 70.0) {
				cnt_pass++;
				passed.put(s, true);
			}
		}
		
		// 4. 결과 나누기
		
		Result result = new Result();
		
		if(this.nMoney > -500000) {
			if(cnt_pass >= 3) {
				// Perfect
				result.type = ResultType.Perfect;
			} else if(cnt_pass == 2) {
				// 그냥 잘했을 경우
				result.type = ResultType.OK;
			} else if(cnt_pass == 1) {
				// 한가지만 잘햇을 경우
				if(passed.get("인문대") == true) {
					result.type = ResultType.Inmun;
				} else if(passed.get("자연대") == true) {
					result.type = ResultType.Jayon;
				} else if(passed.get("공과대") == true) {
					result.type = ResultType.Gonggwa;
				} else if(passed.get("예체능대") == true) {
					result.type = ResultType.Yeche;
				}
	
			} else {
				// Fail
				result.type = ResultType.Fail;
			}
			
			// 상세결과 추가
			
			for(String s : this.CollegeType) {
				String college_desc = "";
				
				college_desc = s;
				
<<<<<<< HEAD
				college_desc += "  ���ļ��� ";
				if(avg_knowledge.get(s) >= 50) {
					college_desc += "���� ";
=======
				college_desc += "  지식수준 ";
				if(avg_knowledge.get(s) >= 50) {
					college_desc += "높음 ";
>>>>>>> 8ad9be3f68010f0913864f35d0c293449982986c
				} else {
					college_desc += "낮음 ";
				}
				
<<<<<<< HEAD
				college_desc += "�ູ�� ";
				if(avg_happyness.get(s) >= 70) {
					college_desc += "���� ";
=======
				college_desc += "행복도 ";
				if(avg_happyness.get(s) >= 70) {
					college_desc += "높음 ";
>>>>>>> 8ad9be3f68010f0913864f35d0c293449982986c
				} else {
					college_desc += "낮음 ";
				}
								
				result.desc.add(college_desc);
			}
		} else {
			result.type = ResultType.Pasan;
		}
	
		return result;
	}
	
	public Vector<HumanWalking> getMyHuman(){
		return this.humanList;
	}

	public boolean getOccur() {
		return isOccur;
	}

	public void setOccur(boolean isOccur) {
		this.isOccur = isOccur;
	}

	public int getRandomDay() {
		return nRandomDay;
	}

	public void setRandomDay(int nRandomDay) {
		this.nRandomDay = nRandomDay;
	}	
	
	
	
}
