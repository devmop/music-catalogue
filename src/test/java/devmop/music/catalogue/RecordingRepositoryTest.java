package devmop.music.catalogue;

import java.util.Arrays;

import devmop.music.catalogue.api.RecordingAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static devmop.music.catalogue.RepositoryFactory.Implementation.*;

/**
 * @author ( michael )
 */
@RunWith(Parameterized.class)
public class RecordingRepositoryTest
{
  private static int DBCOUNT = 0;

  @Parameterized.Parameters(name = "{1}")
  @SuppressWarnings("unchecked")
  public static Iterable<Object[]> implementations()
  {
    Object[] mapBacked = new Object[]{map(), "map"};
    Object[] databaseBacked = new Object[]{database(), "database"};
    Object[] api = new Object[]{api(), "api"};
    return (Iterable) Arrays.asList(mapBacked, databaseBacked, api);
  }

  private static RecordingRepository api()
  {
    return new RecordingAPI(map());
  }

  private static RecordingRepository map()
  {
    return new RepositoryFactory(MAP, null).build();
  }

  private static RecordingRepository database()
  {
    return new RepositoryFactory(DATABASE,
        "jdbc:h2:mem:test" + (DBCOUNT++) + ";DB_CLOSE_DELAY=-1;MODE=PostgreSQL").build();
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
