package devmop.music.catalogue;

/**
 * @author (michael)
 */
public interface Repository
{
  public ID save(Recording recording);
  public Recording find(ID id);
}
