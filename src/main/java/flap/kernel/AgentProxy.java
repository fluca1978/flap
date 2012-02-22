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
package flap.kernel;
import flap.agents.Agent;
import flap.agents.IAgentProxy;
import flap.messaging.*;

/**
 * Questa classe rappresenta un proxy per un generico agente. Il proxy mantiene il collegamento
 * fra l'agente e la sua message queue, e quindi è in grado di smistare i messaggi
 * coda dei messaggi dell'agente.
 * Come si può notare il proxy per molte delle chiamate non fa altro che fare da passa-carte
 * all'agente vero e proprio.
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
public class AgentProxy implements MessageHandler, IAgentProxy{

	/**
	 * The agent hidden behing this proxy.
	 */
	private Agent myOwningAgent = null;
	
	/**
	 * A message queue that handles all the messages incoming and that must
	 * be delivered to the hidden agent.
	 */
	private IMessageQueue incomingMessageQueue = null;
	
	
	/**
	 * Proxy constructor.
	 * Associates this proxy to a message queue and the agent target of such messages.
	 * @param myAgent the agent hidden behind this proxy
	 * @param queue the message queue that will store the incoming messages that are going
	 * to be delivered to the hidden agent
	 */
	public AgentProxy(Agent myAgent, MessageQueue queue){
		super();
		myOwningAgent = myAgent;
		incomingMessageQueue = queue;
		
		// set the circular reference to this proxy
		queue.setOwnerProxy( this );
	}
	
	/* (non-Javadoc)
	 * @see flap.agents.IAgentProxy#handleMessage(flap.messaging.Message)
	 */
	@Override
	public final boolean handleMessage(Message msg){
		incomingMessageQueue.addMessage(msg);
		return true;
	}

	/**
	 * Returns the value of the myOwningAgent for the current class instance.
	 * Please note that this method is not available as a public API.
	 * @return the myOwningAgent
	 */
	public synchronized final Agent getMyOwningAgent() {
		return myOwningAgent;
	}
	

	
	
}
