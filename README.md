# javaChat

Simon O'Shea and Jacob Gohring's Network Programming (COMP-2100-05) Final Project

javaChat is a barebones group chat application reminiscent of an IRC.

To host a chat on your own, the only preresiquites are that you have JDK 13 or above, have your router's port 6789 forwarded, and make a firewall rule for port 6789 to permit inbound and outbound access (unfortunately this was the only way I could get the data to transfer cross-network, am looking for another solution and would appreciate feedback).

********************************

To use:

Step 1: To host a server, open "java chat server.jar" (it will open and run silently in the background, check task manager under "background processes" for an "OpenJDK Platform Binary" process and kill it to end the server).

Step 2: To join the room, open the client "java chat.jar" and enter the IP address of the server. If you're running the server on the same machine, "localhost" will also work. Otherwise you just enter the public IP address of the person's computer that is hosting the server (or the LAN address if you're connected to the same router).

Step 3: Enter a username, hit enter, and chat away!
