package devmop.music.catalogue;

/**
 * @author (michael)
 */
public class Recording implements Equality<Recording>
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
  public boolean equalTo(final Recording recording)
  {
    return recording != null && title_.equals(recording.title_);
  }
}
