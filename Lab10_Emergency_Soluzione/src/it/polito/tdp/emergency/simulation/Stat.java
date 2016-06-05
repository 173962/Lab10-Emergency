////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Statistics.                                                 //
//   #     _\  // Simple class to store some statistics.                      //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

public class Stat {
	private long numData;
	private double average;
	private double min;
	private double max;
	
	public Stat() {
		numData = 0;
		average = 0.0;
	}

	private void _addData(double x) {
		average = ( ( average * numData ) + x ) / ( numData +1);
		if(numData == 0 || x > max)
			max = x;
		if(numData == 0 || x < min)
			min = x;
		numData += 1.0;
	}

	public void addData(double x) {
		_addData(x);
	}
	public void addData(long x) {
		double d = x;
		_addData(d);
	}

	public double getAverage() {
		return average;
	}
	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}
	public long getNum() {
		return numData;
	}
}
