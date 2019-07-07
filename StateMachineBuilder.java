package assignment_02;
/* NAME: ELENE GHAITH ASSAF
 * ID: 201520441
 * COURSE: THEORY OF COMPUTATION -20171
 * ASSIGNMENT 02 
 */
import javax.swing.*;

public class StateMachineBuilder 
{
	public static void main(String a[])
	{
		//declarations for variables that will hold input
		char alphabet[] = null;
		String states[] = null;
		String trans[][] = null;
		String start = null;
		String finish[] = null;
		
		//declaration for DFA object
		DFA dd;
		
		boolean run = true;
		
		while(run)
		{
			int home = JOptionPane.showOptionDialog(new JFrame(), "Welcome to State Machine Builder\nWould you like to create a DFA?", 
			           "Builder", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
			            null, new Object[] {"Yes", "No, Exit"}, JOptionPane.YES_OPTION);
			
			if(home != JOptionPane.YES_OPTION)
				System.exit(0);
				
			boolean accept = false;
			
			while(!accept)
			{
				alphabet = fillAlphabet();
				states = fillStates();
				trans = fillTrans(alphabet, states);
				start = pickStart(states);
				finish = pickFinish(states);
				
				int acc = JOptionPane.showOptionDialog(new JFrame(), "E = {" + display(alphabet) 
		        + "} \n S = {" + display(states) + "} \n" 
		        + "Transitions: \n" + display(trans, states, alphabet) + "\n Start State: " + start + "\n finish: " + display(finish), 
				        "Confirm DFA?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				        null, new Object[] {"Yes, Run!", "Try Again."}, JOptionPane.YES_OPTION);
				
				accept = (acc == JOptionPane.YES_OPTION);
			}
			
			
			dd = new DFA(alphabet, states, trans, start, finish);
			
			boolean newWord = true;
			int again;
			String word = null;
			
			while(newWord)
			{
				word = JOptionPane.showInputDialog("Enter a word:").trim();
				dd.setWord(word);
				
				if(dd.isValid())
					JOptionPane.showMessageDialog(null, "accepted");
				else
					JOptionPane.showMessageDialog(null, "rejected");
				
				again = JOptionPane.showOptionDialog(new JFrame(), "Would you like to test a new word?", 
				        "Test again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				        null, new Object[] {"Yes!", "No."}, JOptionPane.YES_OPTION);
				
				if (again != JOptionPane.YES_OPTION)
				break;
			
			}
			
			int finished = JOptionPane.showOptionDialog(new JFrame(), "Thank you for using State Machine Builder\n"
					+ "Would you like to create another DFA?", 
			       "Thank You", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
			        null, new Object[] {"Yes, Start Over", "No, Exit"}, JOptionPane.YES_OPTION);
			
			if(finished == JOptionPane.NO_OPTION)
				System.exit(0);


		}
		
	}//main
	
	//a few methods that use JOptionPane to prompt user for input and set values to pass to our DFA
	public static char[] fillAlphabet()
	{
		int length = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of elements in your Alphabet set:"));
		
		String ch;
		String set = "";
		int alphaOK = 1;
		char[] a = new char[length];
		while(alphaOK != 0)
		{
			for(int i = 0; i < a.length; i++)
			{
				ch = JOptionPane.showInputDialog("Enter letter");
				a[i] = ch.charAt(0);
				set += a[i]+ ", ";
			}
			
			alphaOK = JOptionPane.showConfirmDialog(null, "is E={" + set + "} correct?", null, JOptionPane.YES_NO_OPTION);
			
			if(alphaOK == 0)
				break;
			else
				set = "";
			
		}
		
		return a;
	}//fillAlphabet
	
	public static String[] fillStates()
	{
		
		String s[] = null;
		boolean done = false;
		
		while(!done)
		{
			int length = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of states in your states set."));
			
			if(length <= 0)
		    	JOptionPane.showMessageDialog(null, "Sorry, " + length + " is not a valid set size! You must have at least one state!", "Whoops!", JOptionPane.ERROR_MESSAGE);
         	else
			{
				s = new String[length];
				done = true;

			}
		}
		
		String set = "";
		int stateOK = 1;
		
		stateIN: while(stateOK != 0)
		{
			for(int i = 0; i < s.length; i++)
			{
				s[i] = JOptionPane.showInputDialog("Enter state");
				
				if (s[i] == null)
				{
					int ans = JOptionPane.showOptionDialog(new JFrame(), "Would you like to Exit or Try Again?", 
					        "Cancel?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
					        null, new Object[] {"Try Again", "Exit"}, JOptionPane.YES_OPTION);
					
					if(ans == JOptionPane.YES_OPTION)
						continue stateIN;
					else
						System.exit(0);
						
				}
			
				set += s[i]+ ", ";
			}
			
			stateOK = JOptionPane.showConfirmDialog(null, "is S={" + set + "} correct?", null, JOptionPane.YES_NO_OPTION);
			
			if(stateOK == 0)
				break;
			else
				set = "";
			
		}
		
		return s;
	}//fillStates
	
	public static String[][] fillTrans(char a[], String[] s)
	{
		JOptionPane.showMessageDialog(null, "Please fill the transitions carefully");

		String set = "";
		int transOK = 1;
		String[][] t = new String[s.length][a.length];
		while(transOK != 0)
		{
			for(int i = 0; i < t.length; i++)
			{
				for (int j = 0; j < t[0].length; j++)
				{
					t[i][j] = JOptionPane.showInputDialog("(" + s[i] + "," + a[j] + ") -->").trim();
					
					if(notElementOf(s, t[i][j]))
					{
						JOptionPane.showMessageDialog(null, "Sorry," +  t[i][j] +"is not a defined state!", "Whoops!", JOptionPane.ERROR_MESSAGE);
						j--;
						continue;
					}
					
					set += "(" + s[i] + "," + a[j] + ") -->" + t[i][j] + "\n";

				}
			}
			
			transOK = JOptionPane.showConfirmDialog(null, "is:\n "+ set + " correct?", null, JOptionPane.YES_NO_OPTION);
			
			if(transOK == 0)
				break;
			else
				set = "";
			
		}
		
		return t;
	}//fillTrans
	
	public static String pickStart(String[] s)
	{
		boolean done = false;
		String start = "";
		String set = "";
		
		for (int i = 0; i < s.length; i++)
		{
			set += s[i] + ", ";
		}
		
		startOver: while(!done)
		{
			start = JOptionPane.showInputDialog("Pick (1) start state from S = {" + set + "}").trim();
			if (start == null)
			{
				int ans = JOptionPane.showOptionDialog(new JFrame(), "Would you like to Exit or Try Again?", 
				        "Cancel?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
				        null, new Object[] {"Try Again", "Exit"}, JOptionPane.YES_OPTION);
				
				if(ans == JOptionPane.YES_OPTION)
					continue startOver;
				else
					System.exit(0);
					
			}
		
		    if(notElementOf(s, start))
		    	JOptionPane.showMessageDialog(null, "Sorry, " + start +" is not a defined state!", "Whoops!", JOptionPane.ERROR_MESSAGE);
		    
		    else
		    	done = true;

		}
			
		
		return start;
		
	}//pickStart
	
	public static String[] pickFinish(String[] s)
	{
		String finish[] = null;
		boolean done = false;
		
        while(!done)
		{
			int length = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of FINAL states."));

			if(length > s.length)
		    	JOptionPane.showMessageDialog(null, "Sorry, the number of states is greater than the elements in your states set!", "Whoops!", JOptionPane.ERROR_MESSAGE);
			else if (length < 0)
		    	JOptionPane.showMessageDialog(null, "Sorry, " + length + " is not a valid set size!", "Whoops!", JOptionPane.ERROR_MESSAGE);
			else if (length == 0)
			{
				finish = new String[1];
				finish[0] = "none";
				return finish;
			}
			else
			{
				finish = new String[length];
				done = true;

			}
		}
		
		
	    String set = "";
	    String finishSet = "";
	    done = false;
		
		for (int i = 0; i < s.length; i++)
		{
			set += s[i] + ", ";
		}
		
		startOver: while(!done)
		{
			for (int i = 0; i < finish.length; i++)
			{
				finish[i] = JOptionPane.showInputDialog("Pick final state(s) from S = {" + set + "}").trim();
				if (finish[i] == null)
				{
					int ans = JOptionPane.showOptionDialog(new JFrame(), "Would you like to Exit or Try Again?", 
					        "Cancel?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
					        null, new Object[] {"Try Again", "Exit"}, JOptionPane.YES_OPTION);
					
					if(ans == JOptionPane.YES_OPTION)
						continue startOver;
					else
						System.exit(0);
						
				}
				
				if(notElementOf(s, finish[i]))
				{
					JOptionPane.showMessageDialog(null, "Sorry, the state" + finish[i] + " is not a defined state, try again", "Whoops!", JOptionPane.ERROR_MESSAGE);
	                i--;		
				}
				else if(i > 0)
				{
					if(!notElementOf(finish, finish[i]))
					{
						JOptionPane.showMessageDialog(null, "Sorry, the state" + finish[i] + " already exists in your final states set, try again", "Whoops!", JOptionPane.ERROR_MESSAGE);
	                    i--;
	                }
			    	
				}
				else
				{
					finishSet += finish[i] + ", ";
					
		            int ans = JOptionPane.showConfirmDialog(null, "is:\n "+ finishSet + " correct?", null, JOptionPane.YES_NO_OPTION);
					
					if(ans == JOptionPane.YES_OPTION)
						return finish;
					else
						finishSet = "";
				}
			}
		}

		return finish;
	}//pickFinish
	
	//simple method to iteratively search an array to detect if the value given is an element of an array
	public static boolean notElementOf(String[] s, String t)
	{
		for(int i = 0; i < s.length; i++)
		{
			if(t.equals(s[i]))
				return false;
		}
		return true;
	}//notElementOf
	
	//*********************************************
	//Simple overloaded methods to display arrays(used in confirm dialogs)
	public static String display(String ar[])
	{
		String set = "";
		for (int i = 0; i < ar.length; i++)
		{
			set += ar[i] +", ";
		}
		
		return set;
	}
	
	public static String display(String ar[][], String ar1[], char ar2[])
	{
		String set = "";
		for (int i = 0; i < ar.length; i++)
		{
			for (int j = 0; j < ar[i].length; j++)
			{
				set += "(" + ar1[i] + "," + ar2[j] + ") -->" + ar[i][j] +"\n";
			}
		}
		
		return set;
	}
	
	public static String display(char ar[])
	{
		String set = "";
		for (int i = 0; i < ar.length; i++)
		{
			set += ar[i] +", ";
		}
		
		return set;
	}

}
