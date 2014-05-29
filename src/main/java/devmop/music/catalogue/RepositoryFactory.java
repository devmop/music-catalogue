package devmop.music.catalogue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

/**
 * @author (michael)
 */
public class RepositoryFactory
{
  public enum Implementation
  {
    MAP, DATABASE
  }

  private final Implementation implementation_;
  private final String jdbcUrl_;

  @JsonCreator
  public RepositoryFactory(@JsonProperty("implementation") Implementation implementation,
                            @JsonProperty("jdbc-url") String jdbcUrl) {
    implementation_ = implementation == null ? Implementation.MAP : implementation;
    jdbcUrl_ = jdbcUrl;

    if(!isValid())
    {
      throw new IllegalArgumentException();
    }
  }

  private boolean isValid()
  {
    return (implementation_ == Implementation.MAP  ||
            jdbcUrl_ != null);
  }

  public RecordingRepository build()
  {
    return implementation_ == Implementation.MAP ?
        new MapBackedRecordingRepository() : createDatabaseRepository();
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
