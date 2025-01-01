package com.pratik;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application<AppConfiguration>
{
    public static void main(String[] args) throws Exception
    {
        new Main().run(args);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) throws Exception {

    }

    @Override
    public void initialize (Bootstrap<AppConfiguration> bootstrap)
    {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        super.initialize(bootstrap);
    }
}