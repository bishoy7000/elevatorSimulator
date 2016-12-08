package elevator;

import java.util.concurrent.ThreadLocalRandom;

public class MaterialA extends Material {
	//number of objects of the same material in the queue
	public static int last_spawned_time = 0;

	public void addToQueue(int system_current_time) {
		this.arriving_time = system_current_time;
		last_spawned_time = system_current_time;
	}
	
	public MaterialA(){
		type = 1;
		this.weight = 200;
//		//estimate an arrival time between 3, 7  (5 give or take 2)
		this.arriving_interval = ThreadLocalRandom.current().nextInt(3, 8);
	}
}
