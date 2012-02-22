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
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flap.messaging.IMessageQueue;
import flap.messaging.Message;
import flap.messaging.MessagePriority;

/**
 * Questa classe fornisce l'implementazione di una coda di messaggi. Una coda di messaggi
 * e' un contenitore per i messaggi relativi ad un agente. Ogni agente ha una ed una sola
 * coda, nella quale vengono salvati i messaggi prima che l'agente li processi. In questo modo
 * nessun messaggio viene consegnto direttamente all'agente, e l'agente può accumulare
 * messaggi in ingresso ogni volta che è impegnato a fare qualche cosa di diverso (es. processare
 * un altro messaggio).
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
public class MessageQueue implements IMessageQueue {
	
	/**
	 * Messages must be kept internally into a queue, that guarantees
	 * that the right ordering is kept (i.e., a message is not processed before
	 * a message that arrived before).
	 * However messages must be kept separated depending on priorities, so the queues
	 * are split into one for each kind of priority.
	 */
	private HashMap<MessagePriority, List<Message> > messageQueues = null;
	
	/**
	 * The logger of this message queue.
	 */
	private Log logger = LogFactory.getLog( MessageQueue.class );
	
	/**
	 * The proxy owning the message queue.
	 */
	private AgentProxy ownerProxy = null;
	
	/**
	 * Default constructor.
	 * Creates all the message queues.
	 */
	public MessageQueue() {
		super();
		
		// init the message queue container
		messageQueues = new HashMap<MessagePriority, List<Message>>();
		
		// create all the message queues
		for( MessagePriority priority : MessagePriority.values() )
			messageQueues.put( priority, new LinkedList<Message>() );
		
	}

	/* (non-Javadoc)
	 * @see flap.messaging.IMessageQueue#addMessage(flap.messaging.Message)
	 */
	@Override
	public synchronized void addMessage(Message messaggio){
		if( messaggio == null )	
			return;		// skip null messages
		
	
		// store the message into the right queue depending on its
		// priority
		logger.debug( "[MessageQueue] Storing the message into the right queue" );
		deliverMessageIntoTheRightQueue( messaggio );
		
		// get a thread to handle this message
		logger.debug( "[MessageQueue] Asking a thread for processing the message" );
		AgentThread thread = AgentThread.getThread();
		thread.handleMessage( this, ownerProxy.getMyOwningAgent() );
		
	}
	
	
	/**
	 * A private service to deliver the message into the right queue
	 * depending on its priority.
	 * @param msg the message to deliver
	 */
	private void deliverMessageIntoTheRightQueue( Message msg ){
		// get the right queue
		List<Message> queue = messageQueues.get( msg.getPriority() );
		queue.add( msg );
	}
	
	/**
	 * Provides the next method to be processed.
	 * The method scans each priority queue in order to find the first not-null message and removes
	 * it from the queue returning it. The side effect is that the message is no more into the
	 * queue.
	 * If no message is found at any priority level, than null is returned.
	 * @return the next message to process or null if none
	 */
	public synchronized Message getNextMessage(){
		for( MessagePriority priority : MessagePriority.values() ){
			List<Message> currentQueue = messageQueues.get( priority );
			if( ! currentQueue.isEmpty( ) ){
				// get the head and remove it from the list
				Message currentMessage = currentQueue.remove( 0 );
				// this is the next message to be processed
				return currentMessage;
			}
				
		}
		
		
		// if here no message has been found in any priority queue
		return null;
			
			
	}
	
	/**
	 * Provides the total amount of messages waiting to be processed, without any regard to the
	 * priority of each message.
	 * @return the total number of not-yet-processed messages
	 */
	public final int getQueueLength(){
		int sum = 0;
		
		for( MessagePriority priority : MessagePriority.values() )
			sum += getQueueLengthByPriority( priority );
		
		return sum;
			
	}
	
	/**
	 * Provides the size of the message queue for a specific priority.
	 * @param priority the priority to inspect
	 * @return the number of waiting messages at the given priority
	 */
	public final int getQueueLengthByPriority( MessagePriority priority ){
		return messageQueues.get( priority ).size();
	}
	
	/* (non-Javadoc)
	 * @see flap.messaging.IMessageQueue#isEmpty()
	 */
	@Override
	public final boolean isEmpty(){
		for( MessagePriority priority : MessagePriority.values() )
			if( ! messageQueues.get( priority ).isEmpty() )
				return false;
		
		return true;
	}
	
	
	/**
	 * Metodo per sospendersi su questa coda attendendo un nuovo messaggio.
	 */
	public final synchronized void wait_a_message(){
		try{
			if( this.isEmpty() ){
				this.wait();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo per risvegliare la message queue.
	 */
	public final synchronized void resume_message_queue(){
		this.notifyAll();
	}

	/**
	 * Returns the value of the ownerProxy for the current class instance.
	 * @return the ownerProxy
	 */
	public synchronized final AgentProxy getOwnerProxy() {
		return ownerProxy;
	}

	/**
	 * Set the ownerProxy value in the current instance.
	 * @param ownerProxy the ownerProxy to set
	 */
	public synchronized final void setOwnerProxy(AgentProxy ownerProxy) {
		this.ownerProxy = ownerProxy;
	}
	
	
}
