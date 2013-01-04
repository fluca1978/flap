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
package flap.examples;
import flap.kernel.Context;
import flap.messaging.*;
import flap.agents.*;

/**
 * An example of using the FLAP platform.
 */
public class PlatformExample {
	
	public static void main(String argv[]) throws Exception{
		// create a new context
		Context context = new Context("FLAP-EXAMPLE-CONTEXT");
		
		// create a few agents
		int dummy1 = context.createAgent("DummyAgent1", "flap.examples.ExampleAgent1");
		Thread.currentThread().sleep(1000);
		int dummy2 = context.createAgent("DummyAgent2", "flap.examples.ExampleAgent1");
		Thread.currentThread().sleep(10000);
		int dummy3 = context.createAgent("DummyAgent3", "flap.examples.ExampleAgent1");
		Thread.currentThread().sleep(10000);
		
		// send intra-agent messages
		IAgentProxy proxy2 =  context.getAgentProxy( dummy2 );
		proxy2.handleMessage(  new Message( MessagePriority.PRIORITY_NORMAL, "Hello from Main!") );
		
		
		// kill agents
		context.killAgent(dummy1);
		context.killAgent(dummy2);
		context.killAgent(dummy3);
		// wait for agents to work
		Thread.currentThread().sleep(10000);
		System.out.println("End of example!");
	}

}
