/*
 * Validator - provides validation for user input
 */

public class Validator
{
	//returns true if the passed string contains numbers
	public boolean containsNumbers(String word)
	{
		for(int i = 0; i < word.length(); i ++)
		{
			//check to see if any of the charecters are digits
			if(Character.isDigit(((Character)word.charAt(i))))
			{
				return true;
			}
		}
		//none of them are
		return false;
	}
	
	//returns true if the passed string is not a number
	public boolean isNotInt(String word)
	{
		//try and convert it to an int
		try
		{
			Integer.parseInt(word);
		}
		catch(Exception e)
		{
			//exception caught, not a number
			return true;
		}
		return false;
	}
	
	//returns true if the passed string is a double
	public boolean isNotDouble(String word)
	{
		//try and convert it
		try
		{
			Double.parseDouble(word);
		}
		catch(Exception e)
		{
			//exception caught, not a doubles
			return true;
		}
		return false;
	}
	
	//returns true if the string is not correct;y formatted
	public boolean notCorrectOrderItems(String word)
	{
		//checks to see if the string has no commas
		if(word.length() > 3 && word.split(",").length == 1)
		{
			return true;
		}
		
		//check to see if the string is one charecter
		if(word.length() == 1)
		{
			//check to see what that charecter is
			if(Character.isLetter(word.charAt(0)))
			{
				return true;
			}
			return false;
		}
		
		//convert the string to an array
		String[] array = word.split(",");
		for(int i = 0; i < array.length; i ++)
		{
			for(int j = 0; j < array[i].length(); j ++)
			{
				if(Character.isLetter(array[i].charAt(j)) == true)
				{
					return true;
				}
			}
		}
		return false;
	}	
}