package devmop.music.catalogue;

import org.junit.Test;

import devmop.music.catalogue.client.RemoteRecordingRepository;

import static devmop.music.catalogue.RepositoryFactory.Implementation.*;

public class RepositoryFactoryTest
{
  @Test(expected = IllegalArgumentException.class)
  public void databaseConfigNotValidWithoutURL() throws Exception
  {
    new RepositoryFactory(RepositoryFactory.Implementation.DATABASE, null, null);
  }

  @Test
  public void databaseImplementationWithURLBuildsDatabaseRepo() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory(DATABASE,
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL", null).build();
    assert (repository instanceof DatabaseBackedRecordingRepository);
  }

  @Test
  public void defaultConfigGivesMap() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory().build();
    assert (repository instanceof MapBackedRecordingRepository);
  }

  @Test
  public void mapImplementationBuildsMapBackedConfig() throws Exception
  {
    RecordingRepository repository = new RepositoryFactory(MAP, null, null).build();
    assert (repository instanceof MapBackedRecordingRepository);
  }

  @Test
  public void remoteImplementationWithHostBuildsRemoteRepo() throws Exception {
    RecordingRepository repository = new RepositoryFactory(REMOTE, null, "localhost").build();
    assert (repository instanceof RemoteRecordingRepository);
  }

  @Test(expected = IllegalArgumentException.class)
  public void remoteImplementationWithMissingHostFailsToBuild() throws Exception {
    RecordingRepository repository = new RepositoryFactory(REMOTE, null, null).build();
  }
}
