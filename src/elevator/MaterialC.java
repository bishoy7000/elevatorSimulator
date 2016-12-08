package elevator;

public class MaterialC extends Material{
	public static int last_spawned_time = 0;
	
	public void addToQueue(int system_current_time) {
		this.arriving_time = system_current_time;
		last_spawned_time = system_current_time;
	}
	
	public MaterialC(){
		type = 3;
		this.weight = 50;
		double r = Math.random();
		if(r < .67){  //67% chance to arrive in 3 mins
			this.arriving_interval = 3;
			}
			else{  //33% chance to arrive in 2 mins
				this.arriving_interval = 2;
			}
	}
}
