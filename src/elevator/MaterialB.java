package elevator;

public class MaterialB extends Material{
	public static int last_spawned_time = 0;
	
	public void addToQueue(int system_current_time) {
		this.arriving_time = system_current_time;
		last_spawned_time = system_current_time;
	}
	
	public MaterialB(){
		type = 2;
		this.weight = 100;
		this.arriving_interval = 6;
	}
}
