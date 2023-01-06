import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;



 class server extends JFrame{
   
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    JLabel heading=new JLabel("server Area");
    JTextArea messageArea=new JTextArea();
    JTextField messageInput=new JTextField();
    Font font=new Font("Roboto",Font.PLAIN,20);


    //constractor....
    public server(){

        try {
            server=new ServerSocket(7778);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting...");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startwriting();
            
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

        private void handleEvents() {

            messageInput.addKeyListener(new KeyListener(){

                @Override
                public void keyTyped(KeyEvent e) {
                    
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    
                     //System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("you hava pressed enter button");
                    String contentTOSend=messageInput.getText();
                    messageArea.append("server: "+contentTOSend+"\n");
                    out.println(contentTOSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                    
                }
    });
}

        private void  createGUI() {
            this.setTitle("server messager[End]");
            this.setSize(600,600);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            heading.setFont(font);
            messageArea.setFont(font);
            messageInput.setFont(font);
            messageArea.setEditable(false);
    
            this.setLayout(new BorderLayout());
            this.add(heading,BorderLayout.NORTH);
            JScrollPane jScrollPane=new JScrollPane(messageArea);
           
            this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageInput,BorderLayout.SOUTH);
            heading.setHorizontalAlignment(JLabel.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            messageInput.setHorizontalAlignment(SwingConstants.CENTER);
            
            
            
            this.setVisible(true);
    
         }

    
    public void startReading(){

        //thread- passing after reading
        Runnable r1=()->{
            System.out.println("Reader started...");
            try{
            while ( true) {
               
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Client has terminated the chat");
                    JOptionPane.showMessageDialog(this, "client has terminated the chat");
                        messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
               // System.out.println("Client: "+msg);
               messageArea.append("client: "+msg+"\n");
           
                
            }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("connection closed");
        }


        };
       new Thread(r1).start();


    }
    public void  startwriting(){

        //thread-user takes data and send it to client
        Runnable r2=()->{
            System.out.println("writer started...");
            try{
            while (!socket.isClosed()) 
            {
               

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

               }

               System.out.println("connection closed");
            }catch(Exception e){
                e.printStackTrace();
            }

        };
        new Thread(r2).start();
    }


   
    public static void main(String[] args) {
        System.out.println("this is server...going to start server");
        new server();
    }
}