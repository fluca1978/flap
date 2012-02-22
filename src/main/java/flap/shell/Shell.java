/**
 * ##### FLAP - Ferrari Luca's Agent PlatformExample ####
 * 
 * FLAP is mini-mini-micro agent platform I developed in order to introduce
 * students to agent and multi-agents contexts. The platform is designed to
 * help students to learn what are the issues and the main solutions in the
 * agent field. 
 * The platform is intentionally kept simple, its aim is not to be a competitive
 * product but a didactic framework on which students can experiments and debug
 * simple agent and multi-agent applications.
 * Source code represents also a good starting point for a complex project design and
 * can be used as a base to organize other Java projects.
 * 
 * This project is released as Open Source, so you can freely use and redistribute.
 * If you want to add features and/or improve the project source code and/or documentation,
 * please contact the author.
 * 
 * If you are using this project in your school or academic course, or even by your own,
 * please notify the author.
 * 
 *  Copyright (C) Luca Ferrari 2006-2012 - fluca1978 (at) gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Luca Ferrari nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Luca Ferrari ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Luca Ferrari BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package flap.shell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import flap.kernel.Context;

/**
 * The FLAP shell is an interactive command line program that
 * allows for administration of the platform itself.
 * 
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
public class Shell {
	
	/**
	 * The shell prompt to be shown to the user.
	 */
	private static final String SHELL_PROMPT = "flap> ";
	
	private ShellCommand createContextCommand = null;
	private ShellCommand createAgentCommand = null;
	private ShellCommand killAgentCommand = null;
	private ShellCommand listAgentCommand = null;
	private ShellCommand quitCommand = null;
	private ShellCommand helpCommand = null;
	private ShellCommand listThreadCommand = null;
	private ShellCommand killContextCommand = null;
	private ShellCommand listContextCommand = null;
	
	
	/**
	 * Running contexts.
	 */
	private List<Context> contexts = null;
	

	/**
	 * Provides initialization of the commands available for the shell.
	 */
	private void initCommands(){

		
		
		// a command to create a new context
		createContextCommand = new ShellCommand();
		createContextCommand.setCommand( "c" );
		createContextCommand.setShortDescription( "Creates a new context" );
		createContextCommand.setLongDescription( "Creates a new context that will be used to handle agents" );
		createContextCommand.setNumberOfRequiredArguments( 1 );
		
		
		// a command to create a new agent
		createAgentCommand = new ShellCommand();
		createAgentCommand.setCommand( "a" );
		createAgentCommand.setShortDescription( "Creates a new agent in the specified context" );
		createAgentCommand.setLongDescription( "Creates a new agent starting from the fully qualified class name and the id of the context" );
		createAgentCommand.setNumberOfRequiredArguments( 2 );
		
		
		// a command to kill anagent
		killAgentCommand = new ShellCommand();
		killAgentCommand.setCommand( "k" );
		killAgentCommand.setShortDescription( "Kills a running agent" );
		killAgentCommand.setLongDescription( "Kills an agent given its identifier" );
		killAgentCommand.setNumberOfRequiredArguments( 1 );
		
		
		// a command to list agents
		listAgentCommand = new ShellCommand();
		listAgentCommand.setCommand( "l" );
		listAgentCommand.setShortDescription( "Lists all running agents" );
		listAgentCommand.setLongDescription( "Lists running agents and their contexts" );
		
		// a command to display help
		helpCommand = new ShellCommand();
		helpCommand.setCommand( "h" );
		helpCommand.setShortDescription( "Displays available commands" );

		// a command to quit
		quitCommand = new ShellCommand();
		quitCommand.setCommand( "q" );
		quitCommand.setShortDescription( "Exits from the shell and stops the platform" );
		quitCommand.setLongDescription( quitCommand.getShortDescription() );
		
		// a command to list threads
		listThreadCommand = new ShellCommand();
		listThreadCommand.setCommand( "t" );
		listThreadCommand.setShortDescription( "List thread status" );
		listThreadCommand.setLongDescription( "List created threads and provide some information about their status" );
		
		// a command to close a context
		killContextCommand = new ShellCommand();
		killContextCommand.setCommand( "d" );
		killContextCommand.setShortDescription( "Close a context and all its agents" );
		killContextCommand.setLongDescription( "Kills a context and all its running agents" );
		
		// a command to list contexts
		listContextCommand = new ShellCommand();
		listContextCommand.setCommand( "v" );
		listContextCommand.setShortDescription( "List available contexts" );
		listContextCommand.setLongDescription( "List contexts running and all agents" );
	}
	
	
	/**
	 * Prints available commands and their description.
	 */
	private void doHelp(){
		System.out.println("*** HELP ***");
		
		List<ShellCommand> shellCommands = new LinkedList<ShellCommand>();
		shellCommands.add( createContextCommand );
		shellCommands.add( killContextCommand );
		shellCommands.add( createAgentCommand );
		shellCommands.add( killAgentCommand );
		shellCommands.add( listThreadCommand );
		shellCommands.add( helpCommand );
		shellCommands.add( listAgentCommand );
		shellCommands.add( listContextCommand );
		shellCommands.add( quitCommand );
		
		for( ShellCommand command : shellCommands ){
			System.out.print( "\t" + command.getCommand() + "\n" );
			System.out.println( command.getLongDescription() );
		}
	}
	
	/**
	 * Performs a shell loop presenting the user with the prompt and asking
	 * for a command to be inserted. The loop is finished only when the user
	 * presents a quit command.
	 */
	private void doShellLoop(){
		
		String userCommand = null;
		Scanner reader = new Scanner( System.in );
		
		// initialize commands
		initCommands();
		
		// print one time the help
		doHelp();
		
		// loop until the user wants to quit
		do{
			// give the shell prompt
			doShellPrompt( "\nStatus: READY", true );
			// take the user command
			userCommand = reader.nextLine();
			
			// what command to execute?
			if( quitCommand.getCommand().equals( userCommand ) ){
				// quit the platform
				doQuit();
			}
			else if( killContextCommand.getCommand().equals( userCommand ) ){
				// do I have context?
				if( contexts == null || contexts.size() <= 0 )
					doShellPrompt( "No context created yet!", false );
				else{
					// I need to kill a context
					List<String> contextNames = new LinkedList<String>();
					for( Context context : contexts )
						contextNames.add( context.getName() );

					String killName = askUserOption( "Which context to kill?", contextNames, reader );
					if( killName != null )
						doKillContext( killName );
				}
				
				
			}
			else if( createContextCommand.getCommand().equals( userCommand ) ){
				// create a new command, I need a name
				String name = askUserData( "Context name?", reader );
				doCreateContext( name );
			}
			else if( listContextCommand.getCommand().equals( userCommand ) 
					|| listAgentCommand.getCommand().equals( userCommand ) ){
				// list available contexts
				doListContext();
			}
			else if( createAgentCommand.getCommand().equals( userCommand ) ){
				if( contexts == null || contexts.size() <= 0 )
					doShellPrompt( "No context created yet!", false );
				else{
					// get the context
					Context context = askUserContext( "Which context?", reader );
					// get the agent name
					String aMName = askUserData( "Mnemonic name for the agent?", reader );
					// now get the fully qualified name of the agent class
					String aName = askUserData( "Agent fully qualified class name (e.g., flap.examples.ExampleAgent1)?", reader );
					
					// create the agent
					doCreateAgent( context, aMName, aName );
				}
			}
			else if( killAgentCommand.getCommand().equals( userCommand ) ){
				// to kill an agent I need to ask for a context and then for the agent in such context
				if( contexts == null || contexts.size() <= 0 )
					doShellPrompt( "No context created yet!", false );
				else{
					Context context = askUserContext( "Which context?", reader );
					
					// get the agent
					int agentIndex = askUserAgentInContext( context, reader );
					int agentID = context.getInstalledAgentProxyIDs()[ agentIndex ];
					
					// kill the agent
					context.killAgent( agentID );
					
				}
			}
			else if( helpCommand.getCommand().equals( userCommand ) )
				doHelp();
			else
				System.out.println("Command not understood or implemented!");
			
			
			
			
		}while( userCommand == null || ! quitCommand.getCommand().equals( userCommand ) );
	}
	
	/**
	 * Provides the context choosen by the user.
	 * @param question
	 * @param reader
	 * @return
	 */
	private final Context askUserContext( String question, Scanner reader ){
		List<String> cNames = new LinkedList<String>();
		for( Context c: contexts )
			cNames.add( c.getName() );

		// get the context name
		String cName = askUserOption( question, cNames, reader );
		
		for( Context c : contexts )
			if( c.getName().equals( cName ) )
				return c;
		
		return null;
	}
	
	/**
	 * Asks to choose an agent in a context.
	 * @param context
	 * @return
	 */
	private final int askUserAgentInContext( Context context, Scanner reader ){
		// get all the available agents
		int ids[] = context.getInstalledAgentProxyIDs();
		
		if( ids == null || ids.length <= 0 )
			return -1;
		
		// build an option list
		List<String> agents = new LinkedList<String>();
		for( int i : ids )
			agents.add( "AgentProxyID: " + i );
		
		int answer = askUserIntegerOption( "Which agent?", agents, reader );
		
		return  answer ;
	}
	
	
	/**
	 * Asks the user a question and returns the answer.
	 * @param question the question to ask the user for
	 * @param reader the reader used to get the answer
	 * @return the answer
	 */
	private final String askUserData( String question, Scanner reader ){
		doShellPrompt( question, false );		
		return reader.nextLine();
	}
	
	
	/**
	 * Prints the shell prompt with an optional message.
	 * @param message
	 */
	private final void doShellPrompt( String message, boolean normalPrompt ){
		if( message != null )
			System.out.println( message );
		
		if( normalPrompt )
			System.out.print( SHELL_PROMPT );
		else
			System.out.print( "> " );
	}
	
	/**
	 * Provides the user a list of options (numbered) and asks for which option to choose.
	 * Allows the user to exit from this menu using q.
	 * @param question a generic question to do
	 * @param options the list of not numbered options
	 * @param reader the reader used to get the answer
	 * @return the answer from the options
	 */
	private final String askUserOption( String question, List<String> options, Scanner reader ){
		
		int intAnswer = askUserIntegerOption( question, options, reader );
		
		// return the answer
		return options.get( intAnswer );
		
		
	}
	
	
	/**
	 * Provides the user a list of options (numbered) and asks for which option to choose.
	 * Allows the user to exit from this menu using q.
	 * @param question a generic question to do
	 * @param options the list of not numbered options
	 * @param reader the reader used to get the answer
	 * @return the numeric answer from the options
	 */
	private final int askUserIntegerOption( String question, List<String> options, Scanner reader ){
		
		int intAnswer = -1;
		do{
			System.out.println("\n\t" +  question );
			System.out.println( "\t" + quitCommand.getCommand() + " aborts the operation \n" );

			for( int i = 0; i < options.size(); i++ )
				System.out.println( i + " - " + options.get( i ) );

			System.out.print( SHELL_PROMPT );
			String answer = reader.nextLine();
			// do I have to exit from this menu?
			if( quitCommand.getCommand().equals( answer ) ) 
				return -1;

			try{
				intAnswer = Integer.parseInt( answer );
			}catch(NumberFormatException e ){ intAnswer = -1; }
			
		}while( intAnswer < 0 || intAnswer > options.size() );
		
		// return the answer
		return intAnswer;
		
		
	}
	

	/**
	 * Exits the whole platform.
	 */
	private void doQuit(){
		System.out.println("Exiting the platform...");
		System.exit( 0 );
	}
	
	
	/**
	 * Kills a context.
	 * @param contextID the name of the context
	 */
	private boolean doKillContext( String contextID ){
		Iterator iter = contexts.iterator();
		while( iter.hasNext() ){
			Context context = (Context) iter.next();
			
			if( context.getName().equals( contextID ) ){
				context.killAll();
				contexts.remove( context );
				return true;
			}
		}
		
		return false;
				
	}
	
	
	/**
	 * Creates a context with the specified name.
	 * @param name
	 */
	private final void doCreateContext( String name ){
		if( contexts == null )
			contexts = new LinkedList<Context>();
		
		Context context = new Context( name );
		contexts.add( context );
	}
	
	
	/**
	 * Prints a summary of running contexts.
	 */
	private final void doListContext(){
		if( contexts == null || contexts.size() <= 0 )
			System.out.println( "No available contexts");
		else{
			for( Context context : contexts ){
				System.out.println("\t" + context.getName() );
				for( int i : context.getInstalledAgentProxyIDs() )
					System.out.println("\t\t AgentProxyID: " + i );
			}
		}
	}
	
	/**
	 * Performs agent creation.
	 * @param contextName
	 * @param agentName
	 * @param agentClass
	 */
	private final void doCreateAgent( Context context, String agentName, String agentClass ){
		// check arguments
		if( context == null 
				|| agentName == null  || agentName.length()  <= 0 
				|| agentClass == null || agentClass.length() <= 0 )
			return;
		
	
		
		// create the agent
		int id = context.createAgent( agentName, agentClass );
		System.out.println("Agent created with id " + id);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.doShellLoop();

	}

}
