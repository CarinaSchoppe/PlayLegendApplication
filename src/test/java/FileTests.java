import com.carinaschoppe.playLegendBewerbung.utility.FileHandler;
import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileTests {

  @AfterEach
  public void tearDown() {
    //delete PlayLegend folder and database.db file
    new File("PlayLegend").delete();
    new File("database.db").delete();
  }

  @Test
  public void fileExistent() {
    FileHandler.makePluginFolder(new File("PlayLegend"));
    Assertions.assertTrue(new File("PlayLegend").exists());
  }

  @Test
  public void createDatabaseFile() {
    FileHandler.makeDatabaseFile(new File("database.db"));
    Assertions.assertTrue(new File("database.db").exists());
  }

}


