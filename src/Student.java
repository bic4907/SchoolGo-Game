
public class Student extends Person {

	private double happyness;
	private double knowledge;

	public Student(String type) {
		this.college = type;
		setHappyness(70);
		setKnowledge(10);
	}

	public double getHappyness() {
		return happyness;
	}

	public void setHappyness(double d) {
		this.happyness = d;
	}

	public double getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(double d) {
		this.knowledge = d;
	}

}
