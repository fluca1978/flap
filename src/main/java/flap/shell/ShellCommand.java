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

/**
 * A wrapper for shell commands.
 * 
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
class ShellCommand {

	/**
	 * The command to digit into the shell.
	 */
	private String command = null;
	
	/**
	 * A short description of the shell.
	 */
	private String shortDescription = null;
	
	/**
	 * A long help for this command.
	 */
	private String longDescription = null;
	
	/**
	 * How many arguments does this command requires?
	 */
	private int numberOfRequiredArguments = 0;

	/**
	 * Returns the value of the command for the current class instance.
	 * @return the command
	 */
	public final String getCommand() {
		return command;
	}

	/**
	 * Set the command value in the current instance.
	 * @param command the command to set
	 */
	public final void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Returns the value of the shortDescription for the current class instance.
	 * @return the shortDescription
	 */
	public final String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Set the shortDescription value in the current instance.
	 * @param shortDescription the shortDescription to set
	 */
	public final void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Returns the value of the longDescription for the current class instance.
	 * @return the longDescription
	 */
	public final String getLongDescription() {
		return longDescription;
	}

	/**
	 * Set the longDescription value in the current instance.
	 * @param longDescription the longDescription to set
	 */
	public final void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Returns the value of the numberOfRequiredArguments for the current class instance.
	 * @return the numberOfRequiredArguments
	 */
	public final int getNumberOfRequiredArguments() {
		return numberOfRequiredArguments;
	}

	/**
	 * Set the numberOfRequiredArguments value in the current instance.
	 * @param numberOfRequiredArguments the numberOfRequiredArguments to set
	 */
	public final void setNumberOfRequiredArguments(int numberOfRequiredArguments) {
		this.numberOfRequiredArguments = numberOfRequiredArguments;
	}
	
	
	
}
