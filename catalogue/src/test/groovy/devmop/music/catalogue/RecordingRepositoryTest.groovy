package devmop.music.catalogue
import com.googlecode.flyway.core.Flyway
import groovy.transform.CompileStatic
import org.h2.jdbcx.JdbcDataSource
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.nullValue
import static org.junit.Assert.fail
/**
 * @author ( michael )
 */
@CompileStatic
@RunWith(Parameterized)
class RecordingRepositoryTest
{

  @Parameterized.Parameters
  static Iterable<Object[]> implementations()
  {
    def mapBacked = [new MapBackedRecordingRepository()].toArray()
    def databaseBacked = [setupDatabase()].toArray()
    return [mapBacked, databaseBacked]
  }

  private static DatabaseBackedRecordingRepository setupDatabase() {
    def source = new JdbcDataSource()
    source.setURL('jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL')
    def flyway = new Flyway()
    flyway.initOnMigrate = true
    flyway.setDataSource(source)
    flyway.migrate()
    new DatabaseBackedRecordingRepository(DSL.using(source, SQLDialect.POSTGRES))
  }

  final RecordingRepository repository

  RecordingRepositoryTest(final RecordingRepository repository)
  {
    this.repository = repository
  }

  @Before
  void cleanup()
  {
    repository.deleteAll()
  }

  @Test
  void canSaveARecording()
  {
    def recording = makeRecording()
    def id = repository.create(recording)
    def retrievedRecording = repository.find(id)

    assertThat retrievedRecording, equalTo(recording)
  }

  @Test
  public void canUpdateTheStoredRecording()
  {
    def original = makeRecording()
    def id = repository.create(original)
    def updated = original.withTitle("updated")
    repository.update(id, updated)

    def retrieved = repository.find(id)
    assertThat retrieved, equalTo(updated)
  }

  @Test(expected = NotFoundException)
  public void canOnlyUpdateIDsThatExist()
  {
    repository.update(new ID(0L), new Recording(""))
  }

  @Test(expected = NotFoundException)
  void exceptionForUnknownIds()
  {
    assertThat repository.find(createId()), nullValue(Recording)
  }

  @Test(expected = NotFoundException)
  void canDeleteARecordingFromTheRepository()
  {
    def id = addNewRecording()
    repository.delete(id);
    repository.find(id);
  }

  @Test(expected = NotFoundException)
  void cannotDeleteUnknownId()
  {
    repository.delete(createId())
  }

  @Test
  void canDeleteEverything() {
    def id1 = addNewRecording()
    def id2 = addNewRecording()
    def id3 = addNewRecording()

    repository.deleteAll();

    assertAbsent(id1, id2, id3)
  }

  void assertAbsent(final ID... ids)
  {
    ids.each
    { ID it ->
      try
      {
        repository.find(it)
        fail()
      }
      catch (NotFoundException e)
      {

      }
    }
  }

  private ID createId()
  {
    new ID(0L)
  }

  private ID addNewRecording()
  {
    repository.create(makeRecording())
  }

  private Recording makeRecording()
  {
    new Recording("")
  }
}
