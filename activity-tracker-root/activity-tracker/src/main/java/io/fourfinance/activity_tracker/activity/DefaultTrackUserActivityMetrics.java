package io.fourfinance.activity_tracker.activity;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Optional;

public class DefaultTrackUserActivityMetrics implements TrackUserActivityMetrics {

    private final MetricRegistry metricRegistry;
    private final Map<String, Meter> metrics = new HashMap<>();

    public DefaultTrackUserActivityMetrics(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void process(String activityName) {
        if (activityName != null) {
            
            Optional<Meter> metric = getMetric(activityName);
            if (!metric.isPresent()) {
                metric = initializeMetric(activityName);
            }
            if(metric.isPresent()) {
                metric.get().mark();
            }
        }
    }

    private Optional<Meter> initializeMetric(String activityName) {
        Meter meter = metricRegistry.meter("ACTIVITY." + activityName);
        metrics.put(activityName, meter);
        return Optional.of(meter);
    }
    
    private Optional<Meter> getMetric(String activityName) {
        return Optional.fromNullable(metrics.get(activityName));
    }
}
