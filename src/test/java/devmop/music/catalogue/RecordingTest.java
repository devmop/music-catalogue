package devmop.music.catalogue;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordingTest
{
  @Test(expected = IllegalArgumentException.class)
  public void nullNotAllowed() {
    new Recording(null);
  }

  @Test
  public void notEqualToNull() {
    assertFalse(new Recording("").equalTo(null));
  }

  @Test
  public void equalToSelf() {
    Recording recording = new Recording("");
    assertTrue(recording.equalTo(recording));
  }

  @Test
  public void notEqualIfTitlesDiffer() {
    assertFalse(new Recording("a").equalTo(new Recording("b")));
  }

  @Test
  public void equalIfTitlesMatch() throws Exception
  {
    assertTrue(new Recording("a").equalTo(new Recording("a")));
  }
}
