package devmop.music.catalogue;

import org.junit.Test;

public class RecordingTest
{
  @Test(expected = IllegalArgumentException.class)
  public void nullNotAllowed() {
    new Recording(null);
  }

  @Test
  public void notEqualToNull() {
    assert !new Recording("").equalTo(null);
  }

  @Test
  public void equalToSelf() {
    Recording recording = new Recording("");
    assert recording.equalTo(recording);
  }

  @Test
  public void notEqualIfTitlesDiffer() {
    assert !new Recording("a").equalTo(new Recording("b"));
  }
}
