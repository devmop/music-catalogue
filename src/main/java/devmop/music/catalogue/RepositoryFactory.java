package devmop.music.catalogue;

import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;

import devmop.music.catalogue.client.RemoteRecordingRepository;

/**
 * @author (michael)
 */
public class RepositoryFactory
{
  public enum Implementation
  {
    MAP, DATABASE, REMOTE
  }

  private final Implementation implementation_;
  private final String jdbcUrl_;
  private final String host_;

  public RepositoryFactory() {
    this(Implementation.MAP, null, null);
  }

  @JsonCreator
  public RepositoryFactory(@JsonProperty("implementation") Implementation implementation,
                           @JsonProperty("jdbc-url") String jdbcUrl,
                           @JsonProperty("remote-host") String host) {
    host_ = host;
    implementation_ = implementation;
    jdbcUrl_ = jdbcUrl;

    if(!isValid())
    {
      throw new IllegalArgumentException();
    }
  }

  private boolean isValid()
  {
    return (validMapConfig() || validDatabaseConfig() || validRemoteConfig());
  }

  private boolean validRemoteConfig() {
    return implementation_ == Implementation.REMOTE && host_ != null;
  }

  private boolean validMapConfig() {
    return implementation_ == Implementation.MAP;
  }

  private boolean validDatabaseConfig() {
    return implementation_ == Implementation.DATABASE && jdbcUrl_ != null;
  }

  public RecordingRepository build()
  {
    switch (implementation_) {
      case DATABASE: return createDatabaseRepository();
      case REMOTE: return createRemoteClient();
      default: return new MapBackedRecordingRepository();
    }
  }

  private RecordingRepository createRemoteClient() {
    return new RemoteRecordingRepository(host_);
  }

  private RecordingRepository createDatabaseRepository()
  {
    DataSource dataSource = new DriverDataSource(null, jdbcUrl_, null, null);
    migrateDatabase(dataSource);
    return new DatabaseBackedRecordingRepository(DSL.using(dataSource, SQLDialect.POSTGRES));
  }

  private void migrateDatabase(final DataSource dataSource)
  {
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource);
    flyway.migrate();
  }
}
