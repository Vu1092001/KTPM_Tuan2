package Screen;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.BasicConfigurator;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ScreenChat extends JFrame {

	private JPanel contentPane;
	String allMess = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScreenChat frame = new ScreenChat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}

	/**
	 * Create the frame.
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public ScreenChat() throws NamingException, JMSException {
		setTitle("Subcriber");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 523);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "N\u1ED9i Dung", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 0, 475, 476);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextArea txtTinNhan = new JTextArea();
		txtTinNhan.setBounds(10, 22, 455, 344);
		panel.add(txtTinNhan);
		
//		JTextArea txtNhap = new JTextArea();
//		txtNhap.setBounds(81, 390, 297, 64);
//		panel.add(txtNhap);
		
//		JButton btnSend = new JButton("Send");
//		btnSend.setBounds(388, 390, 77, 64);
//		panel.add(btnSend);
		
//		JLabel lblNewLabel = new JLabel("Enter Text");
//		lblNewLabel.setBounds(10, 390, 69, 64);
//		panel.add(lblNewLabel);
		
		//Nháº­n tin nháº¯n----------------------------------------------------------------------------
		//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JMS
		BasicConfigurator.configure();
		//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JJNDI
		Properties settings=new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
		"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		//táº¡o context
		Context ctx=new InitialContext(settings);
		//lookup JMS connection factory
		Object obj=ctx.lookup("TopicConnectionFactory");
		ConnectionFactory factory=(ConnectionFactory)obj;
		//táº¡o connection
		Connection con=factory.createConnection("admin","admin");
		//ná»‘i Ä‘áº¿n MOM
		con.start();
		//táº¡o session
		Session session=con.createSession(
			/*transaction*/false,
			/*ACK*/Session.CLIENT_ACKNOWLEDGE
		);
		//táº¡o consumer
		Destination destination=(Destination) ctx.lookup("dynamicTopics/thanthidet");
		MessageConsumer receiver = session.createConsumer(destination);
		//receiver.receive();//blocked method
		//Cho receiver láº¯ng nghe trÃªn queue, chá»«ng cÃ³ message thÃ¬ notify	
		receiver.setMessageListener(new MessageListener() {
			@Override
			//cÃ³ message Ä‘áº¿n queue, phÆ°Æ¡ng thá»©c nÃ y Ä‘Æ°á»£c thá»±c thi
			public void onMessage(Message msg) {//msg lÃ  message nháº­n Ä‘Æ°á»£c
				try {
					if(msg instanceof TextMessage){
						TextMessage tm=(TextMessage)msg;						
						String txt=tm.getText();
						addMess(txt);
						msg.acknowledge();//gá»­i tÃ­n hiá»‡u ack
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}

			private void addMess(String txt) {
				// TODO Auto-generated method stub
				System.out.println(txt);
				if(!txt.equals("")) {
					allMess +=txt + "\n";
					txtTinNhan.setText(allMess);
				}
			}
		});
		//-----------------------------------------------------------------------------------------		
//		btnSend.addActionListener(new ActionListener() {		
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				String s = txtNhap.getText();
//				try {
//					if(!s.equalsIgnoreCase("")) {
//						Send(s);
//						txtNhap.setText("");
//					}
//						
//				} catch (JMSException | NamingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
	}
	
	public void Send(String s) throws JMSException, NamingException {
		//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JMS logging
			BasicConfigurator.configure();
			//thiáº¿t láº­p mÃ´i trÆ°á»�ng cho JJNDI
			Properties settings=new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
			"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			//táº¡o context
			Context ctx=new InitialContext(settings);
			//lookup JMS connection factory
			Object obj=ctx.lookup("TopicConnectionFactory");
			ConnectionFactory factory=(ConnectionFactory)obj;
			//táº¡o connection
			Connection con=factory.createConnection("admin","admin");
			//ná»‘i Ä‘áº¿n MOM
			con.start();
			//táº¡o session
			Session session=con.createSession(
				/*transaction*/false,
				/*ACK*/Session.AUTO_ACKNOWLEDGE
			);
			Destination destination=(Destination)
			ctx.lookup("dynamicTopics/thanthidet");
			//táº¡o producer
			MessageProducer producer = session.createProducer(destination);
			//Táº¡o 1 message
			Message msg=session.createTextMessage(s);
			//gá»­i
			producer.send(msg);
			//shutdown connection
			session.close();
			con.close();
	}	
}
