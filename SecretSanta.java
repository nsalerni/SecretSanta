import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Flags;
import javax.mail.Store;
import javax.mail.Folder;

import java.io.Console;

import java.util.concurrent.ThreadLocalRandom;

public class SecretSanta {

	// 0 -> Nothing is paired to or paird with
	// 1 -> It is paired to something.
	// 2 -> Something pairs to it.
	private static String[][] array = {
		{"Name 1", "Wish List 1", "Email List 1", "0"},
		{"Name 2", "Wish List 2", "Email List 2", "0"},
		{"Name 3", "Wish List 3", "Email List 3", "0"}
	};

	public static String subject = "Secret Santa 2015";

	public static String bodyHeader1 = "Hey ";
	public static String bodyHeader2 = "! \n\n You are ";
	public static String bodyHeader3 = "'s Secret Santa. This is the wish list: \n";
	public static String bodyFooter = "\n\n Happy Holidays, \n -- \n Anonomus Secret Santa";

	/**
	 * Check if all pairs have been completely generated.	
	 */
	public static boolean isComplete(String[][] array) {
		for (String[] arr : array) {
	    	if (!arr[3].contains("0") || !arr[3].contains("1") || !arr[3].contains("2")) {
	    		return false;
	    	}
	    }
	    return true;
	}

	/**
     * Generate all random Secret Santa pairs (a person is assigned a person, and a person is given a person).
	 */
	public static void generateRandomPairs(String[][] array) {
		int min = 0;
		int max = array.length - 1;

		while(!isComplete(array)) {
			// nextInt is normally exclusive of the top value, so add 1 to make it inclusive.
			String[] first = array[ThreadLocalRandom.current().nextInt(min, max + 1)];
			String[] second = array[ThreadLocalRandom.current().nextInt(min, max + 1)];

			if (first[0] != second[0] && !first[3].contains("1") && !second[3].contains("2")) {
				first[3] += "1";
				second[3] += "2";

				sendEmail(first[2], subject, bodyHeader1 + first[0].split(" ")[0] + bodyHeader2 + second[0].split(" ")[0] + bodyHeader3 + second[1] + bodyFooter);
			}
		}
	}

	/**
	 * Sends an email containing the person you got for Secret Santa along with their wish list.
	 */
	public static void sendEmail(String receiver, String subject, String body) {	
		final String username = "username@domain.com";
		final String password = "PASSWORD";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

		try {
		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(username));
		    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
		    message.setSubject(subject);
		    message.setText(body);

		    Transport.send(message);	

		    System.out.println("Email Sent Successfully");

		} catch (MessagingException e) {
		    throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		generateRandomPairs(array);
	}
}
