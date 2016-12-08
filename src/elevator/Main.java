package elevator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Main {
	public static final int RUN_TIME 	= 200;
	public static final int MAX_WEIGHT 	= 400;
	
	//the delay between arriving in queue and the moment the elevator leaves the floor
	//so delay equals (arriving_time + waited_time in the elevator till it's full)
	public  int delayA = 0;
	public  int delayB = 0;
	public  int delayC = 0;

	//number of mats that were actually loaded and transfered to the second floor
	public  int traveledMatA = 0;
	public  int traveledMatB = 0;
	public  int traveledMatC = 0;
	
	public int ideal_time = 0;
	final XYSeries ChartMaterialA = new XYSeries( "MaterialA" );
	final XYSeries ChartMaterialB = new XYSeries( "MaterialB" );
	final XYSeries ChartMaterialC = new XYSeries( "MaterialC" );

	public  void launchSimulation() throws IOException {
		//Initialize Line Chart of Delay
		final XYSeriesCollection dataset = new XYSeriesCollection( );
	      dataset.addSeries( ChartMaterialA );
	      dataset.addSeries( ChartMaterialB );
	      dataset.addSeries( ChartMaterialC );
	    //Initialize Material Queue
		Queue<Material> matsQue = arrivingQueue();
		//Start Operating on the queue and transferring materials when required
		elevator(matsQue);
		//create Ideal Percentage Chart
		create_ideal_pie_chart();
		//Create Delaychart
		createDelayChart(dataset);
	}
	
	public void setVariable(){
		this.delayA =10;
	}
	//Initialize queue
	public  Queue<Material> arrivingQueue() {
		Queue<Material> matsQueue = new ArrayDeque<>();
		for (int current_time = 1; current_time <= RUN_TIME; current_time++) {
			MaterialA matA = new MaterialA();
			MaterialB matB = new MaterialB();
			MaterialC matC = new MaterialC();
			
			/* Calculate the time between the last time a material arrived and current one
			 So we know if to push the same material again or not*/
			if (current_time - matA.last_spawned_time >= matA.arriving_interval) {
				//add the material to the queue
				matsQueue.add(matA);
				//record time of entering the system and increasing the number of objects
				//of the same material inside the system
				matA.addToQueue(current_time);
			}
			if (current_time - matB.last_spawned_time >= matB.arriving_interval) {
				matsQueue.add(matB);
				matB.addToQueue(current_time);
			}
			if (current_time - matC.last_spawned_time >= matC.arriving_interval) {
				matsQueue.add(matC);
				matC.addToQueue(current_time);
			}
		}
		MaterialA.last_spawned_time =0;
		MaterialB.last_spawned_time =0;
		MaterialC.last_spawned_time =0;
		return matsQueue;
	}
	
	public  void elevator(Queue<Material> matsQue) {
		//array to carry loaded materials to the elevator
		Queue<Material> loadedMaterial = new ArrayDeque<>();
		int currentElevatorWeight = 0;
		timeloop:
		for (int current_time = 1; current_time <= RUN_TIME && !matsQue.isEmpty();) {
			//calculate the total ideal time of the elevator where the elevator transfered all the materials currently in the queue
			if (current_time <= matsQue.element().arriving_time) {
				this.ideal_time++;
			}
			//iterate thought the que each min to get all the materials that came in this time
			for (Material material : matsQue) {
				if (currentElevatorWeight + material.weight <= MAX_WEIGHT
						&& current_time >= material.arriving_time)
				{
					currentElevatorWeight += material.weight;
					loadedMaterial.add(material);
					matsQue.remove(material);
				}
				if (currentElevatorWeight == MAX_WEIGHT) {
					elevatorTransfer(loadedMaterial, current_time);
					//the total time the elevator takes to go up -> unload -> go back
					current_time += 4;
					currentElevatorWeight = 0;
					loadedMaterial.clear();
					//the elevator is full so we breach to the time loop to continue loading
					//when the elevator is back
					continue timeloop;
				}
			}
			 current_time++;
		}
	}
	
	public void elevatorTransfer(Queue<Material> loadedMaterial, int current_time){
		for (Material material : loadedMaterial) {
			switch (material.weight) {
			case 200:
				this.delayA += current_time - material.arriving_time;
				this.traveledMatA ++;
			    ChartMaterialA.add(current_time,delayA);
				break;
			case 100:
				this.delayB += current_time - material.arriving_time;
				this.traveledMatB ++;
			    ChartMaterialB.add(current_time,delayB);
				break;
			case 50:
				this.delayC += current_time - material.arriving_time;
				this.traveledMatC ++;
			    ChartMaterialC.add(current_time,delayC);
			    break;
			}
		}
	}
	
	public void create_ideal_pie_chart() throws IOException{
		DefaultPieDataset dataset = new DefaultPieDataset( );
		//calculate the percentage where the elevator worked perfectly
		double perfectPercentage = (ideal_time*100.0f/RUN_TIME);
		double nonIdeal = 100 - perfectPercentage;
		//sets the pie sections
	      dataset.setValue("Ideal Percentage "+perfectPercentage+"%", new Double(perfectPercentage));
	      dataset.setValue("Not Ideal "+nonIdeal+"%", new Double( 100 - perfectPercentage ) );

	      JFreeChart chart = ChartFactory.createPieChart(
	         "Elevator Ideal Time Percentage", // chart title
	         dataset,true,true,false);
	         
	      int width = 422; /* Width of the image */
	      int height = 318; /* Height of the image */ 
	      File pieChart = new File( "IdealPercentage.jpeg" ); 
	      ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
	}
	
	public void createDelayChart(XYSeriesCollection dataset) throws IOException {
	      JFreeChart xylineChart = ChartFactory.createXYLineChart(
	         "Material Delay Time", 
	         "Run Time",
	         "Delay", 
	         dataset,
	         PlotOrientation.VERTICAL, 
	         true, true, false);
	      XYPlot plot = (XYPlot) xylineChart.getPlot();
	      plot.getRenderer().setSeriesPaint(0, Color.BLACK);	      
	      int width = 422; /* Width of the image */
	      int height = 318; /* Height of the image */ 
	      File XYChart = new File( "DelayChart.jpeg" ); 
	      ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
	}
}
