package javaChat;

import java.net.* ;
import java.io.* ;

/**
 * 
 *
 * @author osheas1
 * @version 1.0.0 2020-12-07 Initial implementation
 *
 */
public class ClientGUI
    {

    public static void main( String[] args ) throws IOException
        {
        
        UserInterface login = new UserInterface() ;
        login.setVisible( true ); 

        boolean done = false ;
        while( !done ) 
            {
            done = login.isDone() ;          
            System.out.print("");
            }
       
        String ip = login.getIP() ;
        String username = login.getUsername() ;
        
        // Open your connection to a server, at port 6789
        Socket socket = new Socket( ip , 6789 ) ;
        
        // Create communication streams
        DataInputStream dataStreamIn = new DataInputStream( socket.getInputStream() ) ;    
        DataOutputStream dataStreamOut = new DataOutputStream( socket.getOutputStream() ) ;      

        BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ) ) ;
        
        dataStreamOut.writeUTF( username ) ;
        
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
	// end class ClientGUI