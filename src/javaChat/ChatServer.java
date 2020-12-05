package javaChat;

import java.net.* ;
import java.util.* ;
import java.io.* ;

public class ChatServer
    {

    // List of all active users in Server
    static ArrayList<ClientHandler> userList = new ArrayList<>() ;
    
    /**
     * Accepts Client requests to join chat server, and
     * creates new thread for each individual client.
     *
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException
        {
        
        // Register service on port 6789
        ServerSocket serverSocket = new ServerSocket( 6789 ) ;
        Socket socket = null ;
        
        System.out.println( "\nServer established. Awaiting connections..." ) ;
        
        // Accept client requests
        while( true )
            {
            socket = serverSocket.accept() ;
            
            DataOutputStream dataStreamOut = new DataOutputStream( socket.getOutputStream() ) ;        
            DataInputStream dataStreamIn = new DataInputStream ( socket.getInputStream() ) ;
            
            System.out.println( "\nClient connection detected, requesting username..." ) ;
            String username = dataStreamIn.readUTF() ;
            
            ClientHandler handle = new ClientHandler( socket, username, dataStreamOut, dataStreamIn ) ;
            
            System.out.println( "\nCreating new thread...\n" ) ;
            Thread thread = new Thread( handle ) ;
            
            userList.add( handle ) ;
            
            thread.start() ;
            
            }
        }

    }
    // end class ChatClient




    // This class manages the actual messages being sent around
    @SuppressWarnings( "javadoc" )
    class ClientHandler implements Runnable
        {
        
        Socket socket ;
        String username ;
        final DataOutputStream dataStreamOut ;
        final DataInputStream dataStreamIn ;
        boolean loggedIn ;
        private ArrayList<ClientHandler> userList ;
    
        
    /**
     * Constructor
     * @param s : client socket
     * @param user  : client's user-name
     * @param dos : data stream to client
     * @param dis : data stream from client
     */
    public ClientHandler( Socket s, String user, 
                          DataOutputStream dos,
                          DataInputStream dis )
        {
        this.username = user ;
        this.socket = s ;
        this.dataStreamOut = dos ;
        this.dataStreamIn = dis ;
        this.loggedIn = true ;
        
        }
    
    @Override
    public void run() 
        {
        
        // Note the connection of the new user
        String announcement = String.format( "-= User \"%s\" has successfully connected =-\n", this.username ) ;
        System.out.println( announcement ) ;
       
        // Distribute announcement to entire server
        distributeAll( announcement ) ;
            
        // Message management
        String message = "" ;      
        while( true ) 
            {
                // Read incoming messages
                try
                    {
                    message = this.dataStreamIn.readUTF() ;
                    }
                catch ( IOException x )
                    {
                    x.printStackTrace() ;
                    }      
                
                // Check message for user commands, returns TRUE is user quits
                if( commands(message, this) )
                    {
                    break ;
                    }
                
                // Print the message server-side
                message = String.format( "%s: %s", this.username, message ) ;
                System.out.println( message ) ;
                
                // Distribute the message to all users
                sendMessage( message ) ;
                // end try
            
            }
        
            try
                {
                this.loggedIn = false ;
                this.socket.close() ;
                }
            catch( IOException x )
                {
                x.printStackTrace() ;
                }
        }
 
    
    // This method manages the usage of user-commands
    public boolean commands(String message, ClientHandler user) 
        {
        String[] commands = new String[] {"/help", "/quit", "/userlist"} ;
        
        try {
            
            // Quit command terminates connection, with an announcement
            if( message.equals( "/quit" )) 
                {
                String announcement = String.format( "\n-= \"%s\" has disconnected =- \n", user.username ) ;
                System.out.println( announcement ) ;
                distributeAll( announcement ) ;
                return true ;
                }
            
            if( message.equals( "/userlist" ) ) 
                {
                user.dataStreamOut.writeUTF( "Current list of users in the server: " ) ;
                for( ClientHandler users : ChatServer.userList )
                    {                    
                    if( !users.loggedIn )
                        {
                        continue ;
                        }
                    
                    user.dataStreamOut.writeUTF( users.username ) ;
                    
                    }   
                }
            
            if( message.contentEquals( "/help" ) )
                {
                user.dataStreamOut.writeUTF( "\nAll available commands: " );
                for(int i = 0; i < commands.length; i++ ) 
                    {
                    user.dataStreamOut.writeUTF( commands[i] );
                    }
                
                    user.dataStreamOut.writeUTF( "\n" ) ;
                }
        }
        
        catch( IOException x )
            {
            System.out.println( x ) ;
            }
        
        return false ;
        }
    
    
    // Convenience method to send messages (but not to the user that's sending the message)
    private void sendMessage(String message)
        {
        try
            {      
            for( ClientHandler users : ChatServer.userList )
                {                    
                if( !users.loggedIn || users.equals( this ) )
                    {
                    continue ;
                    }
                users.dataStreamOut.writeUTF( "> " + message ) ;
                }    
            }
        catch ( IOException x )
            {
            x.printStackTrace() ;
            }
        }
    
    
    // Convenience method to distribute messages to all clients (announcements)
    private void distributeAll(String message)
        {
        try
            {      
            for( ClientHandler users : ChatServer.userList )
                {                    
                if( !users.loggedIn )
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