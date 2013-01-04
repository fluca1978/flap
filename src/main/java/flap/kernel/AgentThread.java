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
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import flap.messaging.*;
import flap.agents.*;

/**
 * @author Luca Ferrari ferrari.luca (at) unimore.it
 * @version 1.0
 */
public class AgentThread extends Thread {
	
	/**
	 * A flag to indicate if this thread can run or not.
	 */
	private boolean isActive = true;
	
	/**
	 * The agent on which the thread will work.
	 */
	private Agent targetAgent = null;
	
	/**
	 * The message queue on which the agent will work.
	 */
	private MessageQueue queue = null;
	
	
	/**
	 * The logger of this thread.
	 */
	private Log logger = LogFactory.getLog( AgentThread.class );
	
	
	/**
	 * Builds a new agent thread with the specified number as identifier.
	 * The thread is immediatly started and set as daemon, so that it will not lock
	 * the platform.
	 * Please note that the constructor is private, because a new thread must be created
	 * using a "factory" like method in order to manage the thread pool.
	 * @param number the number that identies this thread.
	 */
	private AgentThread( int number ){
		super( "AgentThread-" + number );
		setDaemon( true );
		isActive = true;
		start();
	}
	
	/**
	 * The max number of thread that can be created on the platform.
	 */
	private static int MAX_THREAD_TO_CREATE = 10;
	
	/**
	 * The counter of the created threads,
	 */
	private static int createdThreadCounter = 0;
	
	/**
	 * A container for all the threads, it implements a simple thread pool.
	 */
	private static Stack<AgentThread> threadPool = new Stack<AgentThread>();
	
	/**
	 * Provides a new thread to process a message.
	 * The thread is built from scratch if possibile, or it is extracted from the
	 * thread pool if at least one thread is available. If no threads are available and
	 * no more threads can be built, the caller is suspended until a new thread is available.
	 * @return the thread to use
	 */
	public static synchronized AgentThread getThread(){
		AgentThread threadToReturn = null;
		do{
			// let's see if we have a thread available in the pool
			if( ! threadPool.isEmpty() )
				threadToReturn = threadPool.pop();
			else if( createdThreadCounter < MAX_THREAD_TO_CREATE ){
				// I can create a new thread
				threadToReturn = new AgentThread( ++createdThreadCounter );
			}
			else{
				// there are no threads available and we cannot create any more
				// so we have to wait until a thread is available
				try{
					AgentThread.class.wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}while( threadToReturn == null );
		
		return threadToReturn;
	}
	
	
	/**
	 * Main thread method. It executes an infinite loop to process all incoming messages.
	 * If the thread has not been assigned to any couple agent-queue (and therefore has no messages
	 * to process) it sleeps, otherwise it starts processing the message on the assigned couple.
	 * The thread is awaken as soon as someone sets the agent/queue couple to process next messages.
	 */
	public final  void run(){
	
		try{
			// almost infinite loop
			while( isActive ){
				// do I have a message to process?
				while( ! hasAMessageToProcess() ){
					logger.debug( String.format( "Thread %s has nothing to do, sleeping...", getName() ) );
					synchronized (this){
						wait();	
					}
					
				}
				
				// if here I've got a message to process
				while( ! queue.isEmpty() )
					processNextMessage( queue, targetAgent );
				
				// done, reset the queue and the target agent
				setHandlingPeers( null, null );
				logger.debug( String.format( "Thread %s has processed the message", getName() ) );
				
				// insert this thread in the pool
				insertThreadInPool( this );
			}
		}
		catch(Exception e){
			logger.error( "Exception caught while doing the infinite thread loop", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts a thread in the pool and notifies waiters that there is a new thread available.
	 * @param thread the thread to insert in the pool
	 */
	private static final synchronized void insertThreadInPool( AgentThread thread ){
		threadPool.push( thread );
		AgentThread.class.notify();
	}
	
	/**
	 * Sets the agent and the message queue to process and awake the thread.
	 * @param queue the message queue from which the message must be extracted
	 * @param targetAgent the target agent to which delivering the message
	 */
	public final synchronized void handleMessage( MessageQueue queue, Agent targetAgent ){
		setHandlingPeers( queue, targetAgent );
		notifyAll();
	}
	
	/**
	 * A private service to set the queue and the agent to handle.
	 * @param queue the message queue
	 * @param targetAgent the target agent
	 */
	private final synchronized void setHandlingPeers( MessageQueue queue, Agent targetAgent ){
		this.queue = queue;
		this.targetAgent = targetAgent;
	}
	
	
	/**
	 * A service method to know if the thread has something to process.
	 * The thread will fire message processing as soon as the message queue
	 * and the target agent are not null.
	 * @return true if there is something to process, false otherwise
	 */
	private synchronized boolean hasAMessageToProcess(){
		return( queue != null && targetAgent != null );
	}
	
	/**
	 * Delivers a message to the specified agent extracting the message from the specified
	 * queue. Delivering a message means reaching an agent with the message. Please note that the
	 * message priority is already handled from the message queue, so there are no particular
	 * tasks to be performed here.
	 * @param queue the message queue from which the message will be extracted
	 * @param targetAgent the agent to which deliver the message
	 */
	private synchronized void processNextMessage( MessageQueue queue, Agent targetAgent ){
		// if the message queue is empty, there is nothing to do here.
		if( queue.isEmpty() ){
			logger.debug( "The queue is empty, nothing to process" );
			return;
		}
		
		// get the next message. Please note that the message queue returns the right message
		// depending on the priority.
		Message nextMessage = queue.getNextMessage();
		
		if( nextMessage == null )
			// should never happen
			return;
		
		// if the message is administrative, check what kind of message it is
		if( nextMessage.getPriority() == MessagePriority.PRIORITY_ADMIN ){
			logger.debug( "Administrative message " );
			if( MessageType.TYPE_SETUP == nextMessage.getType() )
				targetAgent.setUp();
			else if(  MessageType.TYPE_RUN == nextMessage.getType() )
				targetAgent.run();
			else if(  MessageType.TYPE_SHUTDOWN == nextMessage.getType() )
				targetAgent.die();
			else
				logger.debug( "Administrative message not understood!  " + nextMessage.getContent() );
			
			return;
		}
		
		// deliver the message to the agent
		logger.debug( String.format( "Delivering a message [priority = %s, content = %s] to agent %d %s",
						nextMessage.getPriority().toString(),
						nextMessage.getContent().toString(),
						targetAgent.getId(),
						targetAgent.getName()
					  )
				);
		targetAgent.handleMessage( nextMessage );
		logger.debug( "Message delivered!" );
		
		// all done
		return;
		
	}
	
	
	
	
}
