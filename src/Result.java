import java.util.ArrayList;

public class Result {
	public GameManager.ResultType type;
	public ArrayList<String> desc;
	
	public Result() {
		type = null;			// 엔딩타입 Happy, sad...
		desc = new ArrayList<String>(); // 각 단과대학별 상태를 담고있는 ArrayList
	}
}
