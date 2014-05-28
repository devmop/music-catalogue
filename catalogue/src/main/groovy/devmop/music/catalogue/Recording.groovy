package devmop.music.catalogue

import groovy.transform.CompileStatic

/**
 * @author (michael)
 */
@CompileStatic
public class Recording implements Equality<Recording>
{
  private final String title_

  public Recording(final String title)
  {
    assert title != null
    title_ = title
  }

  public String getTitle()
  {
    return title_
  }

  public Recording withTitle(final String title)
  {
    return new Recording(title)
  }

  @Override
  public boolean equals(final Recording recording)
  {
    return recording != null && title.equals(recording.title)
  }
}
