package devmop.music.catalogue;

import org.junit.Test;

import static devmop.music.catalogue.RepositoryFactory.Implementation.*;

public class RepositoryFactoryTest
{
  @Test(expected = IllegalArgumentException.class)
  public void databaseConfigNotValidWithoutURL() throws Exception
  {
    new RepositoryFactory(RepositoryFactory.Implementation.DATABASE, null);
  }

  @Test
  public void databaseImplementationWithURLBuildsDatabaseRepo() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory(DATABASE,
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL").build();
    assert (repository instanceof DatabaseBackedRecordingRepository);
  }

  @Test
  public void defaultConfigGivesMap() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory(null, null).build();
    assert (repository instanceof MapBackedRecordingRepository);
  }

  @Test
  public void mapImplementationBuildsMapBackedConfig() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory(MAP, null).build();
    assert (repository instanceof MapBackedRecordingRepository);
  }
}
