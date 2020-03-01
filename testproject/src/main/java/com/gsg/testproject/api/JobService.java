package com.gsg.testproject.api;

public interface JobService {

    /**
     * Schedules in memory job, deletes old if one exists for given user
     */
    void scheduleJob(String user, String countryCode, int rate);
}
