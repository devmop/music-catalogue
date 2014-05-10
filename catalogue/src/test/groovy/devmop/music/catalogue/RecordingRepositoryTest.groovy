package devmop.music.catalogue

import groovy.transform.CompileStatic
import org.junit.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.nullValue

/**
 * @author ( michael )
 */
@CompileStatic
class RecordingRepositoryTest
{

  def RecordingRepository repository = new MapBackedRecordingRepository()

  @Test
  void canSaveARecording() {
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
    repository.update(new ID(), new Recording(""))
  }

  @Test(expected = NotFoundException)
  void exceptionForUnknownIds()
  {
    assertThat repository.find(createId()), nullValue(Recording)
  }

  @Test(expected = NotFoundException)
  void canDeleteARecordingFromTheRepository()
  {
    def id = repository.create(makeRecording())
    repository.delete(id);
    repository.find(id);
  }

  @Test(expected = NotFoundException)
  void cannotDeleteUnknownId()
  {
    repository.delete(createId())
  }

  private ID createId()
  {
    new ID()
  }

  private Recording makeRecording()
  {
    new Recording("")
  }
}