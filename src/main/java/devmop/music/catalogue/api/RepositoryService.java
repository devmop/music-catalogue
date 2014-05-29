package devmop.music.catalogue.api;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author (michael)
 */
public class RepositoryService extends Application<Configuration>
{
  @Override
  public void initialize(final Bootstrap<Configuration> bootstrap)
  {
  }

  @Override
  public void run(final Configuration configuration, final Environment environment) throws Exception
  {
    environment.jersey().register(new RecordingAPI(configuration.getRepositoryFactory().build()));
  }
}
