package devmop.music.catalogue

import groovy.transform.CompileStatic
import org.junit.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.samePropertyValuesAs

/**
 * @author ( michael )
 */
@CompileStatic
class RecordingRepositoryTest
{

  def Repository repository = new MapRepository();

  @Test
  void canSaveARecording() {
    def recording = new Recording("")
    def id = repository.save(recording)
    def retrievedRecording = repository.find(id)

    assertThat(retrievedRecording, samePropertyValuesAs(recording))
  }
}
