package devmop.music.catalogue;

import java.util.Enumeration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.rules.ExternalResource;

import com.google.common.collect.ImmutableMap;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ServerCommand;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.ConfigOverride;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * @author michael
 */
public class DropwizardAppRule<C extends Configuration> extends ExternalResource {

  private final Class<? extends Application<C>> applicationClass;
  private final String configPath;

  private Server jettyServer;

  public DropwizardAppRule(Class<? extends Application<C>> applicationClass,
                           String configPath,
                           ConfigOverride... configOverrides) {
    this.applicationClass = applicationClass;
    this.configPath = configPath;
    for (ConfigOverride configOverride: configOverrides) {
      configOverride.addToSystemProperties();
    }
  }

  @Override
  protected void before() throws Throwable {
    startIfRequired();
  }

  protected void after() {
    resetConfigOverrides();
    try {
      jettyServer.stop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void resetConfigOverrides() {
    for (Enumeration<?> props = System.getProperties().propertyNames(); props.hasMoreElements();) {
      String keyString = (String) props.nextElement();
      if (keyString.startsWith("dw.")) {
        System.clearProperty(keyString);
      }
    }
  }

  private void startIfRequired() {
    if (jettyServer != null) {
      return;
    }

    try {
      Application<C> application = newApplication();

      final Bootstrap<C> bootstrap = new Bootstrap<C>(application) {
        @Override
        public void run(C configuration, Environment environment) throws Exception {
          environment.lifecycle().addServerLifecycleListener(new ServerLifecycleListener() {
            @Override
            public void serverStarted(Server server) {
              jettyServer = server;
            }
          });
          super.run(configuration, environment);
        }
      };

      application.initialize(bootstrap);
      final ServerCommand<C> command = new ServerCommand<C>(application);
      final Namespace namespace = new Namespace(ImmutableMap.<String, Object>of("file", configPath));
      command.run(bootstrap, namespace);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public int getLocalPort() {
    return ((ServerConnector) jettyServer.getConnectors()[0]).getLocalPort();
  }

  public Application<C> newApplication() {
    try {
      return applicationClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
