package devmop.music.catalogue;

import com.googlecode.flyway.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
/**
 * @author ( michael )
 */
@RunWith(Parameterized.class)
public class RecordingRepositoryTest
{
  @Parameterized.Parameters
  @SuppressWarnings("unchecked")
  public static Iterable<Object[]> implementations()
  {
    Object[] mapBacked = new Object[]{new MapBackedRecordingRepository()};
    Object[] databaseBacked = new Object[]{setupDatabase()};
    return (Iterable) Arrays.asList(mapBacked, databaseBacked);
  }

  private static DatabaseBackedRecordingRepository setupDatabase() {
    JdbcDataSource source = new JdbcDataSource();
    source.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
    Flyway flyway = new Flyway();
    flyway.setInitOnMigrate(true);
    flyway.setDataSource(source);
    flyway.migrate();
    return new DatabaseBackedRecordingRepository(DSL.using(source, SQLDialect.POSTGRES));
  }

  final RecordingRepository repository;

  public RecordingRepositoryTest(final RecordingRepository repository)
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

    assert retrievedRecording.equalTo(recording);
  }

  @Test
  public void canUpdateTheStoredRecording()
  {
    Recording original = makeRecording();
    ID id = repository.create(original);
    Recording updated = original.withTitle("updated");
    repository.update(id, updated);

    Recording retrieved = repository.find(id);
    assert retrieved.equalTo(updated);
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
