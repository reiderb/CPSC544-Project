package parsing;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import parsing.CollisionEntry;

import rules.SunTime;
import rules.Predicate;

public class SunParser
{
	public static ArrayList<SunTime> getSunTable(String path)
	{
		ArrayList<SunTime> sunrises = new ArrayList<SunTime>();
		/*
		//later.. maybe..
		BufferedReader reader = null;
		String line = "";
		String delimiter = " "; //So, we'll split the lines at spaces
		Rule tempmonth;
		Rule temprange;
		
		try
		{
			reader = new BufferedReader(new FileReader(path));
			reader.readLine(); //skip first line
			while ((line = reader.readline()) != null)
			{
				String[] splitEntry = line.split(delimiter);
				tempmonth = makeMonth(splitEntry[0]);
				
			}
			
		}
		* */
		return sunrises;
	}
	
	private Predicate makeMonth(String monthstring)
	{
		Predicate month;
		switch(monthstring)
		{
			case "JAN":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 1);
				break;
			case "FEB":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 2);
				break;
			case "MAR":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 3);
				break;
			case "APR":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 4);
				break;
			case "MAY":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 5);
				break;
			case "JUN":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 6);
				break;
			case "JUL":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 7);
				break;
			case "AUG":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 8);
				break;
			case "SEP":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 9);
				break;
			case "OCT":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 10);
				break;
			case "NOV":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 11);
				break;
			case "DEC":
				month = new Predicate(Predicate.FEATURE.C_MNTH, 12);
				break;
			default:
				month = new Predicate(Predicate.FEATURE.C_MNTH, -1); //this should never be triggered..
				break;
		}
		return month;
	}
	/*
	//work on dis crapola later..
	private Predicate makeDayRange(int min, int max)
	{
		Predicate range = new Predicate(Predicate.FEATURE.
	}
	*/
}
