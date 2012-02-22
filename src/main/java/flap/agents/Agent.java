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
package flap.agents;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flap.messaging.*;
import flap.kernel.*;

/**
 * The base class for each agent.
 * This class provides a set of basic functionalities that each agent must have.
 * In particular, each agent is identified by a numeric id (unique), a mnemonic name
 * (not unique) and a few methods that belongs to its life cycle. The agent life cycle is
 * quite simple:
 * 1) initialization => setUp method
 * 2) execute task(s) => run method
 * 3) destruction => shutDown method
 * 
 * Overriding one or all the above methods provide a fully configurable agent.
 * 
 * Please note that this class is abstract even if all its functionalities are in place,
 * so that developers are forced to create new agent classes and stereotypes and cannot use
 * this (empty) class as a fully running agent.
 * 
 * 
 * @author Luca Ferrari
 * @version 1.0
 */
public abstract class Agent implements MessageHandler{
	
	/**
	 * A mnemonic name for the agent. It is a descriptive name
	 * used to better identify the agent. It can be, for instance,
	 * something related to the aim of the agent like "DatabaseAgent".
	 */
	private String name = null;
	
	/**
	 * Unique identifier of the agent.
	 * Two agents must have always a different id, so that the platform
	 * can uniquely identify each agent.
	 * All the identifiers are greater than zero, so by default a new agent
	 * has an invalid identifier in order to allow the platform to check
	 * if the agent can be installed or not.
	 * 
	 * Please note that this id is automatically assigned by a static constructor
	 * to the next valid for this agent class (and the whole platform).
	 * It is important to note also that the id cannot be accessed (for writing)
	 * by subclasses.
	 */
	private int id = getNextAgentID();
	
	
	
	/**
	 * A static counter that keeps track of the next valid ID
	 * to assign to an agent.
	 */
	public static int nextAgentID = 0;
	
	/**
	 * The context to which the agent belongs.
	 */
	protected Context context = null;
	
	/**
	 * A logger for this agent.
	 */
	private Log logger = LogFactory.getLog( Agent.class );
	
	
	/**
	 * A static service to get the next valid ID for an agent.
	 * This method is used to initialize the id of the agent, that will
	 * never set again. Please note that the whole method is synchronized
	 * allowing a single thread to get access to the id generator.
	 * @return the id to assign to the next agent
	 */
	public final synchronized static int getNextAgentID(){
		return ++nextAgentID;
	}
	
	
	/**
	 * This method is called each time the agent is initialized.
	 * This is a setup method, use to allocate objects and other stuff that will
	 * be useful during the agent life-cycle.
	 * By default this method does nothing more than loggin its invocation.
	 * Override this method in order to setup something for your agent.
	 */
	public void setUp(){
		
		logger.debug( String.format( "[agent id = %d (%s)] -> setUp method called!", id, name ) );
		logger.debug( String.format( "[agent id = %d (%s)] Please overrride the method in order to allow a correct agent initialization", id, name ) );
	}
	
	
	/**
	 * Main lifecycle method. Place here (overriding this method) the execution logic
	 * so that the agent can perform its task(s).
	 */
	public void run(){
		logger.debug( String.format( "[agent id = %d (%s)] -> setUp method called!", id, name ) );
		logger.debug( String.format( "[agent id = %d (%s)] Please override this method to let the agent execute its task(s) during its life cycle", id, name ) );
	}
	
	/**
	 * Method to allow for agent de-initialization. This method is called to allow the agent
	 * to close resources and release memory before it is removed from the platform.
	 *
	 */
	public void die(){
		logger.debug( String.format( "[agent id = %d (%s)] -> shutDown method called!", id, name ) );
		logger.debug( String.format( "[agent id = %d (%s)] Please override this method to allow for a correct agent destruction", id, name ) );
	}
	
	/**
	 * Handling of incoming messages.
	 * Each time the agent is notified of a new incoming message, this method is called.
	 * @param messaggio the message instance
	 */
	public boolean handleMessage(Message messaggio){
		logger.debug( String.format( "[agent id = %d (%s)] -> handleMessage fired!", id, name ) );
		logger.debug( String.format( "[agent id = %d (%s)] Please override this method to correctly handling the message", id, name ) );
		return false;
	}


	/**
	 * Returns the value of the name for the current class instance.
	 * @return the name
	 */
	public synchronized final String getName() {
		return name;
	}


	/**
	 * Set the name value in the current instance.
	 * @param name the name to set
	 */
	public synchronized final void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the value of the id for the current class instance.
	 * @return the id
	 */
	public synchronized final int getId() {
		return id;
	}


	/**
	 * Returns the value of the context for the current class instance.
	 * @return the context
	 */
	public synchronized final Context getContext() {
		return context;
	}


	/**
	 * Set the context value in the current instance.
	 * @param context the context to set
	 */
	public synchronized final void setContext(Context context) {
		this.context = context;
	}



	
	
	
	
}
