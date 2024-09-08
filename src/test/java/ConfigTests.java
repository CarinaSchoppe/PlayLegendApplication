import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigTests {


  @BeforeEach
  public void tearDown() {
    new File("test").delete();
  }


  @Test
  public void test() throws IOException {
    ConfigurationHandler.load(new File("test"));
    Assertions.assertEquals(
        "root", Configuration.INSTANCE.getUsername()
    );
  }


  @Test
  public void test2() throws IOException {
    ConfigurationHandler.load(new File("test"));
    Configuration.INSTANCE.setDatabase("mysql");
    ConfigurationHandler.load(new File("test"));
    Assertions.assertNotEquals(
        "sqlite", Configuration.INSTANCE.getDatabase()

    );


  }
}
