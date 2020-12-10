package demonstration;

import java.net.* ;
import java.io.* ;

@SuppressWarnings( "javadoc" )
public class ClientDEMO 
    {
    
    static LoginDEMO loginDemo = new LoginDEMO() ;
    static ChatDEMO chatRoom = new ChatDEMO() ;
    
    @SuppressWarnings( "javadoc" )
    public static void main( String[] args ) throws IOException
        {
        
        loginDemo.setVisible( true ); 

        boolean done = false ;
        while( !done ) 
            {
            done = loginDemo.isDone() ;          
            System.out.print("");
            }
       
        String ip = loginDemo.getIP() ;
        String username = loginDemo.getUsername() ;
        
        // Open your connection to a server, at port 6789
        Socket socket = new Socket( ip , 6789 ) ;
        
        // Create communication streams
        DataInputStream dataStreamIn = new DataInputStream( socket.getInputStream() ) ;    
        DataOutputStream dataStreamOut = new DataOutputStream( socket.getOutputStream() ) ;             
        
        dataStreamOut.writeUTF( username ) ;
        
        chatRoom.setTitle( "logged in as: " + username ) ;
        chatRoom.setVisible( true ) ;
        
        // Thread for sending messages
        Thread sendMessage = new Thread(new Runnable()
            {
            @Override
            public void run() 
                {                              
                while( true )
                    {
                    try
                        {
                        // if-statement is only triggered if client closes window (disconnects)
                        if( !chatRoom.isVisible() )
                            {
                            dataStreamOut.writeUTF( "/quit" ) ;
                            dataStreamIn.close() ;
                            dataStreamOut.close() ;
                            socket.close() ;
                            System.exit( 0 ) ;
                            }
                        // extract message information from GUI text field
                        if( chatRoom.messageReady )
                            {
                            String messageOut = chatRoom.getMessageOut() ;     
                            if( messageOut == null )
                                {
                                continue ;
                                }
                            dataStreamOut.writeUTF( messageOut ) ;                            
                            }
                        System.out.print("") ;
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
                       chatRoom.printMessage( messageIn ) ;                            
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