
package backend ;

import java.io.DataInputStream ;
import java.io.DataOutputStream ;
import java.io.IOException ;
import java.net.ServerSocket ;
import java.net.Socket ;
import java.util.ArrayList ;

/**
 * ChatServer class accepts Client requests to join 
 * and creates an individual thread for each client.
 * The "brain" of the server.
 *
 * @author osheas1
 * @author gohringj
 * @version Semi-Final Draft
 */
public class ChatServer
    {

    // List of all active users in Server
    public static ArrayList<ClientHandler> userList = new ArrayList<>() ;

    
    /**
     * @param args (unused)
     * @throws IOException (is thrown if connection is unexpectedly closed)
     */
    public static void main( final String[] args ) throws IOException
        {
        // Register service on port 6789
        ServerSocket serverSocket = new ServerSocket( 6789 ) ;
        Socket socket = null ;

        System.out.println( "\nServer established. Awaiting connections..." ) ;

        // Loop to accept client requests
        while ( true )
            {
            socket = serverSocket.accept() ;

            DataOutputStream dataStreamOut = new DataOutputStream( socket.getOutputStream() ) ;
            DataInputStream dataStreamIn = new DataInputStream( socket.getInputStream() ) ;

            System.out.println( "\nClient connection detected, requesting username..." ) ;
            String username = dataStreamIn.readUTF() ;

            ClientHandler handle = new ClientHandler( socket, username, dataStreamOut, dataStreamIn ) ;

            System.out.println( "\nCreating new thread...\n" ) ;
            Thread thread = new Thread( handle ) ;

            userList.add( handle ) ;

            thread.start() ;
            }
        }   
    } // end class ChatServer




/**
 * ClientHandler class manages the actual messages being sent
 * between the users. The "heart" of the server.
 *
 * @author osheas1
 * @author gohringj
 * @version Semi-Final Draft
 */

class ClientHandler implements Runnable
    {
    Socket socket ;
    String username ;
    DataOutputStream dataStreamOut ;
    DataInputStream dataStreamIn ;
    boolean loggedIn ;
    int commandCheck ;

    /**
     * Constructor
     * @param s = client socket
     * @param user = client's user-name
     * @param dos = data stream to client
     * @param dis = data stream from client
     */
    public ClientHandler( Socket s, String user, DataOutputStream dos, DataInputStream dis )
        {
        this.socket = s ;
        this.username = user ;
        this.dataStreamOut = dos ;
        this.dataStreamIn = dis ;
        this.loggedIn = true ;
        }


    @Override
    public void run()
        {
        // Note the connection of the new user
        String announcement = String.format( "\n>>> User \"%s\" has successfully connected (type /help for a list of commands) <<<\n", this.username ) ;
        System.out.println( announcement ) ;

        // Distribute announcement to entire server
        sendMessage( announcement ) ;

        // Message management
        String message = "" ;
        while ( true )
            {          
            // Try-Catch block interprets incoming messages 
            try
                {
                message = this.dataStreamIn.readUTF() ;
                
                if(message == "" || message == " ")
                    {
                    continue ;
                    }
                }
            catch ( IOException x )
                {
                x.printStackTrace() ;
                }

            // Check message for user commands, returns TRUE if user "/quit"s
            if( commands( message, this ) )
                {
                break ;
                }
           
            // Print the message server-side
            message = String.format( "%s: %s", this.username, message ) ;
            System.out.println( message ) ;

            // Distribute the message to all users
            if ( this.commandCheck == 0 )
                {
                sendMessage( message ) ;
                }
            else
                {
                this.commandCheck = 0 ;
                }                                  
            } // end while

        // close streams when user disconnects
        try
            {
            this.loggedIn = false ;
            this.dataStreamIn.close() ;
            this.dataStreamOut.close() ;
            this.socket.close() ;
            }
        catch ( IOException x )
            {
            x.printStackTrace() ;
            }
        } // end run


    /**
     * The 'commands' method manages the usage of user-commands
     * by distributing private messages, providing a user-list,
     * etc.
     *
     * @param message = the command itself
     * @param user = the user that 
     * @return = boolean that indicates that the user has disconnected
     */
    public boolean commands( String message, final ClientHandler user )
        {
        final String[] commands = new String[] { "/help", "/quit", "/userlist", "/pm" } ;

        try
            {
            // Quit command terminates client's connection, with an announcement
            if ( message.equals( "/quit" ) )
                {
                this.commandCheck++ ;
                String announcement = String.format( "\n-= \"%s\" has disconnected =- \n", user.username ) ;
                System.out.println( announcement ) ;
                sendMessage( announcement ) ;
                return true ;
                }

            // User-List command provides the user a list of everyone else connected to the server
            if ( message.equals( "/userlist" ) )
                {
                this.commandCheck++ ;
                user.dataStreamOut.writeUTF( "> Current list of users in the server: " ) ;
                for ( ClientHandler users : ChatServer.userList )
                    {
                    if ( !users.loggedIn )
                        {
                        continue ;
                        }

                    user.dataStreamOut.writeUTF( "> " + users.username ) ;
                    }
                }

            // Help command lists all available commands
            if ( message.equals( "/help" ) )
                {
                this.commandCheck++ ;
                user.dataStreamOut.writeUTF( "\n> All available commands: " ) ;
                for ( String command : commands )
                    {
                    user.dataStreamOut.writeUTF( "> " +command ) ;
                    }

                user.dataStreamOut.writeUTF( "\n" ) ;
                }

            // Private Message Command allows users to send messages to a single recipient
            if ( message.contains( "/pm" ) )
                {
                this.commandCheck++ ;
                user.dataStreamOut.writeUTF( "\n> Who would you like to private message?" ) ;
                String recipient = this.dataStreamIn.readUTF() ;
                user.dataStreamOut.writeUTF( "> \"" + recipient + "\"" ) ;

                ClientHandler sendTo = null ;

                // loop determines which ClientHandler object to send the message to
                for ( ClientHandler users : ChatServer.userList )
                    { 
                    if ( !users.loggedIn )
                        {
                        continue ;
                        }
                    else if ( users.username.equals( recipient ) )
                        {
                        sendTo = users ;
                        }
                    }
                
                if ( sendTo == null )
                    {
                    user.dataStreamOut.writeUTF( "\n> User not found, please try again" ) ;
                    return false ;
                    }

                user.dataStreamOut.writeUTF( "\n> What would you like to say?" ) ;
                String privateMessage = this.dataStreamIn.readUTF() ;
                user.dataStreamOut.writeUTF( "> sent: \"" + privateMessage + "\"" ) ;
                sendPrivateMessage( privateMessage, user.username, sendTo ) ;
                    
                }
            }

        catch ( IOException x )
            {
            while ( ChatServer.userList.size() != 0 )
                {
                System.out.println( x ) ;
                }
            }

        return false ;
        } // end commands


    // Convenience method to send messages privately
    private static void sendPrivateMessage( String message, final String sentfrom, final ClientHandler sendto )
        {
        try
            {
            for ( ClientHandler users : ChatServer.userList )
                {
                if ( users.equals( sendto ) )
                    {
                    message = String.format( "\nFrom <%s>: %s", sentfrom, message ) ;
                    users.dataStreamOut.writeUTF( message ) ;
                    }
                }
            }
        catch ( final IOException x )
            {
            x.printStackTrace() ;
            }
        }


    // Convenience method to distribute messages to all clients
    private static void sendMessage( String message )
        {
        try
            {
            for ( ClientHandler users : ChatServer.userList )
                {
                if ( !users.loggedIn )
                    {
                    continue ;
                    }
                users.dataStreamOut.writeUTF( message ) ;
                }
            }
        catch ( IOException x )
            {
            x.printStackTrace() ;
            }
        }
    }
// end class ClientHandler