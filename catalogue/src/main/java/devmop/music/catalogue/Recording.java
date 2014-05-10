package devmop.music.catalogue;

/**
 * @author (michael)
 */
public class Recording
{
  private final String title_;

  public Recording(final String title)
  {
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
}
