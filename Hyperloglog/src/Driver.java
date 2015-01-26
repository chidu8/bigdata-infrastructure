import hll.Hyperloglog;

import java.util.ArrayList;
import java.util.List;

public class Driver {

	public static void main(String[] args) {
		Hyperloglog hllo = new Hyperloglog();
		
		List<String> mylist = new ArrayList<String>();
		
		
		mylist.add("askfjhsadf");
		
		mylist.add("askfjhsad");
		/*
		mylist.add("askfjhsa");
		mylist.add("askfjhs");
		mylist.add("askfjh");
		mylist.add("askfj");
		*/
		
		hllo.offer(mylist);
		
		System.out.println(hllo.cardinality());

	}

}
