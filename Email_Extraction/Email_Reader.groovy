//import javax.mail.Message
//import javax.mail.MessagingException
//import javax.mail.Session
//import javax.mail.Transport
//import javax.mail.internet.InternetAddress
//import javax.mail.internet.MimeMessage
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Properties
import javax.*


Properties props = System.getProperties();
Session session = Session.getDefaultInstance(props, null);
//Store store = null;
//Folder folder = null;

File tmpFile = new File("D:\\EAI\\Email\\60e4695f-c67d-4bf1-a91c-8766ea34d57b");
MimeMessage email = null;
try {
    FileInputStream fis = new FileInputStream(tmpFile);
    email = new MimeMessage(session, fis);
    System.out.println("content type: " + email.getContentType());
    System.out.println("\nsubject: " + email.getSubject());
    System.out.println("\nrecipients: " + Arrays.asList(email.getRecipients(Message.RecipientType.TO)));
} catch (MessagingException e) {
    throw new IllegalStateException("illegal state issue", e);
} catch (FileNotFoundException e) {
    throw new IllegalStateException("file not found issue issue: " + tmpFile.getAbsolutePath(), e);
}
boolean success = processMethod(email);
