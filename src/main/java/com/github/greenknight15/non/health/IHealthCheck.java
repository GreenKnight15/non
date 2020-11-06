package com.github.greenknight15.non.health;

import org.eclipse.microprofile.health.HealthCheckResponse;

@FunctionalInterface
public interface IHealthCheck {

    HealthCheckResponse call();
}