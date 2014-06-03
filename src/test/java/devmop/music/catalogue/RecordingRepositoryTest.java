package devmop.music.catalogue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import devmop.music.catalogue.api.Configuration;
import devmop.music.catalogue.api.RecordingAPI;
import devmop.music.catalogue.api.RepositoryService;

import static devmop.music.catalogue.RepositoryFactory.Implementation.DATABASE;
import static devmop.music.catalogue.RepositoryFactory.Implementation.MAP;
import static devmop.music.catalogue.RepositoryFactory.Implementation.REMOTE;

/**
 * @author ( michael )
 */
@RunWith(Parameterized.class)
public class RecordingRepositoryTest
{
  private static int DBCOUNT = 0;

  @ClassRule
  public static final DropwizardAppRule<Configuration> SERVICE
          = new DropwizardAppRule<Configuration>(RepositoryService.class, "config/test/test-config.yml");

  @Parameterized.Parameters(name = "{1}")
  @SuppressWarnings("unchecked")
  public static Iterable<Object[]> implementations() throws Throwable {
    Object[] mapBacked = new Object[]{map(), "map"};
    Object[] databaseBacked = new Object[]{database(), "database"};
    Object[] api = new Object[]{api(), "api"};
    Object[] client = new Object[]{client(), "client"};
    return (Iterable) Arrays.asList(mapBacked, databaseBacked, api, client);
  }

  private static RecordingRepository api()
  {
    return new RecordingAPI(map());
  }

  private static RecordingRepository map()
  {
    return new RepositoryFactory(MAP, null, null).build();
  }

  private static RecordingRepository database()
  {
    return new RepositoryFactory(DATABASE,
        "jdbc:h2:mem:test" + (DBCOUNT++) + ";DB_CLOSE_DELAY=-1;MODE=PostgreSQL", null).build();
  }

  private static RecordingRepository client() throws Throwable {
    SERVICE.before(); //start service now. ClassRule is run at the wrong time
    return new RepositoryFactory(REMOTE, null, "localhost:" + SERVICE.getLocalPort()).build();
  }

  final RecordingRepository repository;

  public RecordingRepositoryTest(final RecordingRepository repository, String name)
  {
    this.repository = repository;
  }

  @Before
  public void cleanup()
  {
    repository.deleteAll();
  }

  @Test
  public void canSaveARecording()
  {
    Recording recording = makeRecording();
    ID id = repository.create(recording);
    Recording retrievedRecording = repository.find(id);

    assertTrue(retrievedRecording.equalTo(recording));
  }

  @Test
  public void canUpdateTheStoredRecording()
  {
    Recording original = makeRecording();
    ID id = repository.create(original);
    Recording updated = original.withTitle("updated");
    repository.update(id, updated);

    Recording retrieved = repository.find(id);
    assertTrue(retrieved.equalTo(updated));
  }

  @Test(expected = NotFoundException.class)
  public void canOnlyUpdateIDsThatExist()
  {
    repository.update(new ID(0L), new Recording(""));
  }

  @Test(expected = NotFoundException.class)
  public void exceptionForUnknownIds()
  {
    assertThat(repository.find(createId()), nullValue(Recording.class));
  }

  @Test(expected = NotFoundException.class)
  public void canDeleteARecordingFromTheRepository()
  {
    ID id = addNewRecording();
    repository.delete(id);
    repository.find(id);
  }

  @Test(expected = NotFoundException.class)
  public void cannotDeleteUnknownId()
  {
    repository.delete(createId());
  }

  @Test
  public void canDeleteEverything() {
    ID id1 = addNewRecording();
    ID id2 = addNewRecording();
    ID id3 = addNewRecording();

    repository.deleteAll();

    assertAbsent(id1, id2, id3);
  }

  private void assertAbsent(final ID... ids)
  {
    for (ID id : ids)
    {
      try
      {
        repository.find(id);
        fail();
      }
      catch (NotFoundException e)
      {

      }
    }
  }

  private ID createId()
  {
    return new ID(0L);
  }

  private ID addNewRecording()
  {
    return repository.create(makeRecording());
  }

  private Recording makeRecording()
  {
    return new Recording("");
  }
}
