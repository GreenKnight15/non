package com.github.greenknight15.non.health;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class NoNHealthCheck implements IHealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("successful-check");
    }
}