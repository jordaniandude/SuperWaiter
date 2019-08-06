package com.ozeh.apps.superwaiter.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Attendance implements Parcelable {

	public long id ;
	public String clockin ;
	public String clockout ;
	public String shift_date ;
	public double total_hours ;
	public double payoff ;
	public double tips_cash;
	public double tips_credit;
	public double profit;
	
	public Attendance(String clockin, String clockout,String shift_date, double total_hours,double payoff,double tipsCash,double tipsCredit,double profit) {
		this.clockin = clockin;
		this.clockout = clockout;
		this.shift_date = shift_date;
		this.total_hours = total_hours;
		this.payoff = payoff;
		this.tips_cash = tipsCash;
		this.tips_credit = tipsCredit;
		this.profit = profit;
	}

	public Attendance(Parcel source) {
		// TODO Auto-generated constructor stub
		this.clockin = source.readString();
		this.clockout = source.readString();
		this.shift_date = source.readString();
		this.total_hours = source.readDouble();
		this.payoff  = source.readDouble();
		this.tips_cash = source.readDouble();
		this.tips_credit = source.readDouble();
		this.profit = source.readDouble();}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		  dest.writeString(clockin);
	      dest.writeString(clockout);
	      dest.writeString(shift_date);
	      dest.writeDouble(total_hours);
	      dest.writeDouble(payoff);
	      dest.writeDouble(tips_cash);
	      dest.writeDouble(tips_credit);
	      dest.writeDouble(profit);
	}
	
	public static final Parcelable.Creator<Attendance> CREATOR = new Parcelable.Creator<Attendance>() {
	      public Attendance createFromParcel(Parcel source) {
	            return new Attendance(source);
	      }
	      public Attendance[] newArray(int size) {
	            return new Attendance[size];
	      }
	};

}
