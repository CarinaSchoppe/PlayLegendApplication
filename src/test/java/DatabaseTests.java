import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.database.DefaultRankGeneration;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.FileHandler;
import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
public class DatabaseTests {


  public void setup() {

    teardown();
    //recusively delete test folder and all its contents
    FileHandler.makePluginFolder(new File(""));
    FileHandler.makeDatabaseFile(new File("database.db"));
    ConfigurationHandler.load(new File(""));

  }


  private void deleteRecursively(File file) {
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        deleteRecursively(child);
      }
    }
    file.delete();
  }


  public void teardown() {
    if (DatabaseServices.getDatabase() != null) {
      DatabaseServices.getDatabase()
          .shutdown(); // Oder eine ähnliche Methode, um die Datenbank zu schließen
      DatabaseServices.setDatabase(null);         // Datenbank-Instanz zurücksetzen
      Messages.INSTANCE = null;
      Configuration.INSTANCE = null;

    }
    // Lösche alle Dateien rekursiv im Ordner "test"
    File databaseFile = new File("database.db");
    File testDirectory = new File("");

    if (databaseFile.exists()) {
      databaseFile.delete();
    }

    if (testDirectory.exists()) {
      // Lösche den Ordner rekursiv und alle Inhalte
      deleteRecursively(testDirectory);
    }
  }


  @Test
  public void test() {
    setup();
    Assertions.assertTrue(new File("database.db").exists());
    teardown();
  }


  @Test
  public void test2() {
    setup();
    var database = DatabaseServices.createDatabase(new File("database.db"));
    Assertions.assertNotNull(database);
    teardown();

  }

  @Test
  public void test3() {
    setup();
    var database = DatabaseServices.createDatabase(new File("database.db"));
    DatabaseServices.loadPlayers();
    Assertions.assertNotNull(database);
    Assertions.assertEquals(0, DatabaseServices.DATABASE_PLAYERS.size());

    teardown();
  }


  @Test
  public void test4() {
    setup();
    DatabaseServices.createDatabase(new File("database.db"));
    DatabaseServices.loadRanks();
    Assertions.assertTrue(DatabaseServices.DATABASE_RANK.size() < 2);

    teardown();
  }

  @Test
  public void test8() {
    setup();
    var database = DatabaseServices.createDatabase(new File("database.db"));
    DefaultRankGeneration.loadDefaultRanks();
    DatabaseServices.loadRanks();

    Assertions.assertNotNull(database);
    Assertions.assertEquals(1, DatabaseServices.DATABASE_RANK.size());

    teardown();
  }

  @Test
  public void test5() {
    setup();
    var database = DatabaseServices.createDatabase(new File("database.db"));
    DefaultRankGeneration.loadDefaultRanks();
    DatabaseServices.loadRanks();
    var rank = DatabaseServices.DATABASE_RANK.stream().toList().getFirst();
    Assertions.assertNotNull(database);
    Assertions.assertEquals("default",
        rank.getRankName());

    teardown();
  }


  @Test
  public void test6() {
    setup();
    DatabaseServices.createDatabase(new File("database.db"));
    DefaultRankGeneration.loadDefaultRanks();
    DatabaseServices.loadRanks();
    var rankWithNameTest1 = DatabaseServices.getDatabase()
        .find(DatabaseRank.class).where().eq("rank_name", "test").findOne();
    Assertions.assertNull(rankWithNameTest1);


    teardown();
  }

  @Test
  public void test7() {
    setup();
    DatabaseServices.createDatabase(new File("database.db"));
    DefaultRankGeneration.loadDefaultRanks();
    DatabaseServices.loadRanks();

    var rankWithNameTest1 = DatabaseServices.getDatabase()
        .find(DatabaseRank.class).where().eq("rank_name", "test").findOne();
    Assertions.assertNull(rankWithNameTest1);
    teardown();
  }

  @Test
  public void test10() {
    setup();
    DatabaseServices.createDatabase(new File("database.db"));
    DefaultRankGeneration.loadDefaultRanks();
    DatabaseServices.loadRanks();
    var rank = DatabaseServices.getDatabase()
        .find(DatabaseRank.class).where().eq("rank_name", "default").findOne();
    rank.setRankName("test123");
    rank.save();
    var rankWithNameTest = DatabaseServices.getDatabase()
        .find(DatabaseRank.class).where().eq("rank_name", "test123").findOne();
    Assertions.assertNotNull(rankWithNameTest);

    teardown();
  }

}
