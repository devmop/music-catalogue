package devmop.music.catalogue.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import devmop.music.catalogue.RepositoryFactory;

import javax.validation.constraints.NotNull;

/**
 * @author (michael)
 */
public class Configuration extends io.dropwizard.Configuration
{
  private RepositoryFactory repositoryFactory_ = new RepositoryFactory(null, null);

  @NotNull
  public RepositoryFactory getRepositoryFactory()
  {
    return repositoryFactory_;
  }

  @JsonProperty("repository")
  public void setRepositoryFactory(final RepositoryFactory repositoryFactory)
  {
    repositoryFactory_ = repositoryFactory;
  }
}
