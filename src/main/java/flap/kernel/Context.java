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
 *  Copyright (C) Luca Ferrari 2006-2013 - fluca1978 (at) gmail.com
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
package flap.kernel;
import flap.agents.*;
import flap.messaging.*;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Questa classe rappresenta un generico contesto.
 * Un contesto è un ambiente in cui gli agenti possono essere creati, possono vivere
 * e comunicare e possono essere distrutti. Compito del contesto è quello di fornire 
 * supporto all'indicizzazione degli agenti, alla loro comunicazione e alla loro
 * istanziazione.
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
public class Context {

	/**
	 * The context keeps a map of all installed agents and their installedProxies.
	 * The map stores the agent proxy (from which the context can extract the agent) and the id of
	 * the agent (and agent proxy). The id of the agent is the key for the map.
	 */
	protected HashMap<Integer, AgentProxy> installedProxies = null;
	
	/**
	 * Tabella che contiene riferimenti ai thread degli agenti effettivi (non ai loro proxy).
	 * Viene utilizzata per operazioni speciali (ad esempio la loro distruzione) e non
	 * deve mai essere resa pubblica. E' indicizzata come quella dei proxy.
	 */
	private HashMap threads = null;
	

	
	/**
	 * A mnemonic name of the context, used to see what the aim/task of the context is.
	 */
	private String name = null;
	
	/**
	 * The logger of this context.
	 */
	private Log logger = LogFactory.getLog( Context.class );
	
	
	/**
	 * Costruttore del contesto.
	 * @param name un nome simbolico da attribuire al contesto
	 */
	public Context(String name){
		super();
		this.name = name;
		installedProxies = new HashMap<Integer, AgentProxy>();
		this.threads  = new HashMap();
	}
	
	/**
	 * Metodo per creare un nuovo agente.
	 * Il metodo istanzia tutti i componenti necessari per la creazione di un nuovo agente,
	 * quali il suo message manager, il suo thread, il suo proxy. Dopo aver associato assieme
	 * tutte le proprietà, il metodo registra l'agente presso il contesto.
	 * @param friendlyName il nome simbolico dell'agente
	 * @param clazz il nome della classe da cui creare l'agente
	 * @return l'identificativo dell'agente creato
	 */
	public final synchronized  int createAgent(String agentName, String clazz){
		// check arguments
		if(clazz == null)
			return -1;
		
		try{
			// step 1: create a new agent instance
			logger.debug( String.format( "[CONTEXT %s] = createAgent: step 1 = Creating the agent %s from class %s", name, agentName, clazz ) );
			Class agentClass = Class.forName(clazz);
			Agent agent = (Agent) agentClass.newInstance();
			
			// step 2: initialize the agent
			agent.setName( agentName );
			agent.setContext( this );
			logger.debug( String.format( "[CONTEXT %s] = createAgent: step 2 = Agent %s associated to the context", name, agentName) );
			
			// step 3: create a message queue 
			IMessageQueue messageManager = new MessageQueue();
			logger.debug( String.format( "[CONTEXT %s] = createAgent: step 3 = MessageQueue created", name) );
			
			// step 4: create a new proxy
			// and associate to the proxy both the agent and the message manager
			AgentProxy proxy = new AgentProxy( agent, (MessageQueue) messageManager );
			logger.debug( String.format( "[CONTEXT %s] = createAgent: step 4 = A new proxy for the agent has been created!", name) );

			// step 5: store the agent and its proxy into the map
			// that contains the installed agents
			installedProxies.put( agent.getId(), proxy );
			logger.debug( String.format( "[CONTEXT %s] = createAgent: step 5 = The proxy and the agent have been stored in the installed agent map", name) );
			

			// step 6: place messages into the queue in order to make the agent
			// to start
			messageManager.addMessage(  new Message( MessagePriority.PRIORITY_ADMIN, "setup", MessageType.TYPE_SETUP) );
			messageManager.addMessage(  new Message( MessagePriority.PRIORITY_ADMIN, "run", MessageType.TYPE_RUN) );
			
			// all done, return the agent id
			return agent.getId();
			
			
		}catch(Exception e){
			logger.error( "Exception caught while creating a new agent", e );
			e.printStackTrace();
			return -1;
		}


		
		
	}
	
	/**
	 * Given the id (unique) of an agent, this method provides its proxy.
	 * This works as a kind of lookup system for an agent proxy.
	 * @param agentID the id of the searching for agent
	 * @return the agent proxy (if found and installed) or null
	 */
	public final synchronized IAgentProxy getAgentProxy(int agentID){
		if( installedProxies.containsKey( agentID ) )
			return (IAgentProxy) installedProxies.get( agentID );
		else
			return null;
	}
	
	/**
	 * Kills a specific agent.
	 * Given an agent (unique) identifier, this method removes the agent and its proxy
	 * from the installed agents map.
	 * @param agentID the id of the agent to kill
	 * @return true if the agent is removed, false if not (maybe it has been killed before)
	 */
	public final synchronized boolean killAgent(int agentID){
		// check if the agent is running and is installed
		if( ! installedProxies.containsKey( agentID ) ){
			return false;
		}
		
		// send a message to kill the agent
		IAgentProxy proxy = getAgentProxy( agentID );
		proxy.handleMessage(  new Message( MessagePriority.PRIORITY_ADMIN, "kill", MessageType.TYPE_SHUTDOWN ) );
		
		// now remove the agent from the proxy map, so that it is no more installed
		logger.debug( String.format( "[CONTEXT %s] = killAgent: step 1 = Deinstalling agent %d and its proxy", name, agentID) );
		installedProxies.remove( agentID );
		return true;
/*		
		// fase 1: rimuovo l'agente dalle tabelle, così nessuno può più mandargli
		//         dei messaggi
		System.out.println("Il contesto rimuove l'agente"+ agentID);
		this.proxies.remove(new Integer(agentID));
		
		// fase 2: recupero il riferimento al thread dell'agente
		AgentThread thread = (AgentThread) this.threads.get(new Integer(agentID));
		this.threads.remove(new Integer(agentID));
		
		// fase 3: ordino al thread dell'agente di terminare
		System.out.println("Il contesto ordina al thread di distruggersi");
		thread.destroyThread();
		System.out.println("Distruzione del thread completata ");
		*/
	}
	
	/**
	 * A method to kill all the running agents in the context.
	 * The method stops at the first agent that cannot be killed.
	 * @return true if all the running agents have been killed, false otherwise (it means that at least one
	 * agent has failed to be killed, and the kill action has not continued)
	 */
	public final synchronized boolean killAll(){
		for( int agentID : installedProxies.keySet() )
			if( ! killAgent( agentID ) )
				return false;
		
		// all done
		return true;
	}

	/**
	 * Returns the value of the name for the current class instance.
	 * @return the name
	 */
	public synchronized final String getName() {
		return name;
	}
	
	
	/**
	 * Provides an array of all the installed agent proxy ids known at this
	 * time.
	 * @return the array of ids
	 */
	public synchronized int[] getInstalledAgentProxyIDs(){
		if( installedProxies.isEmpty() )
			return new int[0];
		
		int ids[] = new int[ installedProxies.size() ];
		int i = 0;
		for( int currentID : installedProxies.keySet() )
			ids[ i++ ] = currentID;
		
		return ids;
	}
	
	
	
	
	
}
