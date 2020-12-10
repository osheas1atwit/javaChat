package backend;

import java.net.* ;

import javax.swing.JFrame ;

import java.io.* ;

public class ChatClientCMD
    {

    /**
     * 
     *
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException
        {
        
        // Get server IP
        BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ) ) ;
        System.out.print( "\nPlease enter the IP of the desired server: " ); // GUI: IP BOX
        
        String serverIP = keyboard.readLine() ;
        
        // Open your connection to a server, at port 6789
        Socket socket = new Socket( serverIP , 6789 ) ;
        
        // Create communication streams
        DataInputStream dataStreamIn = new DataInputStream( socket.getInputStream() ) ;    
        DataOutputStream dataStreamOut = new DataOutputStream( socket.getOutputStream() ) ;
       
        // Establish connection, make user-name
        System.out.print( "\nConnection established,\nPlease enter a username: " ) ;       
        String username = keyboard.readLine() ;
        dataStreamOut.writeUTF( username ) ;
        
        /////////////////////////
        //// Program Running ////
        /////////////////////////
        
        // Thread for sending messages
        Thread sendMessage = new Thread(new Runnable()
            {
            @Override
            public void run() 
                {
                System.out.print( "\n-= Type /help for a list of commands =-" ) ;
                while( true )
                    {
                    try
                        {
                        String messageOut = keyboard.readLine() ;
                        dataStreamOut.writeUTF( messageOut ) ;
                        }
                    catch( IOException x )
                        {
                        x.printStackTrace() ;
                        System.exit( 0 ) ;
                        }
                    }
                }
            }) ;
        
        // Thread for receiving messages
        Thread receiveMessage = new Thread( new Runnable()
            {
            
            @Override
            public void run()
               {
               while( true )
                   {
                   try
                       {
                       String messageIn = dataStreamIn.readUTF() ;
                       System.out.printf( "%n%s\n", messageIn ) ;
                       }
                   catch( IOException x )
                       {
                       x.printStackTrace() ;
                       System.exit( 0 ) ;
                       } 
                   }
               }
            });
        
        // Begin threads
        sendMessage.start() ;
        receiveMessage.start() ;
        }
    }
	// end class ChatClient