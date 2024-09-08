import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTests {

  @BeforeEach
  public void tearDown() {
    new File("test").delete();
  }

  @Test
  public void test() throws IOException {
    MessageHandler.load(new File("test"));
    Assertions.assertEquals(
        "Du musst ein Spieler für diesen Befehl sein.", Messages.INSTANCE.getNoPlayer()

    );

  }


  @Test
  public void test2() throws IOException {
    MessageHandler.load(new File("test"));
    Messages.INSTANCE.setNoPermissions("testMessage");
    MessageHandler.load(new File("test"));
    Assertions.assertNotEquals(
        "testMessage", Messages.INSTANCE.getNoPermissions()

    );


  }

}
