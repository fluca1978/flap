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
package flap.messaging;


/**
 * This class represents a message that can be sent to an agent.
 * Each message has a content that can be anything, like for instance a stringa,
 * an object, a binary data, and so on. It is the addressee that must interpret correctly
 * the message content.
 * 
 * Each message has an associate priority.
 * The normal priority means that the message will be handled by the agent
 * in a normal way, while the administrative priority means that the message is
 * special and has been sent from the platform itself.
 * 
 * Please note that once the message has been constructed its content and its priority
 * cannot be changed. However, if the content is an object it can be manipulated from
 * the addressee thru the reference to the content itself. 
 * To avoid this the content of the message could be accepted only if cloneable and each time
 * it is accessed a clone of the content is returned. This could be very time consuming and
 * CPU intensive, depending on the purpose of the message and the size of its content.
 * 
 * @author Luca Ferrari
 */
public class Message {

	

	
	
	/**
	 * The priority of this message. By default the priority is normal.
	 */
	protected MessagePriority priority = MessagePriority.PRIORITY_NORMAL;
	
	/**
	 * The content of the message.
	 * It can be anything of any type.
	 */
	protected Object content = null; 
	
	/**
	 * The message type.
	 */
	private MessageType type = MessageType.TYPE_USER;
	

	/**
	 * Constructs the message.
	 * @param priority the priority for this message
	 * @param content the content of this message (can be null)
	 * @param type the type of the message
	 */
	public Message(MessagePriority priority, Object content, MessageType type) {
		super();
		this.priority = priority;
		this.content = content;
		this.type = type;
	}
	
	/**
	 * Builds a message type of uer type.
	 * @param priority
	 * @param content
	 */
	public Message(MessagePriority priority, Object content){
		this( priority, content, MessageType.TYPE_USER);
	}
	
	/**
	 * Default constructor.
	 * Constructs a message with the normal priority and without any content.
	 */
	public Message(){
		this( MessagePriority.PRIORITY_NORMAL, null, MessageType.TYPE_USER );
	}

	/**
	 * Returns the value of the priority for the current class instance.
	 * @return the priority
	 */
	public synchronized final MessagePriority getPriority() {
		if( priority == null )
			return MessagePriority.PRIORITY_NORMAL;
		else
			return priority;
	}

	/**
	 * Returns the value of the content for the current class instance.
	 * @return the content
	 */
	public synchronized final Object getContent() {
		return content;
	}

	/**
	 * Returns the value of the type for the current class instance.
	 * @return the type
	 */
	public synchronized final MessageType getType() {
		return type;
	}

	

}
