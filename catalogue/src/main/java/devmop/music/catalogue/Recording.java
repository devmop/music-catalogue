package devmop.music.catalogue;

/**
 * @author (michael)
 */
public class Recording
{
  private final String title_;

  public Recording(final String title)
  {
    assert title != null;
    title_ = title;
  }

  public String getTitle()
  {
    return title_;
  }

  public Recording withTitle(final String title)
  {
    return new Recording(title);
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Recording recording = (Recording) o;

    return title_.equals(recording.title_);
  }

  @Override
  public int hashCode()
  {
    return title_.hashCode();
  }
}
