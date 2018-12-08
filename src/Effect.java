import java.util.HashMap;

public class Effect {
	
	private HashMap<String, Double> knowledge;
	private HashMap<String, Double> happyness;
	private String name;
	private String desc;
	
	public Effect() {
		knowledge = new HashMap<String, Double>();
		happyness = new HashMap<String, Double>();
	
		this.name = "";
		this.desc = "";
		
		for(String type : SceneManager.getInstance().getGameManager().CollegeType) {
			knowledge.put(type, (double)0);
			happyness.put(type, (double)0);
		}
	}

	public Effect(double i, double j, double k, double l, double m, double n, double o, double p, String nm, String d) {
		knowledge = new HashMap<String, Double>();
		happyness = new HashMap<String, Double>();
	
		this.name = nm;
		this.desc = d;
		
		this.knowledge.put("인문대", i);
		this.knowledge.put("자연대", j);
		this.knowledge.put("공과대", k);
		this.knowledge.put("예체능대", l);
		
		this.happyness.put("인문대", m);
		this.happyness.put("자연대", n);
		this.happyness.put("공과대", o);
		this.happyness.put("예체능대", p);
		
	}	
	
	public double getHappyness(String type) {
		return this.happyness.get(type);	
	}	
	public double setHappyness(String type, double v) {
		if(!SceneManager.getInstance().getGameManager().CollegeType.contains(type)) {
			System.out.println("Wrong key");
		}
		return this.happyness.put(type, v);
	}
	public double getKnowledge(String type) {
		return this.knowledge.get(type);	
	}	
	public double setKnowledge(String type, double v) {
		if(!SceneManager.getInstance().getGameManager().CollegeType.contains(type)) {
			System.out.println("Wrong key");
		}
		return this.knowledge.put(type, v);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String str = "";
		
		str += "[Knowledge]";
		str += "인문대 :";
		str += this.knowledge.get("인문대");
		str += "자연대 :";
		str += this.knowledge.get("자연대");
		str += "공과대 :";
		str += this.knowledge.get("공과대");
		str += "예체능대 :";
		str += this.knowledge.get("예체능대");
		
		str += "[Happyness]";
		str += "인문대 :";
		str += this.happyness.get("인문대");
		str += "자연대 :";
		str += this.happyness.get("자연대");
		str += "공과대 :";
		str += this.happyness.get("공과대");
		str += "예체능대 :";
		str += this.happyness.get("예체능대");
		
		return str;
		
	}
	
}
